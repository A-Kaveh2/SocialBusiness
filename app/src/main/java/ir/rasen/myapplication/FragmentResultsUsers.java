package ir.rasen.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import ir.rasen.myapplication.adapters.UserResultsAdapter;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.search.SearchUser;

/**
 * Created by 'Sina KH' on '01/22/2015'.
 */
public class FragmentResultsUsers extends Fragment implements WebserviceResponse, EditInterface {
    private static final String TAG = "FragmentResultsUsers";

    private View view, listFooterView, listHeaderView;

    private boolean isLoadingMore = false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private ListAdapter mAdapter;

    // user id is received here
    private String searchString;
    private ArrayList<User> users;

    private ProgressDialog pd;

    public static FragmentResultsUsers newInstance(String searchString) {
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
    public FragmentResultsUsers() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            searchString = bundle.getString(Params.SEARCH_TEXT);

            new SearchUser(searchString,0,getResources().getInteger(R.integer.lazy_load_limitation), FragmentResultsUsers.this).execute();
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
        pd = new ProgressDialogCustom(getActivity());

        // setUp ListView
        setUpListView();

        users = new ArrayList<User>();
        mAdapter = new FriendsAdapter(getActivity(), users, true, FragmentResultsUsers.this);
        list.setAdapter(mAdapter);

        return view;
    }

    // LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        new SearchUser(searchString,users.get(users.size()-1).id,getResources().getInteger(R.integer.lazy_load_limitation), FragmentResultsUsers.this).execute();
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
                users = new ArrayList<User>();
                // get results again
                users.clear();
                new SearchUser(searchString,0,getResources().getInteger(R.integer.lazy_load_limitation), FragmentResultsUsers.this).execute();
                swipeView.setRefreshing(true);
            }
        });
        listFooterView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.INVISIBLE);
        list.addFooterView(listFooterView);
        // ListView LoadMore
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
                            && users.size()>0 && users.size()%getResources().getInteger(R.integer.lazy_load_limitation)==0) {
                        loadMoreData();
                    }
                }
            }
        });
    }

    @Override
    public void getResult(Object result) {
        pd.dismiss();
        try {
            if (result instanceof ArrayList) {
                users = new ArrayList<>();
                ArrayList<SearchItemUserBusiness> searchResult = (ArrayList<SearchItemUserBusiness>) result;
                User user = null;
                for (SearchItemUserBusiness item : searchResult) {
                    user = new User();
                    user.userName = item.username;
                    user.profilePictureId = item.pictureId;
                    users.add(user);
                }

                mAdapter = new UserResultsAdapter(getActivity(), users);
                list.setAdapter(mAdapter);
                isLoadingMore=false;
                swipeView.setRefreshing(false);
                listFooterView.setVisibility(View.GONE);
            }
        } catch(Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        pd.dismiss();
        try {
            String errorMessage = ServerAnswer.getError(getActivity(), errorCode);
            Dialogs.showMessage(getActivity(), errorMessage, users.size()==0 ? true : false);
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
