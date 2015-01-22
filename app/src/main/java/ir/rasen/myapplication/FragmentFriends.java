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

import ir.rasen.myapplication.adapters.FriendsAdapter;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.TextViewFont;

/**
 * Created by 'Sina KH'.
 */
public class FragmentFriends extends Fragment {
    private static final String TAG = "FragmentFriends";

    private View view, listFooterView, listHeaderView;

    private boolean isLoadingMore=false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private ListAdapter mAdapter;

    // user id is received here
    private String userId;

    public static FragmentFriends newInstance (String userId){
        FragmentFriends fragment = new FragmentFriends();

        Bundle bundle = new Bundle();
        bundle.putString(Params.USER_ID, userId);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentFriends() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString(Params.USER_ID);
        } else {
            Log.e(TAG, "bundle is null!!");
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        this.view = view;

        list = (ListView) view.findViewById(R.id.list_friends_friends);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        // setUp ListView
        setUpListView();

        // TODO: Change Adapter to display your content
        ArrayList<User> friends = new ArrayList<User>();

        /*
            for example, i've made some fake data to show ::
        */
        User User1 = new User();
        User User2 = new User();
        User User3 = new User();
        User1.name=("SINA");
        User2.name=("HASAN");
        User3.name=("HOSSEIN");
        friends.add(User1);
        friends.add(User2);
        friends.add(User3);

        mAdapter = new FriendsAdapter(getActivity(), friends, true);
        ((AdapterView<ListAdapter>) view.findViewById(R.id.list_friends_friends)).setAdapter(mAdapter);

        // TODO:: check if new requests received or not
        if(true) { // new requests received or not!
            int requestsNum = 2; // number of new requests
            listHeaderView.setVisibility(View.VISIBLE);
            ((TextViewFont) listHeaderView.findViewById(R.id.txt_friends_requests)).setText(
                    requestsNum + " " + getString(R.string.friend_request));
            listHeaderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFriendRequests();
                }
            });
        } else {
            listHeaderView.setVisibility(View.GONE);
        }
        return view;
    }

    // TODO: LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        listFooterView.setVisibility(View.VISIBLE);
    }

    void setUpListView() {
        // add requests header::
        listHeaderView = (View)getActivity().getLayoutInflater().inflate(R.layout.layout_friends_requests,null);
        list.addHeaderView(listHeaderView);
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

    void showFriendRequests() {
        InnerFragment innerFragment = new InnerFragment(getActivity());
        innerFragment.newRequestsFragment(userId);
    }
}
