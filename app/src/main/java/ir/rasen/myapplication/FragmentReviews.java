package ir.rasen.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.CommentsAdapter;
import ir.rasen.myapplication.adapters.ReviewsAdapter;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.review.GetBusinessReviews;
import ir.rasen.myapplication.webservice.review.ReviewBusiness;

/**
 * Created by 'Sina KH' on 1/13/2015.
 */
public class FragmentReviews extends Fragment implements WebserviceResponse {
    private static final String TAG = "FragmentReviews";

    private View view, listFooterView;

    private boolean isLoadingMore=false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private BaseAdapter mAdapter;
    private String businessId;
    ArrayList<Review> reviews;

    public static FragmentReviews newInstance (String businessId) {
        FragmentReviews fragment = new FragmentReviews();

        Bundle bundle = new Bundle();
        bundle.putString(Params.BUSINESS_ID, businessId);
        fragment.setArguments(bundle);

        return fragment;
    }

    public FragmentReviews() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new GetBusinessReviews(businessId,0,
                getActivity().getResources().getInteger(R.integer.lazy_load_limitation)
                ,FragmentReviews.this).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reviews, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            businessId = bundle.getString(Params.BUSINESS_ID);
        } else {
            Log.e(TAG, "bundle is null!!");
            if (getActivity() != null) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
            }
        }

        list = (ListView) view.findViewById(R.id.list_reviews_review);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        ArrayList<Review> reviews = new ArrayList<Review>();
        mAdapter = new ReviewsAdapter(getActivity(), reviews,FragmentReviews.this);
        list.setAdapter(mAdapter);

        view.findViewById(R.id.btn_reviews_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReviewDialog();
            }
        });

        return view;
    }

    public void sendReview(String review_text, float review_rate) {
        new ReviewBusiness(LoginInfo.getUserId(getActivity()),
                businessId,
                review_text,
                FragmentReviews.this).execute();
        // TODO:: SEND RATE
    }

    // TODO: LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        if (reviews != null) {
            new GetBusinessReviews(businessId,Integer.parseInt(reviews.get(reviews.size()-1).id),
                    getActivity().getResources().getInteger(R.integer.lazy_load_limitation)
                    ,FragmentReviews.this).execute();
            listFooterView.setVisibility(View.VISIBLE);
        }
    }

    void setUpListView() {
        // SwipeRefreshLayout
        swipeView.setColorScheme(R.color.button_on_dark, R.color.red, R.color.green);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isLoadingMore) {
                    swipeView.setRefreshing(false);
                    return;
                }
                swipeView.setRefreshing(true);
            }
        });
        listFooterView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.INVISIBLE);
        list.addFooterView(listFooterView);
        // TODO: ListView LoadMore
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
                        isLoadingMore = true;
                        loadMoreData();
                    }
                }
            }
        });
    }

    @Override
    public void getResult(Object result) {
        try {
            if (result instanceof ArrayList) {
                if (isLoadingMore) {
                    ArrayList<Review> loadedReviews = new ArrayList<>();
                    for (Review item : loadedReviews) {
                        reviews.add(item);
                    }
                    mAdapter.notifyDataSetChanged();
                    isLoadingMore=false;
                    listFooterView.setVisibility(View.INVISIBLE);
                } else {
                    reviews = (ArrayList<Review>) result;
                    mAdapter = new ReviewsAdapter(getActivity(), reviews, FragmentReviews.this);
                    list.setAdapter(mAdapter);
                }
            } else if (result instanceof ResultStatus) {
                Dialogs.showMessage(getActivity(), getString(R.string.success));
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
        } catch(Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    private void showReviewDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.AppTheme_Dialog);
        dialog.findViewById(R.id.btn_new_review_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String review = ((TextViewFont) dialog.findViewById(R.id.txt_new_review_review)).getText().toString();
                float rate = ((RatingBar) dialog.findViewById(R.id.ratingBar_new_review_rate)).getRating();
                if(rate>0) {
                    sendReview(review, rate);
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
}
