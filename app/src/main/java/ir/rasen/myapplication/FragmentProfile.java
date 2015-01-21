package ir.rasen.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.PostsAdapter;
import ir.rasen.myapplication.adapters.ProfilePostsGridAdapter;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.GridViewHeader;
import ir.rasen.myapplication.ui.TextViewFont;

/**
 * Created by 'Sina KH'.
 */
public class FragmentProfile extends Fragment {
    private static final String TAG = "FragmentProfile";

    private SwipeRefreshLayout swipeView;
    private GridViewHeader grid;
    private ImageButton btnGrid, btnList;

    private View view, listFooterView, header;
    private boolean isLoadingMore=false;
    private ListAdapter mAdapter;

    private int profileType; // returns the type, USER or BUSINESS
    private boolean profileOwn; // true if user is the owner of user or business
    private String profileId; // id of user of business

    ListAdapter listAdapter, gridAdapter;

    public static FragmentProfile newInstance (int profileType, boolean profileOwn){
        FragmentProfile fragment = new FragmentProfile();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.PROFILE_TYPE, profileType);
        bundle.putBoolean(Params.PROFILE_OWN, profileOwn);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentProfile() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            profileType = bundle.getInt(Params.PROFILE_TYPE);
            profileOwn = bundle.getBoolean(Params.PROFILE_OWN);
        } else {
            Log.e(TAG, "bundle is null!!");
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        this.view = view;

        // TODO:: SET PROFILE ID !!
        profileId="!";

        header = (View)getActivity().getLayoutInflater().inflate(R.layout.fragment_profile_header,null);

        // check if back button should be visible or not!
        if(((ActivityMain) getActivity()).fragCount[((ActivityMain) getActivity()).pager.getCurrentItem()] == 0)
            header.findViewById(R.id.btn_profile_back).setVisibility(View.GONE);

        btnGrid = (ImageButton) header.findViewById(R.id.btn_profile_grid);
        btnList = (ImageButton) header.findViewById(R.id.btn_profile_list);

        grid = (GridViewHeader) view.findViewById(R.id.grid_profile);
        grid.addHeaderView(header);
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnList.setBackgroundResource(R.drawable.selected_tab);
                btnGrid.setBackgroundResource(android.R.color.transparent);
                grid.setAdapter(gridAdapter);
                grid.setNumColumns(3);
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnList.setBackgroundResource(android.R.color.transparent);
                btnGrid.setBackgroundResource(R.drawable.selected_tab);
                grid.setNumColumns(1);
                grid.setAdapter(listAdapter);
            }
        });

        // setUp GridView
        setUpGridView();

        // LOAD VIEWS
        loadViews();

        return view;
    }

    void loadViews() {

        // SEARCH USERS
        ((ImageButton) header.findViewById(R.id.btn_profile_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InnerFragment innerFragment = new InnerFragment(getActivity());
                innerFragment.newSearchUsers();
            }
        });

        // SLIDING DRAWER
        ((ActivityMain) getActivity()).rightDrawer();
        header.findViewById(R.id.btn_profile_drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityMain) getActivity()).openDrawer(Gravity.RIGHT);
            }
        });

        ((ImageView) header.findViewById(R.id.img_profile_option1)).setImageResource(R.drawable.ic_menu_user);
        ((ImageView) header.findViewById(R.id.img_profile_option2)).setImageResource(R.drawable.ic_menu_reviews);
        // PROFILE
        if(profileType==Params.ProfileType.PROFILE_USER) {
            ((ImageView) header.findViewById(R.id.img_profile_option3)).setImageResource(R.drawable.ic_menu_businesses);
            // FRIENDS
            header.findViewById(R.id.ll_profile_option1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InnerFragment innerFragment = new InnerFragment(getActivity());
                    innerFragment.newFriends(profileId);
                }
            });
            // REVIEWS OF USER
            header.findViewById(R.id.ll_profile_option2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InnerFragment innerFragment = new InnerFragment(getActivity());
                    innerFragment.newPrfoileReviews(profileId);
                }
            });
            // BUSINESSES OF USER
            header.findViewById(R.id.ll_profile_option3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InnerFragment innerFragment = new InnerFragment(getActivity());
                    innerFragment.newBusinessesFragment(profileId);
                }
            });
            // MY OWN USER'S PROFILE
            if(profileOwn==true) {
                myOwnProfile();
            } else {
                // SOMEONE'S PROFILE ( will be processed after loading data )
                header.findViewById(R.id.btn_profile_on_picture).setVisibility(View.INVISIBLE);
            }
        }
        // BUSINESS
        if(profileType==Params.ProfileType.PROFILE_BUSINESS) {
            // TODO: SHOULD BE REPLACED WITH ic_menu_call::
            ((ImageView) header.findViewById(R.id.img_profile_option3)).setImageResource(R.drawable.ic_menu_products);
            // CALL INFO
            header.findViewById(R.id.ll_profile_option3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InnerFragment innerFragment = new InnerFragment(getActivity());
                    innerFragment.newCallInfo(profileId);
                }
            });
            // FRIENDS
            header.findViewById(R.id.ll_profile_option1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InnerFragment innerFragment = new InnerFragment(getActivity());
                    innerFragment.newFollowers(profileId);
                }
            });
            if(profileOwn) {
                myOwnBusiness();
            } else {
                // SOMEONE'S PROFILE ( will be processed after loading data )
                header.findViewById(R.id.btn_profile_on_picture).setVisibility(View.INVISIBLE);
            }
        }

        // TODO: NOW LOAD AND SHOW PROFILES DETAILS
        // ...
        // TODO: THEN RUN THIS ::
        if(profileType==Params.ProfileType.PROFILE_BUSINESS) {
            // MY OWN BUSINESS
            if (profileOwn == true) { myOwnBusiness();
            } else { // SOMEONE'S BUSINESS
                ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.follow_request);
                // FRIEND REQUEST
                header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendFollowRequest();
                    }
                });
            }
        } else if(profileType==Params.ProfileType.PROFILE_USER) {
            // MY OWN USER'S PROFILE
            if(profileOwn==true) { myOwnProfile();
            } else { // SOMEONE'S PROFILE
                ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.friend_request);
                // FRIEND REQUEST
                header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendFriendRequest();
                    }
                });
            }
        }

        // TODO:: AFTER LOADING DATA COMPLETELY
        header.findViewById(R.id.btn_profile_on_picture).setVisibility(View.VISIBLE);
        // TODO: Change Adapter to display your content
        ArrayList<Post> posts = new ArrayList<Post>();
        /*
            for example, i've made some fake data to show ::
        */
        Post post1 = new Post();
        Post post2 = new Post();
        Post post3 = new Post();
        post1.title="راسن";
        post1.businessID= "RAASEN";
        post1.price= "100.000";
        post1.code="30";
        posts.add(post1);
        post2.businessID="sina";
        post2.description= "programmer - RASEN CO.";
        post2.price= "123.456";
        post2.code="30";
        posts.add(post2);
        post3.businessID="sina";
        post3.description= "progrsafasfasfasfafafasfasd\n\nammer - RASEN CO.";
        post3.price= "125.234";
        post3.code="30";
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post2);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);
        posts.add(post3);

        listAdapter = new PostsAdapter(getActivity(), posts);
        gridAdapter = new ProfilePostsGridAdapter(getActivity(), posts);
        grid.setAdapter(gridAdapter);
    }

    void sendFriendRequest() {
        // TODO: SEND FRIEND REQUEST
    }

    void sendFollowRequest() {
        // TODO: SEND FOLLOW REQUEST
    }

    void myOwnProfile() {
        ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.profile_edit_profile);
        // EDIT PROFILE
        header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityProfileEdit.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
    void myOwnBusiness() {
        ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.profile_edit_business);
        // EDIT BUSINESS
        header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityNewBusiness_Step1.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        view.findViewById(R.id.btn_profile_new_post).setVisibility(View.VISIBLE);
        view.findViewById(R.id.btn_profile_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityNewPost.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    void setUpGridView() {
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
        grid.addFooterView(listFooterView);
        // TODO: ListView LoadMore
        grid.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    // TODO: LOAD MORE DATA
    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        listFooterView.setVisibility(View.VISIBLE);
    }

}
