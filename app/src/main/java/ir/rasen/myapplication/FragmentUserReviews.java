package ir.rasen.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.ReviewsAdapter;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.review.GetUserReviews;

/**
 * Created by 'Sina KH' on 1/13/2015.
 */
public class FragmentUserReviews extends Fragment implements WebserviceResponse {
    private static final String TAG = "FragmentReviews";

    private View view, listFooterView;

    private boolean isLoadingMore = false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private ListAdapter mAdapter;

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

        new GetUserReviews(LoginInfo.getUserId(getActivity()),
                0, getResources().getInteger(R.integer.lazy_load_limitation)).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_reviews, container, false);
        this.view = view;

        list = (ListView) view.findViewById(R.id.list_user_reviews_review);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        // TODO: Change Adapter to display your content
        ArrayList<Review> reviews = new ArrayList<Review>();

        /*
            for example, i've made some fake data to show ::
        */

        mAdapter = new ReviewsAdapter(getActivity(), reviews, FragmentUserReviews.this);
        ((AdapterView<ListAdapter>) view.findViewById(R.id.list_user_reviews_review)).setAdapter(mAdapter);

        return view;
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
                isLoadingMore = false;
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
        if (result instanceof ArrayList) {
            ArrayList<Review> reviews = new ArrayList<Review>();
            reviews = (ArrayList<Review>) result;

            //TODO assign reviews
        }
    }

    @Override
    public void getError(Integer errorCode) {

    }
}
