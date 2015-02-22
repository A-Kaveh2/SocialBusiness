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

import ir.rasen.myapplication.adapters.FollowersAdapter;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.GetBusinessFollowers;

/**
 * Created by 'Sina KH'.
 */
public class FragmentFollowers extends Fragment implements WebserviceResponse, EditInterface {
    private static final String TAG = "FragmentFollowers";

    private View view, listFooterView, listHeaderView;

    private boolean isLoadingMore = false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private ListAdapter mAdapter;

    // business id is received here
    private int businessId;
    private boolean businessOwner;

    ArrayList<User> followers;

    private ProgressDialogCustom pd;

    public static FragmentFollowers newInstance(int businessId, boolean businessOwner) {
        FragmentFollowers fragment = new FragmentFollowers();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.BUSINESS_ID, businessId);
        bundle.putBoolean(Params.BUSINESS_OWNER, businessOwner);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentFollowers() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            businessId = bundle.getInt(Params.BUSINESS_ID);
            businessOwner = bundle.getBoolean(Params.BUSINESS_OWNER);
        } else {
            Log.e(TAG, "bundle is null!!");
            if (getActivity() != null) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
            }
        }
        pd = new ProgressDialogCustom(getActivity());

        new GetBusinessFollowers(businessId,FragmentFollowers.this).execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        this.view = view;

        list = (ListView) view.findViewById(R.id.list_followers_followers);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        pd = new ProgressDialogCustom(getActivity());

        // setUp ListView
        setUpListView();

        followers = new ArrayList<User>();
        mAdapter = new FollowersAdapter(getActivity(), followers, true, FragmentFollowers.this);
        ((AdapterView<ListAdapter>) view.findViewById(R.id.list_followers_followers)).setAdapter(mAdapter);

        return view;
    }

    // TODO: LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        isLoadingMore = true;
        listFooterView.setVisibility(View.VISIBLE);
    }

    void setUpListView() {
        // manage blockeds header::
        if(businessOwner) {
            listHeaderView = getActivity().getLayoutInflater().inflate(R.layout.layout_followers_blockeds, null);
            list.addHeaderView(listHeaderView);
            listHeaderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getActivity());
                    innerFragment.newBlockedsFragment(businessId);
                }
            });
            listHeaderView.setVisibility(View.VISIBLE);
        }
        // SwipeRefreshLayout
        swipeView.setColorScheme(R.color.button_on_dark, R.color.red, R.color.green);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isLoadingMore) {
                    swipeView.setRefreshing(false);
                    return;
                }
                followers = new ArrayList<User>();
                new GetBusinessFollowers(businessId,FragmentFollowers.this).execute();
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
                    if (!swipeView.isRefreshing() && !isLoadingMore && followers.size()>0) {
                        loadMoreData();
                    }
                }
            }
        });
    }

    @Override
    public void getResult(Object result) {
        try {
            pd.dismiss();
            if (result instanceof ArrayList) {
                //result from executing getBusinessFollowers
                ArrayList<SearchItemUserBusiness> businessesFollowers = (ArrayList<SearchItemUserBusiness>) result;
                followers = new ArrayList<User>();
                User user = null;
                for (SearchItemUserBusiness item : businessesFollowers) {
                    user = new User();
                    user.id = item.id;
                    user.userName = item.username;
                    user.profilePictureId = item.pictureId;
                    followers.add(user);
                }
                mAdapter = new FollowersAdapter(getActivity(), followers, true, FragmentFollowers.this);
                ((AdapterView<ListAdapter>) view.findViewById(R.id.list_followers_followers)).setAdapter(mAdapter);
                isLoadingMore = false;
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
            pd.dismiss();
            String errorMessage = ServerAnswer.getError(getActivity(), errorCode);
            Dialogs.showMessage(getActivity(), errorMessage);
        } catch (Exception e) {
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
