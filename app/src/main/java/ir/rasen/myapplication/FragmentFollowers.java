package ir.rasen.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.FollowersAdapter;
import ir.rasen.myapplication.adapters.FriendsAdapter;
import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.GridViewHeader;
import ir.rasen.myapplication.ui.TextViewFont;

/**
 * Created by 'Sina KH'.
 */
public class FragmentFollowers extends Fragment {
    private static final String TAG = "FragmentFollowers";

    private View view, listFooterView, listHeaderView;

    private boolean isLoadingMore=false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private ListAdapter mAdapter;

    // business id is received here
    private String businessId;

    public static FragmentFollowers newInstance (String businessId){
        FragmentFollowers fragment = new FragmentFollowers();

        Bundle bundle = new Bundle();
        bundle.putString(Params.BUSINESS_ID, businessId);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentFollowers() {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            businessId = bundle.getString(Params.BUSINESS_ID);
        } else {
            Log.e(TAG, "bundle is null!!");
            if(getActivity()!=null) getActivity().finish();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        this.view = view;

        list = (ListView) view.findViewById(R.id.list_followers_followers);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        // setUp ListView
        setUpListView();

        // TODO: Change Adapter to display your content
        ArrayList<User> followers = new ArrayList<User>();

        /*
            for example, i've made some fake data to show ::
        */
        User User1 = new User();
        User User2 = new User();
        User User3 = new User();
        User1.name=("SINA");
        User2.name=("HASAN");
        User3.name=("HOSSEIN");
        followers.add(User1);
        followers.add(User2);
        followers.add(User3);

        mAdapter = new FollowersAdapter(getActivity(), followers, true);
        ((AdapterView<ListAdapter>) view.findViewById(R.id.list_followers_followers)).setAdapter(mAdapter);

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
