package ir.rasen.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.ReviewsAdapter;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.review.GetBusinessReviews;
import ir.rasen.myapplication.webservice.review.GetUserReviews;

/**
 * Created by 'Sina KH' on 1/13/2015.
 */
public class FragmentUserReviews extends Fragment implements WebserviceResponse, EditInterface {
    private static final String TAG = "FragmentReviews";

    private View view, listFooterView;

    private boolean isLoadingMore = false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private BaseAdapter mAdapter;

    private ArrayList<Review> reviews;

    private ProgressDialogCustom pd;

    public static FragmentUserReviews newInstance() {
        FragmentUserReviews fragment = new FragmentUserReviews();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentUserReviews() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pd = new ProgressDialogCustom(getActivity());
        new GetUserReviews(LoginInfo.getUserId(getActivity()),
                0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentUserReviews.this).execute();
        pd.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_reviews, container, false);
        this.view = view;

        list = (ListView) view.findViewById(R.id.list_user_reviews_review);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        reviews = new ArrayList<Review>();
        mAdapter = new ReviewsAdapter(getActivity(), reviews, FragmentUserReviews.this, FragmentUserReviews.this);
        ((AdapterView<ListAdapter>) view.findViewById(R.id.list_user_reviews_review)).setAdapter(mAdapter);

        return view;
    }

    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        if (reviews != null) {
            new GetUserReviews(LoginInfo.getUserId(getActivity()),
                    reviews.get(reviews.size()-1).id, getResources().getInteger(R.integer.lazy_load_limitation), FragmentUserReviews.this).execute();
            isLoadingMore=true;
            listFooterView.setVisibility(View.VISIBLE);
        }
    }

    void setUpListView() {
        // SwipeRefreshLayout
        swipeView.setColorScheme(R.color.button_on_dark, R.color.red, R.color.green);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isLoadingMore) {
                    swipeView.setRefreshing(false);
                    return;
                }
                reviews = new ArrayList<Review>();
                new GetUserReviews(LoginInfo.getUserId(getActivity()),
                        0, getResources().getInteger(R.integer.lazy_load_limitation), FragmentUserReviews.this).execute();
                swipeView.setRefreshing(true);
            }
        });
        listFooterView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.INVISIBLE);
        list.addFooterView(listFooterView);
        // ListView LoadMore
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            int currentFirstVisibleItem
                    ,
                    currentVisibleItemCount
                    ,
                    currentScrollState;

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            private void isScrollCompleted() {
                if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {
                    /*** In this way I detect if there's been a scroll which has completed ***/
                    /*** do the work for load more date! ***/
                    if (!swipeView.isRefreshing() && !isLoadingMore
                            && reviews.size()>0 && reviews.size()%getResources().getInteger(R.integer.lazy_load_limitation)==0) {
                        loadMoreData();
                    }
                }
            }
        });
    }

    @Override
    public void getResult(Object result) {
        try {
            pd.dismiss();
            if (result instanceof ArrayList) {
                ArrayList<Review> temp = new ArrayList<>();
                temp.addAll(reviews);
                temp.addAll((ArrayList<Review>) result);
                reviews.clear();
                reviews.addAll(temp);
                mAdapter.notifyDataSetChanged();
                isLoadingMore=false;
                swipeView.setRefreshing(false);
                listFooterView.setVisibility(View.GONE);
            } else if (result instanceof ResultStatus) {
                int reviewPosition = -1;
                for (int i = 0; i < reviews.size(); i++) {
                    if (reviews.get(i).id == editingId) {
                        reviewPosition = i;
                        break;
                    }
                }
                if (reviewPosition > -1) {
                    if (editingText.equals(null)) {
                        reviews.remove(reviewPosition);
                    } else {
                        reviews.get(reviewPosition).text = editingText;
                    }
                    mAdapter.notifyDataSetChanged();
                }
                Dialogs.showMessage(getActivity(), getString(R.string.success), false);
                editingDialog.dismiss();
            }
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        editingId=0;
        try {
            pd.dismiss();
            String errorMessage = ServerAnswer.getError(getActivity(), errorCode);
            Dialogs.showMessage(getActivity(), errorMessage, false);
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    private int editingId;
    private String editingText;
    private Dialog editingDialog;

    @Override
    public void setEditing(int id, String text, Dialog dialog) {
        editingId = id;
        editingText = text;
        editingDialog = dialog;
    }
}
