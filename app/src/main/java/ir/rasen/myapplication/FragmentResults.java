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

import ir.rasen.myapplication.adapters.BusinessesAdapter;
import ir.rasen.myapplication.adapters.BusinessesAdapterResult;
import ir.rasen.myapplication.adapters.HomePostsAdapter;
import ir.rasen.myapplication.adapters.PostsAdapter;
import ir.rasen.myapplication.adapters.ReviewsAdapter;
import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.helper.Location_M;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.EditTextFont;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by 'Sina KH' on 1/16/2015.
 */
public class FragmentResults extends Fragment {
    private static final String TAG = "FragmentResults";

    private View view, listFooterView;

    private boolean isLoadingMore=false;
    private SwipeRefreshLayout swipeView;
    private StickyListHeadersListView list;
    private StickyListHeadersAdapter mAdapter;
    private int searchType;

    String searchString, category, location_latitude, location_longitude;
    Boolean nearby;

    public static FragmentResults newInstance (String searchString, String category, boolean nearby
            , Location_M location_m, int searchType){
        FragmentResults fragment = new FragmentResults();

        Bundle bundle = new Bundle();
        bundle.putString(Params.SEARCH_TEXT, searchString);
        bundle.putString(Params.CATEGORY, category);
        bundle.putBoolean(Params.NEAR_BY, nearby);
        bundle.putString(Params.LOCATION_LATITUDE, location_m.getLatitude());
        bundle.putString(Params.LOCATION_LONGITUDE, location_m.getLongitude());
        bundle.putInt(Params.SEARCH_TYPE, searchType);
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
            searchType = bundle.getInt(Params.SEARCH_TYPE);
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

        list = (StickyListHeadersListView) view.findViewById(R.id.list_results_results);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        // setUp ListView
        setUpListView();

        // TODO: Change Adapter to display your content after loading data
        if(searchType== Params.SearchType.PRODUCTS) {
            // ArrayList to show
            ArrayList<Post> posts = new ArrayList<Post>();
            /*
                for example, i've made some fake data to show ::
            */
            Post post1 = new Post();
            Post post2 = new Post();
            Post post3 = new Post();
            post1.businessID = "راسن";
            post1.description = "یک نرم افزار عالی!!";
            post1.price = "100.000";
            post1.code = "30";
            ArrayList<Comment> lastThreeComments = new ArrayList<>();
            Comment comment = new Comment();
            comment.userID = "SINA";
            comment.text = "سلام";
            lastThreeComments.add(comment);
            post1.lastThreeComments = lastThreeComments;
            post1.title = "عنوان!!";
            posts.add(post1);
            post2.businessID = "sina";
            post2.description = "programmer - RASEN CO.";
            post2.price = "123.456";
            post2.code = "30";
            post2.title = "عنوان!!";
            post2.lastThreeComments = lastThreeComments;
            posts.add(post2);
            post3.businessID = "sina";
            post3.description = "progrsafasfasfasfafafasfasd\n\nammer - RASEN CO.";
            post3.price = "125.234";
            post3.code = "30";
            post3.title = "عنوان!!";
            post3.lastThreeComments = lastThreeComments;
            posts.add(post3);
            mAdapter = new HomePostsAdapter(getActivity(), posts);
            list.setAdapter(mAdapter);
        } else {
            ArrayList<Business> businesses = new ArrayList<Business>();
            /*
                for example, i've made some fake data to show ::
            */
            Business b1 = new Business();
            Business b2 = new Business();
            Business b3 = new Business();
            b1.name=("RASEN");
            b2.name=("IRAN AIR");
            b3.name=("کبابی محل");
            businesses.add(b1);
            businesses.add(b2);
            businesses.add(b3);
            mAdapter = new BusinessesAdapterResult(getActivity(), businesses);
            list.setAdapter(mAdapter);
        }

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
