package ir.rasen.myapplication;

import android.app.Dialog;
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
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.friend.GetUserFriends;

/**
 * Created by 'Sina KH'.
 */
public class FragmentFriends extends Fragment implements WebserviceResponse, EditInterface {
    private static final String TAG = "FragmentFriends";

    private View view, listFooterView, listHeaderView;

    private boolean isLoadingMore = false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private ListAdapter mAdapter;

    // user id is received here
    private int userId;

    private ArrayList<User> friends;

    public static FragmentFriends newInstance(int userId) {
        FragmentFriends fragment = new FragmentFriends();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.USER_ID, userId);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentFriends() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getInt(Params.USER_ID);

            new GetUserFriends(userId, FragmentFriends.this).execute();

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

        friends = new ArrayList<User>();
        mAdapter = new FriendsAdapter(getActivity(), friends, true, FragmentFriends.this);
        ((AdapterView<ListAdapter>) view.findViewById(R.id.list_friends_friends)).setAdapter(mAdapter);

        // TODO:: check if new requests received or not
        if (true) { // new requests received or not!
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
        listHeaderView = (View) getActivity().getLayoutInflater().inflate(R.layout.layout_friends_requests, null);
        list.addHeaderView(listHeaderView);
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

    void showFriendRequests() {
        InnerFragment innerFragment = new InnerFragment(getActivity());
        innerFragment.newRequestsFragment(userId);
    }

    @Override
    public void getResult(Object result) {
        try {
            if (result instanceof ArrayList) {
                ArrayList<SearchItemUserBusiness> usersFriends = new ArrayList<SearchItemUserBusiness>();
                usersFriends = (ArrayList<SearchItemUserBusiness>) result;
                friends = new ArrayList<User>();
                User user = null;
                for (SearchItemUserBusiness item : usersFriends) {
                    user = new User();
                    user.userName = item.username;
                    user.profilePicture = item.picture;
                    friends.add(user);
                }

                friends = new ArrayList<User>();
                mAdapter = new FriendsAdapter(getActivity(), friends, true, FragmentFriends.this);
                ((AdapterView<ListAdapter>) view.findViewById(R.id.list_friends_friends)).setAdapter(mAdapter);
                isLoadingMore=false;
                swipeView.setRefreshing(false);
                listFooterView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
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

    private int editingId;
    private String editingText;
    private Dialog editingDialog;
    @Override
    public void setEditing(int id, String text, Dialog dialog) {
        editingId = id;
        editingText = text;
        editingDialog = dialog;
    }
}
