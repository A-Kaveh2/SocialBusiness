package ir.rasen.myapplication;

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

import ir.rasen.myapplication.adapters.FriendsAdapter;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Params;

/**
 * Created by 'Sina KH' on '01/22/2015'.
 */
public class FragmentResultsUsers extends Fragment {
    private static final String TAG = "FragmentFriends";

    private View view, listFooterView, listHeaderView;

    private boolean isLoadingMore=false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private ListAdapter mAdapter;

    // user id is received here
    private String searchString;

    public static FragmentResultsUsers newInstance (String searchString){
        FragmentResultsUsers fragment = new FragmentResultsUsers();

        Bundle bundle = new Bundle();
        bundle.putString(Params.SEARCH_TEXT, searchString);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentResultsUsers() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            searchString = bundle.getString(Params.SEARCH_TEXT);
        } else {
            Log.e(TAG, "bundle is null!!");
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results_users, container, false);
        this.view = view;

        list = (ListView) view.findViewById(R.id.list_results_users_results);
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
        list.setAdapter(mAdapter);

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
