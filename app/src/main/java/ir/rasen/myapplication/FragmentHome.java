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
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.HomePostsAdapter;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingPosts;
import ir.rasen.myapplication.ui.TextViewFont;
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
public class FragmentHome extends Fragment {
    private static final String TAG = "FragmentHome";

    /**
     * The fragment's ListView/GridView.
     */
    private StickyListHeadersListView mListView;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;
    private SwipeRefreshLayout swipeView;
    private View listFooterView;
    private RelativeLayout actionBar;
    //private RelativeLayout rlHome;
    private TextViewFont title;
    private boolean singlePost = false;

    Boolean isLoadingMore=false;

    //Boolean actionBarShown = true, isMovingActionBar = false;
    //private int barHeight, getY;
    //private float delta;

    private int homeType;
    private String homeTitle;

    // as home fragment
    public static FragmentHome newInstance (){
        FragmentHome fragment = new FragmentHome();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.HOME_TYPE, Params.HomeType.HOME_HOME);
        fragment.setArguments(bundle);

        return fragment;
    }

    // as post fragment
    public static FragmentHome newInstance (String title){
        FragmentHome fragment = new FragmentHome();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.HOME_TYPE, Params.HomeType.HOME_POST);
        bundle.putString(Params.TITLE, title);
        fragment.setArguments(bundle);

        return fragment;
    }
/*
    // as search fragment
    public static FragmentHome newInstance (String searchString){
        FragmentHome fragment = new FragmentHome();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.HOME_TYPE, Params.HomeType.HOME_SEARCH);
        bundle.putString(Params.TITLE, searchString);
        fragment.setArguments(bundle);

        return fragment;
    }
*/
    public FragmentHome() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            homeType = bundle.getInt(Params.HOME_TYPE);
        } else {
            Log.e(TAG, "bundle is null!!");
            getActivity().finish();
        }

        // ArrayList to show
        ArrayList<Post> posts = new ArrayList<Post>();

        // TODO: Check home type
        //if(homeType==Params.HomeType.HOME_SEARCH) {

        //    // load as search page!
        //    homeTitle = bundle.getString(Params.TITLE);

        //} else
        if(homeType==Params.HomeType.HOME_POST) {

            // load as post page!
            homeTitle = bundle.getString(Params.TITLE);
            posts = PassingPosts.getInstance().getValue();
            if(posts.size()==1) {
                singlePost = true;
            }
            mAdapter = new HomePostsAdapter(getActivity(), posts);

        } else if(homeType==Params.HomeType.HOME_HOME) {

            // TODO: LOAD AS HOME PAGE
            // TODO: Change Adapter to display your content after receiving HOME from net

            /*
                for example, i've made some fake data to show ::
            */
            Post post1 = new Post();
            Post post2 = new Post();
            Post post3 = new Post();
            post1.businessID = "راسن";
            post1.description = "یک نرم افزار عالی!!";
            post1.price = "100.000";
            post1.code = "30";
            ArrayList<Comment> lastThreeComments = new ArrayList<>();
            Comment comment = new Comment();
            comment.userID = "SINA";
            comment.text = "سلام";
            lastThreeComments.add(comment);
            post1.lastThreeComments = lastThreeComments;
            post1.title = "عنوان!!";
            posts.add(post1);
            post2.businessID = "sina";
            post2.description = "programmer - RASEN CO.";
            post2.price = "123.456";
            post2.code = "30";
            post2.title = "عنوان!!";
            post2.lastThreeComments = lastThreeComments;
            posts.add(post2);
            post3.businessID = "sina";
            post3.description = "progrsafasfasfasfafafasfasd\n\nammer - RASEN CO.";
            post3.price = "125.234";
            post3.code = "30";
            post3.title = "عنوان!!";
            post3.lastThreeComments = lastThreeComments;
            posts.add(post3);

            mAdapter = new HomePostsAdapter(getActivity(), posts);

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
        if(homeTitle!=null) title.setText(homeTitle);

        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        // LISTVIEW FOOTER AND ADAPTER
        setUpListView();

        // TODO: AUTO HIDE ACTION BAR
        //autoHideActionBar();
        // TODO: ON CLICK LISTENERS
        onClickLisneners(view);

        return view;
    }

    // TODO: LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        listFooterView.setVisibility(View.VISIBLE);
    }

    // SET ONCLICK LISTENERS AND DRAWER..
    void onClickLisneners(final View view) {

        // check if the button should be back or options or nothing!
        if(((ActivityMain) getActivity()).fragCount[((ActivityMain) getActivity()).pager.getCurrentItem()] > 0) {
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
                swipeView.setRefreshing(true);
                // TODO: CANCEL LOADING MORE AND REFRESH HERE...
                listFooterView.setVisibility(View.INVISIBLE);
                isLoadingMore=false;
            }
        });
        listFooterView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more, null, false);
        listFooterView.setVisibility(View.INVISIBLE);
        mListView.addFooterView(listFooterView);
        mListView.setAdapter((StickyListHeadersAdapter) mAdapter);
        // TODO: ListView LoadMore if it's not single post
        if(!singlePost) {
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
                            isLoadingMore = true;
                            loadMoreData();
                        }
                    }
                }
            });
        }
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
                                    // TODO :: HIDE ACTIONBAR
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
                                    // TODO :: SHOW ACTIONBAR
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
