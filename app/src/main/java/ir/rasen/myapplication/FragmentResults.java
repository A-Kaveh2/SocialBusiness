package ir.rasen.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import ir.rasen.myapplication.adapters.BusinessesAdapterResult;
import ir.rasen.myapplication.adapters.PostsGridAdapterResult;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.Location_M;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemPost;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.search.SearchBusinessesLocation;
import ir.rasen.myapplication.webservice.search.SearchPost;

/**
 * Created by 'Sina KH' on 1/16/2015.
 */
public class FragmentResults extends Fragment implements WebserviceResponse {
    private static final String TAG = "FragmentResults";

    private View view, listFooterView;

    private boolean isLoadingMore = false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private ListAdapter mAdapter;
    private int searchType;
    private ArrayList<SearchItemUserBusiness> searchResult;

    private WebserviceResponse webserviceResponse;

    String searchString, location_latitude, location_longitude;
    Context context;
    int subcategoryId;

    public static FragmentResults newInstance(String searchString,  int sucategoryId
            , Location_M location_m, int searchType) {

        FragmentResults fragment = new FragmentResults();

        Bundle bundle = new Bundle();
        bundle.putString(Params.SEARCH_TEXT, searchString);
        bundle.putInt(Params.SUB_CATEGORY_ID,sucategoryId);
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

        webserviceResponse = this;
        context = getActivity();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            searchString = bundle.getString(Params.SEARCH_TEXT);
            subcategoryId = bundle.getInt(Params.SUB_CATEGORY_ID);
            location_latitude = bundle.getString(Params.LOCATION_LATITUDE);
            location_longitude = bundle.getString(Params.LOCATION_LONGITUDE);
            searchType = bundle.getInt(Params.SEARCH_TYPE);
        } else {
            Log.e(TAG, "bundle is null!!");
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
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

        // load results
        if (searchType == Params.SearchType.PRODUCTS) {
            new SearchPost( searchString, FragmentResults.this).execute();
        } else {
            new SearchBusinessesLocation(LoginInfo.getUserId(context), searchString,subcategoryId, location_latitude, location_longitude,0,getResources().getInteger(R.integer.lazy_load_limitation), FragmentResults.this).execute();
        }

        return view;
    }

    // TODO: LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        isLoadingMore = true;
        listFooterView.setVisibility(View.VISIBLE);
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
                searchResult = new ArrayList<SearchItemUserBusiness>();
                // TODO get results again
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
                if (searchType == Params.SearchType.PRODUCTS) {

                    //TODO search post reutrns a list of SearchItemPost object
                    //TODO modify code to display the list
                    ArrayList<SearchItemPost> searchPostResult = new ArrayList<>();
                    searchPostResult = (ArrayList<SearchItemPost>)result;
                    //TODO use searchPostResult


                    searchResult = new ArrayList<SearchItemUserBusiness>();
                    searchResult = (ArrayList<SearchItemUserBusiness>) result;


                    //TODO check the code. FORCE CLOSE
                    /*mAdapter = new PostsGridAdapterResult(getActivity(), searchResult);
                    list.setAdapter(mAdapter);
                    isLoadingMore=false;
                    swipeView.setRefreshing(false);
                    listFooterView.setVisibility(View.GONE);*/

                } else {
                    searchResult = new ArrayList<SearchItemUserBusiness>();
                    searchResult = (ArrayList<SearchItemUserBusiness>) result;

                    mAdapter = new BusinessesAdapterResult(getActivity(), searchResult);
                    list.setAdapter(mAdapter);
                    isLoadingMore=false;
                    swipeView.setRefreshing(false);
                    listFooterView.setVisibility(View.GONE);
                }
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
}
