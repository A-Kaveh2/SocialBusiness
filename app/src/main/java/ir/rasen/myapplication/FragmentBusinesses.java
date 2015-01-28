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

import ir.rasen.myapplication.adapters.BusinessesAdapter;
import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.Edit;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.user.GetFollowingBusinesses;

/**
 * Created by 'Sina KH'.
 */
public class FragmentBusinesses extends Fragment implements WebserviceResponse, Edit {
    private static final String TAG = "FragmentBusinesses";

    private View view, listFooterView;

    private boolean isLoadingMore = false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private ListAdapter mAdapter;

    // business id is received here
    private String userId;

    private ArrayList<Business> businesses;

    public static FragmentBusinesses newInstance(String userId) {
        FragmentBusinesses fragment = new FragmentBusinesses();

        Bundle bundle = new Bundle();
        bundle.putString(Params.USER_ID, userId);
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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userId = bundle.getString(Params.USER_ID);
        } else {
            Log.e(TAG, "bundle is null!!");
            if (getActivity() != null) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
            }
        }

        list = (ListView) view.findViewById(R.id.list_businesses_business);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        // TODO: Change Adapter to display your content
        businesses = new ArrayList<Business>();

        /*
            for example, i've made some fake data to show ::
        */
       /* Business b1 = new Business();
        Business b2 = new Business();
        Business b3 = new Business();
        b1.name = ("RASEN");
        b2.name = ("IRAN AIR");
        b3.name = ("کبابی محل");
        businesses.add(b1);
        businesses.add(b2);
        businesses.add(b3);

        mAdapter = new BusinessesAdapter(getActivity(), businesses);
        ((AdapterView<ListAdapter>) view.findViewById(R.id.list_businesses_business)).setAdapter(mAdapter);
*/

        //TODO remove test parts
        new GetFollowingBusinesses(userId,FragmentBusinesses.this).execute();

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

    @Override
    public void getResult(Object result) {
        try {
            if (result instanceof ArrayList) {
                businesses = new ArrayList<Business>();
                Business business = null;
                ArrayList<SearchItemUserBusiness> searchItemUserBusinesses = (ArrayList<SearchItemUserBusiness>) result;
                for (SearchItemUserBusiness item : searchItemUserBusinesses) {
                    new Business();
                    business.name = item.name;
                    business.profilePicture = item.picture;
                    businesses.add(business);
                }

                // todo:: check if userId.equals(myId) or not
                boolean unFollowAvailable = true;
                mAdapter = new BusinessesAdapter(getActivity(), businesses, FragmentBusinesses.this, unFollowAvailable);
                ((AdapterView<ListAdapter>) view.findViewById(R.id.list_businesses_business)).setAdapter(mAdapter);
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

    private String editingId, editingText;
    private Dialog editingDialog;
    @Override
    public void setEditing(String id, String text, Dialog dialog) {
        editingId = id;
        editingText = text;
        editingDialog = dialog;
    }
}
