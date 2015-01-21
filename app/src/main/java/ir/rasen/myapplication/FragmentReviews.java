package ir.rasen.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.CommentsAdapter;
import ir.rasen.myapplication.adapters.ReviewsAdapter;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.ui.EditTextFont;

/**
 * Created by 'Sina KH' on 1/13/2015.
 */
public class FragmentReviews extends Fragment {
    private static final String TAG = "FragmentReviews";

    private View view, listFooterView;

    private boolean isLoadingMore=false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private ListAdapter mAdapter;

    public static FragmentReviews newInstance (){
        FragmentReviews fragment = new FragmentReviews();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentReviews() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reviews, container, false);

        list = (ListView) view.findViewById(R.id.list_reviews_review);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        // TODO: Change Adapter to display your content
        ArrayList<Review> reviews = new ArrayList<Review>();

        /*
            for example, i've made some fake data to show ::
        */
        Review review = new Review();
        review.businessID="RASEN";
        review.text="کسب و کار خوبیه, خوشم اومد!";
        review.userID="SINA";

        reviews.add(review);

        mAdapter = new ReviewsAdapter(getActivity(), reviews);
        list.setAdapter(mAdapter);
        view.findViewById(R.id.btn_reviews_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReview(view);
            }
        });

        ((EditTextFont) view.findViewById(R.id.btn_reviews_send)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                if(!view.isFocused()) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(((EditTextFont) view.findViewById(R.id.btn_reviews_send)).getWindowToken(), 0);
                }
            }
        });

        return view;
    }

    public void sendReview(View view) {
        // TODO: SEND REVIEW HERE
        String reviewText = ((EditTextFont) view.findViewById(R.id.txt_reviews_review)).getText().toString();
    }

    // TODO: LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        listFooterView.setVisibility(View.VISIBLE);
    }

    void setUpListView() {
        // SwipeRefreshLayout
        swipeView.setColorScheme(R.color.button_on_dark, R.color.red, R.color.green);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                // TODO: CANCEL LOADING MORE AND REFRESH HERE...
                listFooterView.setVisibility(View.INVISIBLE);
                isLoadingMore=false;
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
}
