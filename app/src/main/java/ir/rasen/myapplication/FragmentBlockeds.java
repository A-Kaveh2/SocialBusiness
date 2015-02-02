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

import ir.rasen.myapplication.adapters.BlockedsAdapter;
import ir.rasen.myapplication.adapters.FollowersAdapter;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.GetBusinessFollowers;
import ir.rasen.myapplication.webservice.review.GetBusinessReviews;

/**
 * Created by 'Sina KH'.
 */
public class FragmentBlockeds extends Fragment implements WebserviceResponse {
    private static final String TAG = "FragmentBlockeds";

    private View view, listFooterView;

    private boolean isLoadingMore=false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private ListAdapter mAdapter;
    ArrayList<User> blockeds;

    // business id is received here
    private String businessId;

    public static FragmentBlockeds newInstance (String businessId){
        FragmentBlockeds fragment = new FragmentBlockeds();

        Bundle bundle = new Bundle();
        bundle.putString(Params.BUSINESS_ID, businessId);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentBlockeds() {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            businessId = bundle.getString(Params.BUSINESS_ID);
        } else {
            Log.e(TAG, "bundle is null!!");
            if(getActivity()!=null){
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetBusinessFollowers(1,FragmentBlockeds.this).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blockeds, container, false);
        this.view = view;

        list = (ListView) view.findViewById(R.id.list_blockeds_blockeds);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        // setUp ListView
        setUpListView();

        // TODO: Change Adapter to display your content
        blockeds = new ArrayList<User>();

        /*
            for example, i've made some fake data to show ::
        */
        User User1 = new User();
        User User2 = new User();
        User User3 = new User();
        User1.name=("SINA");
        User2.name=("HASAN");
        User3.name=("HOSSEIN");
        blockeds.add(User1);
        blockeds.add(User2);
        blockeds.add(User3);

        mAdapter = new BlockedsAdapter(getActivity(), blockeds);
        ((AdapterView<ListAdapter>) view.findViewById(R.id.list_blockeds_blockeds)).setAdapter(mAdapter);

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
                blockeds = new ArrayList<User>();
                // TODO get blockeds again
                swipeView.setRefreshing(true);
            }
        });
        listFooterView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.GONE);
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
        if(result instanceof ArrayList){
            //result from executing getBusinessFollowers
            ArrayList<SearchItemUserBusiness> businessesFollowers = (ArrayList<SearchItemUserBusiness>)result;
            ArrayList<User> blockeds = new ArrayList<User>();
            User user = null;
            for(SearchItemUserBusiness item:businessesFollowers){
                user = new User();
                user.userName = item.username;
                user.profilePicture = item.picture;
                blockeds.add(user);
            }
            mAdapter = new BlockedsAdapter(getActivity(), blockeds);
            ((AdapterView<ListAdapter>) view.findViewById(R.id.list_blockeds_blockeds)).setAdapter(mAdapter);
            isLoadingMore=false;
            swipeView.setRefreshing(false);
            listFooterView.setVisibility(View.GONE);

            //TODO use followers to intiate listview

        }
    }

    @Override
    public void getError(Integer errorCode) {

    }
}
