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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.FriendsAdapter;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.DownloadImages;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.announcement.GetAllCommentNotifications;
import ir.rasen.myapplication.webservice.announcement.GetLastCommentNotification;
import ir.rasen.myapplication.webservice.business.BlockUser;
import ir.rasen.myapplication.webservice.business.DeleteComment;
import ir.rasen.myapplication.webservice.business.GetBlockedUsers;
import ir.rasen.myapplication.webservice.business.RateBusiness;
import ir.rasen.myapplication.webservice.business.UnblockUser;
import ir.rasen.myapplication.webservice.comment.GetPostAllComments;
import ir.rasen.myapplication.webservice.comment.SendComment;
import ir.rasen.myapplication.webservice.comment.UpdateComment;
import ir.rasen.myapplication.webservice.friend.AnswerRequestFriendship;
import ir.rasen.myapplication.webservice.friend.GetUserFriends;
import ir.rasen.myapplication.webservice.friend.RequestFriendship;
import ir.rasen.myapplication.webservice.post.GetBusinessPosts;
import ir.rasen.myapplication.webservice.post.GetPost;
import ir.rasen.myapplication.webservice.post.GetSharedPosts;
import ir.rasen.myapplication.webservice.post.GetTimeLinePosts;
import ir.rasen.myapplication.webservice.post.Like;
import ir.rasen.myapplication.webservice.post.Report;
import ir.rasen.myapplication.webservice.post.Unlike;
import ir.rasen.myapplication.webservice.user.ForgetPassword;

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
    private int userId, userNewRequests;

    private ArrayList<User> friends;

    private ProgressDialogCustom pd;

    public static FragmentFriends newInstance(int userId, int newRequests) {
        FragmentFriends fragment = new FragmentFriends();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.USER_ID, userId);
        bundle.putInt(Params.USER_FRIEND_REQUESTS, newRequests);
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
            userNewRequests = bundle.getInt(Params.USER_FRIEND_REQUESTS);

            new GetUserFriends(userId, FragmentFriends.this).execute();



            //new GetBusinessPosts(3,2,0,20,FragmentFriends.this).execute();
            //new GetPost(3,6,FragmentFriends.this).execute();
            //new GetSharedPosts(1,0,20,FragmentFriends.this).execute();
            //new GetTimeLinePosts(3,0,20,FragmentFriends.this).execute();

            //new GetLastCommentNotification(getActivity(),3,FragmentFriends.this).execute();
            //new GetAllCommentNotifications(2,0,20,FragmentFriends.this).execute();


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

        pd = new ProgressDialogCustom(getActivity());

        list = (ListView) view.findViewById(R.id.list_friends_friends);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        pd.show();

        // setUp ListView
        setUpListView();

        friends = new ArrayList<User>();
        mAdapter = new FriendsAdapter(getActivity(), friends, true, FragmentFriends.this);
        ((AdapterView<ListAdapter>) view.findViewById(R.id.list_friends_friends)).setAdapter(mAdapter);

        // check if new requests received or not
        if (userNewRequests>0) { // new requests received or not!
            int requestsNum = userNewRequests;
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
        isLoadingMore = true;
        listFooterView.setVisibility(View.VISIBLE);
    }

    void setUpListView() {
        // add requests header::
        listHeaderView = getActivity().getLayoutInflater().inflate(R.layout.layout_friends_requests, null);
        list.addHeaderView(listHeaderView);
        // SwipeRefreshLayout
        swipeView.setColorScheme(R.color.button_on_dark, R.color.red, R.color.green);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isLoadingMore) {
                    swipeView.setRefreshing(false);
                    return;
                }
                friends = new ArrayList<User>();
                new GetUserFriends(userId, FragmentFriends.this).execute();
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
                    if (!swipeView.isRefreshing() && !isLoadingMore
                            && friends.size()>0 && friends.size()%getResources().getInteger(R.integer.lazy_load_limitation)==0) {
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
        pd.dismiss();
        try {
            if (result instanceof ArrayList) {
                ArrayList<SearchItemUserBusiness> usersFriends = (ArrayList<SearchItemUserBusiness>) result;
                ArrayList<User> temp = friends;
                User user = null;
                for (SearchItemUserBusiness item : usersFriends) {
                    user = new User();
                    user.userName = item.username;
                    user.profilePictureId = item.pictureId;
                    temp.add(user);
                }

                friends.clear();
                friends.addAll(temp);
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
        pd.dismiss();
        try {
            String errorMessage = ServerAnswer.getError(getActivity(), errorCode);
            Dialogs.showMessage(getActivity(), errorMessage, friends.size()==0 ? true : false);
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
