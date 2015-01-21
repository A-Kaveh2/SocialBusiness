package ir.rasen.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.PostsAdapter;
import ir.rasen.myapplication.adapters.ReviewsAdapter;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.helper.Location_M;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.EditTextFont;

/**
 * Created by 'Sina KH' on 1/16/2015.
 */
public class FragmentResults extends Fragment {
    private static final String TAG = "FragmentResults";

    private View view, listFooterView;

    private boolean isLoadingMore=false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private ListAdapter mAdapter;

    String searchString, category, location_latitude, location_longitude;
    Boolean nearby;

    public static FragmentResults newInstance (String searchString, String category, boolean nearby, Location_M location_m){
        FragmentResults fragment = new FragmentResults();

        Bundle bundle = new Bundle();
        bundle.putString(Params.SEARCH_TEXT, searchString);
        bundle.putString(Params.CATEGORY, category);
        bundle.putBoolean(Params.NEAR_BY, nearby);
        bundle.putString(Params.LOCATION_LATITUDE, location_m.getLatitude());
        bundle.putString(Params.LOCATION_LONGITUDE, location_m.getLongitude());
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentResults() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            searchString = bundle.getString(Params.SEARCH_TEXT);
            category = bundle.getString(Params.CATEGORY);
            nearby = bundle.getBoolean(Params.NEAR_BY);
            location_latitude = bundle.getString(Params.LOCATION_LATITUDE);
            location_longitude = bundle.getString(Params.LOCATION_LONGITUDE);
        } else {
            Log.e(TAG, "bundle is null!!");
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        this.view = view;

        list = (ListView) view.findViewById(R.id.list_results_results);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        // setUp ListView
        setUpListView();

        // TODO: Change Adapter to display your content after loading data

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
