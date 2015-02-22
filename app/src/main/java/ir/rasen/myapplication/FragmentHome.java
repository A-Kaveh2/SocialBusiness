package ir.rasen.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.HomePostsAdapter;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingActiveRole;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.helper.PassingPosts;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.announcement.GetLastCommentNotification;
import ir.rasen.myapplication.webservice.post.GetTimeLinePosts;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by 'Sina KH'.
 */

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class FragmentHome extends Fragment implements WebserviceResponse, EditInterface {
    private static final String TAG = "FragmentHome";

    /**
     * The fragment's ListView/GridView.
     */
    private StickyListHeadersListView mListView;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private BaseAdapter mAdapter;
    private SwipeRefreshLayout swipeView;
    private View listFooterView;
    private RelativeLayout actionBar;
    //private RelativeLayout rlHome;
    private TextViewFont title;
    private boolean singlePost = false;
    public ArrayList<Post> posts;

    Boolean isLoadingMore = false;

    //Boolean actionBarShown = true, isMovingActionBar = false;
    //private int barHeight, getY;
    //private float delta;

    private int homeType;
    private String homeTitle;
    private WebserviceResponse webserviceResponse;
    private EditInterface editDelegateInterface;

    private ProgressDialogCustom pd;

    // as home fragment
    public static FragmentHome newInstance() {
        FragmentHome fragment = new FragmentHome();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.HOME_TYPE, Params.HomeType.HOME_HOME);
        fragment.setArguments(bundle);

        return fragment;
    }

    // as post fragment
    public static FragmentHome newInstance(String title) {
        FragmentHome fragment = new FragmentHome();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.HOME_TYPE, Params.HomeType.HOME_POST);
        bundle.putString(Params.TITLE, title);
        fragment.setArguments(bundle);

        return fragment;
    }

    public FragmentHome() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startLoad();

    }

    public void startLoad() {
        webserviceResponse = this;
        editDelegateInterface = this;
        pd = new ProgressDialogCustom(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            homeType = bundle.getInt(Params.HOME_TYPE);
        } else {
            Log.e(TAG, "bundle is null!!");
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
        }

        // ArrayList to show
        posts = new ArrayList<Post>();

        // Check home type
        if (homeType == Params.HomeType.HOME_POST) {

            // load as post page!
            homeTitle = bundle.getString(Params.TITLE);
            posts = PassingPosts.getInstance().getValue();
            PassingPosts.getInstance().setValue(null);
            if (posts.size() == 1) {
                singlePost = true;
            }
            mAdapter = new HomePostsAdapter(getActivity(), posts, webserviceResponse, editDelegateInterface, pd);
        } else if (homeType == Params.HomeType.HOME_HOME) {

            mAdapter = new HomePostsAdapter(getActivity(), posts,webserviceResponse, editDelegateInterface, pd);
            new GetTimeLinePosts(LoginInfo.getUserId(getActivity())
                    ,0,getResources().getInteger(R.integer.lazy_load_limitation),FragmentHome.this).execute();
            pd.show();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Set the adapter
        mListView = (StickyListHeadersListView) view.findViewById(R.id.list_home);
        actionBar = (RelativeLayout) view.findViewById(R.id.rl_home_actionbar);
        //rlHome = (RelativeLayout) view.findViewById(R.id.rl_home_inner);
        title = (TextViewFont) view.findViewById(R.id.txt_home_title);

        // set tile of fragment
        if (homeTitle != null) title.setText(homeTitle);

        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        // LISTVIEW FOOTER AND ADAPTER
        setUpListView();

        // AUTO HIDE ACTION BAR
        //autoHideActionBar();
        // ON CLICK LISTENERS
        onClickLisneners(view);

        return view;
    }

    // TODO: LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        isLoadingMore = true;
        listFooterView.setVisibility(View.VISIBLE);
    }

    // SET ONCLICK LISTENERS AND DRAWER..
    void onClickLisneners(final View view) {

        // check if the button should be back or options or nothing!
        if (((ActivityMain) getActivity()).fragCount[((ActivityMain) getActivity()).pager.getCurrentItem()] > 0) {
            // back button!
            view.findViewById(R.id.btn_home_options).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        } else {
            // nothing
            actionBar.setVisibility(View.GONE);
        }

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
                if (homeType == Params.HomeType.HOME_HOME) {
                    posts = new ArrayList<Post>();
                    new GetTimeLinePosts(LoginInfo.getUserId(getActivity())
                            ,0,getResources().getInteger(R.integer.lazy_load_limitation),FragmentHome.this).execute();
                    swipeView.setRefreshing(true);
                }
            }
        });
        listFooterView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.INVISIBLE);
        mListView.addFooterView(listFooterView);
        mListView.setAdapter((StickyListHeadersAdapter) mAdapter);
        // TODO: ListView LoadMore if it's not single post
        if (!singlePost) {
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
    }

    @Override
    public void getResult(Object result) {
        pd.dismiss();
        try {
            if (result instanceof ArrayList) {
                ArrayList<Post> temp = new ArrayList<>();
                temp.addAll((ArrayList<Post>) result);
                posts.addAll(temp);
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void getError(Integer errorCode) {
        pd.dismiss();
        try {
            String errorMessage = ServerAnswer.getError(getActivity(), errorCode);
            Dialogs.showMessage(getActivity(), errorMessage, false);
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    private int editingId;
    private String editingText;
    private Dialog editingDialog;

    @Override
    public void setEditing(int id, String text, Dialog dialog) {
        pd.show();
        editingId = id;
        editingText = text;
        editingDialog = dialog;
    }

/*
    void autoHideActionBar() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                barHeight = actionBar.getMeasuredHeight();
                getY=0;
                mListView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                delta = event.getY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if(isMovingActionBar) delta = event.getY();
                                if (actionBarShown && event.getY() - delta < - Params.ActionBarSensitivity.TO_HIDE) {
                                    // HIDE ACTIONBAR
                                    /*final Handler moveHandler = new Handler();
                                    Runnable hider;
                                    //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){ // API_LEVEL <11
                                        hider = new Runnable() {
                                            @Override
                                            public void run() {
                                                setY(getY - barHeight);// / 10);
                                                //delta=delta+barHeight;
                                                /*if (getY > -barHeight)
                                                    moveHandler.postDelayed(this, 1);
                                                else {*/
                                        /*        isMovingActionBar = false;
                                    delta=delta+barHeight;
                                                //}
                                            //}
                                        //};
                                    /*} else {
                                        hider = new Runnable() {
                                            @Override
                                            public void run() {
                                                getY=getY - barHeight / 5;
                                                rlHome.setY(getY);
                                                if (getY > -barHeight)
                                                    moveHandler.postDelayed(this, 10);
                                                else {
                                                    isMovingActionBar = false;
                                                }
                                            }
                                        };
                                    }*/
    //isMovingActionBar = true;
                                    /*delta = event.getY();
                                    actionBarShown=false;
                                    actionBar.setVisibility(View.GONE);
                                    //moveHandler.post(hider);
                                } else if (!actionBarShown && (event.getY() - delta > Params.ActionBarSensitivity.TO_SHOW)) {// || (((AbsListView) mListView).getChildAt(0).getTop()==0))) {
                                    // SHOW ACTIONBAR
                                    //final Handler moveHandler = new Handler();
                                    //Runnable viewer;
                                    //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) { // API_LEVEL <11
                                       /* viewer = new Runnable() {
                                            @Override
                                            public void run() {
                                                setY(getY + barHeight);// / 10);
                                                //delta=delta-barHeight;
                                                /*if (getY < 0)
                                                    moveHandler.postDelayed(this, 1);
                                                else {*/
    // isMovingActionBar = false;
    //}
    //      }
    //   };
                                    /*} else {
                                        viewer = new Runnable() {
                                            @Override
                                            public void run() {
                                                getY = getY + barHeight / 5;
                                                rlHome.setY(getY);
                                                if (getY < 0)
                                                    moveHandler.postDelayed(this, 10);
                                                else
                                                    isMovingActionBar = false;
                                            }
                                        };
                                    }*/
                                    /*actionBarShown=true;
                                    delta = event.getY();
                                    actionBar.setVisibility(View.VISIBLE);
                                    delta=delta-barHeight;
                                    //isMovingActionBar=true;
                                   // moveHandler.post(viewer);
                                }
                        }
                        return false;
                    }
                });
            }
        }, 5);
    }

    /*public void setY(int y) {
        //FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) rlHome.getLayoutParams();
        lp.topMargin=y;
        //rlHome.setLayoutParams(lp);
        getY=y;
    }*/
}
