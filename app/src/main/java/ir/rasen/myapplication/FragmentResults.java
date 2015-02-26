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

import com.google.android.gms.internal.id;

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
import ir.rasen.myapplication.ui.ProgressDialogCustom;
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
    private ArrayList<SearchItemUserBusiness> searchResultUserBusinessTemp;
    private ArrayList<SearchItemUserBusiness> searchResultUserBusiness;

    private ArrayList<SearchItemPost> searchResultPostTemp;
    private ArrayList<SearchItemPost> searchResultPost;

    private WebserviceResponse webserviceResponse;

    private ProgressDialogCustom pd;

    String searchString, location_latitude, location_longitude;
    Context context;
    int subcategoryId;

    public static FragmentResults newInstance(String searchString, int sucategoryId
            , Location_M location_m, int searchType) {

        FragmentResults fragment = new FragmentResults();

        Bundle bundle = new Bundle();
        bundle.putString(Params.SEARCH_TEXT, searchString);
        bundle.putInt(Params.SUB_CATEGORY_ID, sucategoryId);
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
        pd = new ProgressDialogCustom(getActivity());

        list = (ListView) view.findViewById(R.id.list_results_results);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        searchResultUserBusiness = new ArrayList<>();
        searchResultPost = new ArrayList<>();

        // setUp ListView
        setUpListView();

        pd.show();
        loadResults(0);

        return view;
    }

    public void loadMoreData() {
        isLoadingMore = true;
        listFooterView.setVisibility(View.VISIBLE);
        // load results
        if (searchType == Params.SearchType.PRODUCTS) {
            int lastId;
            if (searchResultPost.size() > 0)
                lastId = searchResultPost.get(searchResultPost.size() - 1).postId;
            else
                lastId = 0;
            new SearchPost(searchString, lastId
                    , getResources().getInteger(R.integer.lazy_load_limitation), FragmentResults.this).execute();
        } else {
            int lastId;
            if (searchResultUserBusiness.size() > 0)
                lastId = searchResultUserBusiness.get(searchResultUserBusiness.size() - 1).id;
            else
                lastId = 0;
            new SearchBusinessesLocation(LoginInfo.getUserId(context), searchString, subcategoryId, location_latitude, location_longitude
                    , lastId
                    , getResources().getInteger(R.integer.lazy_load_limitation), FragmentResults.this).execute();
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
                if (searchType == Params.SearchType.PRODUCTS) {
                    searchResultPost = new ArrayList<>();
                } else {
                    searchResultUserBusiness = new ArrayList<>();
                }
                loadMoreData();
                swipeView.setRefreshing(true);
            }
        });
        listFooterView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.INVISIBLE);
        list.addFooterView(listFooterView);

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
                            && (searchResultPost.size() > 0 && searchResultPost.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0)
                            || (searchResultUserBusiness.size() > 0 && searchResultUserBusiness.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0)) {
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
                pd.dismiss();
                if (searchType == Params.SearchType.PRODUCTS) {

                    searchResultPostTemp = new ArrayList<>();
                    searchResultPostTemp = (ArrayList<SearchItemPost>) result;

                    searchResultPost.addAll(searchResultPostTemp);
                    if (searchResultPost.size() > 0) {
                        mAdapter = new PostsGridAdapterResult(getActivity(), searchResultPost);
                        list.setAdapter(mAdapter);
                        isLoadingMore = false;
                        swipeView.setRefreshing(false);
                        listFooterView.setVisibility(View.GONE);
                    } else {
                        Dialogs.showMessage(getActivity(), getString(R.string.no_results_found), true);
                    }
                } else {
                    searchResultUserBusinessTemp = (ArrayList<SearchItemUserBusiness>) result;

                    searchResultUserBusiness.addAll(searchResultUserBusinessTemp);

                    if(searchResultUserBusiness.size()>0) {

                        mAdapter = new BusinessesAdapterResult(getActivity(), searchResultUserBusiness);
                        list.setAdapter(mAdapter);
                        isLoadingMore = false;
                        swipeView.setRefreshing(false);
                        listFooterView.setVisibility(View.GONE);
                    } else {
                        Dialogs.showMessage(getActivity(), getString(R.string.no_results_found), true);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        try {
            String errorMessage = ServerAnswer.getError(getActivity(), errorCode);
            Dialogs.showMessage(getActivity(), errorMessage, searchResultPost.size() == 0 ? true : false);
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    private void loadResults(int beforeThisId) {
        // load results
        if (searchType == Params.SearchType.PRODUCTS) {
            new SearchPost(searchString, beforeThisId, getResources().getInteger(R.integer.lazy_load_limitation), FragmentResults.this).execute();
        } else {
            new SearchBusinessesLocation(LoginInfo.getUserId(context), searchString, subcategoryId, location_latitude, location_longitude, beforeThisId, getResources().getInteger(R.integer.lazy_load_limitation), FragmentResults.this).execute();
        }
    }
}
