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

import ir.rasen.myapplication.adapters.RequestsAdapter;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.friend.GetUserFriendRequests;

/**
 * Created by 'Sina KH' on 1/21/2015.
 */
public class FragmentRequests extends Fragment implements WebserviceResponse {
    private static final String TAG = "FragmentRequests";

    private View view, listFooterView;

    private boolean isLoadingMore=false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private ListAdapter mAdapter;

    int userId;
    Boolean nearby;

    ArrayList<SearchItemUserBusiness> requests;

    ProgressDialogCustom pd;

    public static FragmentRequests newInstance (int userId){
        FragmentRequests fragment = new FragmentRequests();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.USER_ID, userId);
        fragment.setArguments(bundle);

        return fragment;
    }

    public FragmentRequests() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userId = bundle.getInt(Params.USER_ID);

            new GetUserFriendRequests(userId,FragmentRequests.this).execute();
        } else {
            Log.e(TAG, "bundle is null!!");
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        this.view = view;

        list = (ListView) view.findViewById(R.id.list_requests_request);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        pd = new ProgressDialogCustom(getActivity());

        // setUp ListView
        setUpListView();

        requests = new ArrayList<SearchItemUserBusiness>();
        mAdapter = new RequestsAdapter(getActivity(), requests, FragmentRequests.this);
        list.setAdapter(mAdapter);

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
                requests = new ArrayList<SearchItemUserBusiness>();
                // TODO get requests again
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
                            && requests.size()>0 && requests.size()%getResources().getInteger(R.integer.lazy_load_limitation)==0) {
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
                requests = (ArrayList<SearchItemUserBusiness>) result;
                mAdapter = new RequestsAdapter(getActivity(), requests, FragmentRequests.this);
                list.setAdapter(mAdapter);
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
            Dialogs.showMessage(getActivity(), errorMessage, requests.size()==0 ? true : false);
        } catch(Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }
}
