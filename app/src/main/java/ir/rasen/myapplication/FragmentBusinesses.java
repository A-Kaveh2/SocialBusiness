package ir.rasen.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.BusinessesAdapter;
import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.user.FollowBusiness;
import ir.rasen.myapplication.webservice.user.GetFollowingBusinesses;

/**
 * Created by 'Sina KH'.
 */
public class FragmentBusinesses extends Fragment implements WebserviceResponse, EditInterface {
    private static final String TAG = "FragmentBusinesses";

    private View view, listFooterView;

    private boolean isLoadingMore = false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private BaseAdapter mAdapter;

    // business id is received here
    private int userId;
    private Context context;

    private ArrayList<Business> businesses;

    private ProgressDialogCustom pd;

    public static FragmentBusinesses newInstance(int userId) {
        FragmentBusinesses fragment = new FragmentBusinesses();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.USER_ID, userId);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentBusinesses() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_businesses, container, false);
        this.view = view;
        context = getActivity();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userId = bundle.getInt(Params.USER_ID);
        } else {
            Log.e(TAG, "bundle is null!!");
            if (getActivity() != null) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
            }
        }

        list = (ListView) view.findViewById(R.id.list_businesses_business);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        pd = new ProgressDialogCustom(context);
        businesses = new ArrayList<Business>();
        boolean unFollowAvailable = (userId==LoginInfo.getUserId(context));
        mAdapter = new BusinessesAdapter(getActivity(), businesses, FragmentBusinesses.this, unFollowAvailable,FragmentBusinesses.this);
        ((AdapterView<ListAdapter>) view.findViewById(R.id.list_businesses_business)).setAdapter(mAdapter);

        
        new GetFollowingBusinesses(LoginInfo.getUserId(getActivity()), FragmentBusinesses.this).execute();
        pd.show();

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
                businesses = new ArrayList<Business>();
                // TODO get businesses again
                new GetFollowingBusinesses(userId, FragmentBusinesses.this).execute();
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
                pd.hide();
                ArrayList<Business> temp = new ArrayList<>();
                temp.addAll(businesses);
                Business business = null;
                ArrayList<SearchItemUserBusiness> searchItemUserBusinesses = (ArrayList<SearchItemUserBusiness>) result;
                for (SearchItemUserBusiness item : searchItemUserBusinesses) {
                    business = new Business();
                    business.id = item.id;
                    business.businessUserName = item.username;
                    business.profilePictureId = item.pictureId;

                    //user pictureId to download image with DownloadImages class
                    temp.add(business);
                }

                businesses.clear();
                businesses.addAll(temp);
                mAdapter.notifyDataSetChanged();
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
            Dialogs.showMessage(getActivity(), errorMessage, businesses.size()==0 ? true : false);
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
