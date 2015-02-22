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
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.BlockedsAdapter;
import ir.rasen.myapplication.adapters.FollowersAdapter;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.GetBlockedUsers;
import ir.rasen.myapplication.webservice.business.GetBusinessFollowers;
import ir.rasen.myapplication.webservice.review.GetBusinessReviews;

/**
 * Created by 'Sina KH'.
 */
public class FragmentBlockeds extends Fragment implements WebserviceResponse, EditInterface {
    private static final String TAG = "FragmentBlockeds";

    private View view, listFooterView;

    private boolean isLoadingMore=false;
    private SwipeRefreshLayout swipeView;
    private ListView list;
    private BaseAdapter mAdapter;
    ArrayList<User> blockeds;

    // business id is received here
    private static int businessId;

    private ProgressDialogCustom pd;

    public static FragmentBlockeds newInstance (int businessID){
        FragmentBlockeds fragment = new FragmentBlockeds();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.BUSINESS_ID, businessId);
        fragment.setArguments(bundle);
        businessId = businessID;

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentBlockeds() {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            businessId = bundle.getInt(Params.BUSINESS_ID);
        } else {
            Log.e(TAG, "bundle is null!!");
            if(getActivity()!=null){
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetBlockedUsers(businessId,FragmentBlockeds.this).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blockeds, container, false);
        this.view = view;

        list = (ListView) view.findViewById(R.id.list_blockeds_blockeds);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        pd = new ProgressDialogCustom(getActivity());

        // setUp ListView
        setUpListView();

        // TODO: Change Adapter to display your content
        blockeds = new ArrayList<User>();

        /*
            for example, i've made some fake data to show ::
        */
        /*User User1 = new User();
        User User2 = new User();
        User User3 = new User();
        User1.name=("SINA");
        User2.name=("HASAN");
        User3.name=("HOSSEIN");
        blockeds.add(User1);
        blockeds.add(User2);
        blockeds.add(User3);*/

        /// TODO:: UNCOMMENT AFTER LAZY LOAD
        //mAdapter = new BlockedsAdapter(getActivity(), blockeds);
        //list.setAdapter(mAdapter);

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
                blockeds = new ArrayList<User>();
                // TODO get blockeds again
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
                    if (!swipeView.isRefreshing() && !isLoadingMore) {
                        loadMoreData();
                    }
                }
            }
        });
    }

    @Override
    public void getResult(Object result) {
        pd.dismiss();
        if(result instanceof ArrayList){
            //result from executing getBusinessFollowers
            ArrayList<SearchItemUserBusiness> businessesFollowers = (ArrayList<SearchItemUserBusiness>)result;
            ArrayList<User> temp = new ArrayList<>();
            temp.addAll(blockeds);
            blockeds = new ArrayList<>();
            User user;
            for(SearchItemUserBusiness item:businessesFollowers){
                user = new User();
                user.id = item.id;
                user.userName = item.username;
                user.profilePictureId = item.pictureId;
                temp.add(user);
            }

            // temp have the result. Display the result.

            blockeds.clear();
            blockeds.addAll(temp);
            mAdapter = new BlockedsAdapter(getActivity(), blockeds, businessId, FragmentBlockeds.this, FragmentBlockeds.this);
            list.setAdapter(mAdapter);
            isLoadingMore=false;
            swipeView.setRefreshing(false);
            listFooterView.setVisibility(View.GONE);

        } else if(editingId>0){
            for(int i=0; i<blockeds.size(); i++) {
                if(blockeds.get(i).id==editingId) {
                    blockeds.remove(i);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public void getError(Integer errorCode) {
        try {
            String errorMessage = ServerAnswer.getError(getActivity(), errorCode);
            Dialogs.showMessage(getActivity(), errorMessage, blockeds.size()==0 ? true : false);
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
        if(id>0)
            pd.show();
        else
            pd.dismiss();
    }
}
