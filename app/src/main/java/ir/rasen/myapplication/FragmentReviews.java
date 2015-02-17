package ir.rasen.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.ReviewsAdapter;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.RateBusiness;
import ir.rasen.myapplication.webservice.review.GetBusinessReviews;
import ir.rasen.myapplication.webservice.review.ReviewBusiness;

/**
 * Created by 'Sina KH' on 1/13/2015.
 */
public class FragmentReviews extends Fragment implements WebserviceResponse, EditInterface {
    private static final String TAG = "FragmentReviews";

    private View view, listFooterView;

    private boolean isLoadingMore=false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private BaseAdapter mAdapter;
    private int businessId, businessOwner;
    ArrayList<Review> reviews;

    Dialog dialog;

    private ProgressDialogCustom pd;

    public static FragmentReviews newInstance (int businessId, int businessOwner) {
        FragmentReviews fragment = new FragmentReviews();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.BUSINESS_ID, businessId);
        bundle.putInt(Params.BUSINESS_OWNER, businessOwner);
        fragment.setArguments(bundle);

        return fragment;
    }

    public FragmentReviews() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pd = new ProgressDialogCustom(getActivity());

        Bundle bundle = getArguments();
        if (bundle != null) {
            businessId = bundle.getInt(Params.BUSINESS_ID);
            businessOwner = bundle.getInt(Params.BUSINESS_OWNER);
            new GetBusinessReviews(businessId,0,
                    getActivity().getResources().getInteger(R.integer.lazy_load_limitation)
                    ,FragmentReviews.this).execute();
            //pd.show();

        } else {
            Log.e(TAG, "bundle is null!!");
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reviews, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            businessId = bundle.getInt(Params.BUSINESS_ID);
        } else {
            Log.e(TAG, "bundle is null!!");
            if (getActivity() != null) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
            }
        }

        list = (ListView) view.findViewById(R.id.list_reviews_review);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        pd = new ProgressDialogCustom(getActivity());

        reviews = new ArrayList<Review>();
        mAdapter = new ReviewsAdapter(getActivity(), reviews,FragmentReviews.this, FragmentReviews.this);
        list.setAdapter(mAdapter);

        if(businessOwner== LoginInfo.getUserId(getActivity())) {
            view.findViewById(R.id.btn_reviews_send).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.btn_reviews_send).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showReviewDialog();
                }
            });
        }

        return view;
    }

    public void sendReview(String review_text, int review_rate) {
        if(review_text != null) {
            new ReviewBusiness(LoginInfo.getUserId(getActivity()),
                    businessId,
                    review_text, review_rate,
                    FragmentReviews.this).execute();
        }
        else
            new RateBusiness(businessId, LoginInfo.getUserId(getActivity()),review_rate,FragmentReviews.this).execute();
    }

    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        if (reviews != null) {
            new GetBusinessReviews(businessId,reviews.get(reviews.size()-1).id,
                    getActivity().getResources().getInteger(R.integer.lazy_load_limitation)
                    ,FragmentReviews.this).execute();
            isLoadingMore = true;
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
                new GetBusinessReviews(businessId,0,
                        getActivity().getResources().getInteger(R.integer.lazy_load_limitation)
                        ,FragmentReviews.this).execute();
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
                    if (!swipeView.isRefreshing() && !isLoadingMore) {
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
                if (isLoadingMore) {
                    ArrayList<Review> temp = new ArrayList<>();
                    temp.addAll(reviews);
                    temp.addAll((ArrayList<Review>) result);
                    reviews.clear();
                    reviews.addAll(temp);
                    mAdapter.notifyDataSetChanged();
                    isLoadingMore=false;
                    swipeView.setRefreshing(false);
                    listFooterView.setVisibility(View.GONE);
                } else {
                    reviews = (ArrayList<Review>) result;
                    mAdapter = new ReviewsAdapter(getActivity(), reviews, FragmentReviews.this, FragmentReviews.this);
                    list.setAdapter(mAdapter);
                    isLoadingMore=false;
                    swipeView.setRefreshing(false);
                    listFooterView.setVisibility(View.GONE);
                }
            } else if (result instanceof ResultStatus) {
                int reviewPosition = -1;
                if(editingId == 0) {
                    // new review submitted
                    Dialogs.showMessage(getActivity(), getString(R.string.success));
                    dialog.dismiss();
                    return;
                }
                // review-> deleted or modified
                for(int i=0; i<reviews.size(); i++) {
                    if (reviews.get(i).id == editingId) {
                        reviewPosition = i;
                        break;
                    }
                }
                if(reviewPosition>-1) {
                    if (editingText.equals(null)) {
                        reviews.remove(reviewPosition);
                    } else {
                        reviews.get(reviewPosition).text=editingText;
                    }
                    mAdapter.notifyDataSetChanged();
                }
                Dialogs.showMessage(getActivity(), getString(R.string.success));
                editingDialog.dismiss();
            }
        } catch(Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        try {
            String errorMessage = ServerAnswer.getError(getActivity(), errorCode);
            Dialogs.showMessage(getActivity(), errorMessage);
            pd.hide();
        } catch(Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    private void showReviewDialog() {
        dialog = new Dialog(getActivity(), R.style.AppTheme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_new_review);
        ((RatingBar) dialog.findViewById(R.id.ratingBar_new_review_rate)).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ((RatingBar) dialog.findViewById(R.id.ratingBar_new_review_rate)).setRating((float) Math.ceil(v));
            }
        });
        dialog.findViewById(R.id.btn_new_review_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String review = ((EditTextFont) dialog.findViewById(R.id.edt_new_review_review)).getText().toString();
                float rate = ((RatingBar) dialog.findViewById(R.id.ratingBar_new_review_rate)).getRating();
                if(rate>0) {
                    editingId = 0;
                    editingText = null;
                    editingDialog = null;
                    sendReview(review,(int)rate);
                } else {
                    Dialogs.showMessage(getActivity(), getString(R.string.rate_needed));
                }
            }
        });
        dialog.findViewById(R.id.btn_new_review_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private int editingId;
    private String editingText;
    private Dialog editingDialog;
    @Override
    public void setEditing(int id, String text, Dialog dialog) {
        pd.show();
        editingId = id;
        editingText = text;
        editingDialog = dialog;
    }
}
