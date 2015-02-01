package ir.rasen.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.RatingBar;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.PostsAdapter;
import ir.rasen.myapplication.adapters.ProfilePostsGridAdapter;
import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.FriendshipRelation;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.helper.Permission;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.GridViewHeader;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.GetBusinessHomeInfo;
import ir.rasen.myapplication.webservice.post.GetBusinessPosts;
import ir.rasen.myapplication.webservice.post.GetSharedPosts;
import ir.rasen.myapplication.webservice.user.FollowBusiness;
import ir.rasen.myapplication.webservice.user.GetUserHomeInfo;

/**
 * Created by 'Sina KH'.
 */
public class FragmentProfile extends Fragment implements WebserviceResponse, EditInterface {
    private static final String TAG = "FragmentProfile";

    private SwipeRefreshLayout swipeView;
    private GridViewHeader grid;
    private ImageButton btnGrid, btnList;

    private View view, listFooterView, header;
    private boolean isLoadingMore = false;
    private ListAdapter mAdapter;

    private int profileType; // returns the type, USER or BUSINESS
    private boolean profileOwn; // true if user is the owner of user or business
    private int profileId; // id of user of business
    private String profileUsername;

    private Business profile_business;
    private User profile_user;

    ListAdapter listAdapter, gridAdapter;

    private WebserviceResponse webserviceResponse;
    private static Context cont;
    private boolean isBusinessProfile = false;

    int editingId;
    String editingText;
    Dialog editingDialog;

    @Override
    public void setEditing(int id, String text, Dialog dialog) {
        editingId = id;
        editingText = text;
        editingDialog = dialog;
    }

    private enum RunningWebserviceType {getUserHomeInfo, getUserPosts, getBustinessPosts, getBusinessHomeInfo}

    ;
    private static RunningWebserviceType runningWebserviceType;

    private ArrayList<Post> posts;

    private Bitmap profile_pic, cover_pic;

    public static FragmentProfile newInstance(Context context, int profileType, boolean profileOwn, int profileId) {
        FragmentProfile fragment = new FragmentProfile();

        cont = context;
        Bundle bundle = new Bundle();
        bundle.putInt(Params.PROFILE_TYPE, profileType);
        bundle.putBoolean(Params.PROFILE_OWN, profileOwn);
        bundle.putInt(Params.ID, profileId);
        fragment.setArguments(bundle);

        return fragment;
    }
    public static FragmentProfile newInstance(Context context, int profileType, boolean profileOwn, String username) {
        FragmentProfile fragment = new FragmentProfile();

        cont = context;
        Bundle bundle = new Bundle();
        bundle.putInt(Params.PROFILE_TYPE, profileType);
        bundle.putBoolean(Params.PROFILE_OWN, profileOwn);
        bundle.putString(Params.NAME, username);
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

        webserviceResponse = this;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            profileType = bundle.getInt(Params.PROFILE_TYPE);
            profileOwn = bundle.getBoolean(Params.PROFILE_OWN);
            if(bundle.containsKey(Params.ID))
                profileId = bundle.getInt(Params.ID);
            else if(bundle.containsKey(Params.NAME))
                profileUsername = bundle.getString(Params.NAME);
        } else {
            Log.e(TAG, "bundle is null!!");
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
        }


        if (!isBusinessProfile) {
            //get user home info

            //TODO remove test part
            //get user home info by sending user_id
            //new GetUserHomeInfo(LoginInfo.getUserId(cont),webserviceResponse).execute();

            // TODO ::
            if(profileId!=0) {

                //TODO for the test
                new GetUserHomeInfo(1, webserviceResponse).execute();
                runningWebserviceType = RunningWebserviceType.getUserHomeInfo;
            } else {
                // TODO :: GET with username
            }
        } else {
            //get business home info

            // TODO ::
            if(profileId!=0) {
                //TODO remove test part
                new GetBusinessHomeInfo("food_1", FragmentProfile.this).execute();
                runningWebserviceType = RunningWebserviceType.getBusinessHomeInfo;
            } else {
                // TODO :: GET with username
            }

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        this.view = view;

        header = (View) getActivity().getLayoutInflater().inflate(R.layout.fragment_profile_header, null);

        // check if back button should be visible or not!
        if (((ActivityMain) getActivity()).fragCount[((ActivityMain) getActivity()).pager.getCurrentItem()] == 0)
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
        if (((ActivityMain) getActivity()).pager.getCurrentItem() == 2)
            //&& ((ActivityMain) getActivity()).fragCount[2]==0)
            ((ActivityMain) getActivity()).rightDrawer();
        header.findViewById(R.id.btn_profile_drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(((ActivityMain) getActivity()).fragCount[2]==0)
                ((ActivityMain) getActivity()).openDrawer(Gravity.RIGHT);
                //else {
                //showPopup();
                //}
            }
        });

        ((ImageView) header.findViewById(R.id.img_profile_option1)).setImageResource(R.drawable.ic_menu_user);
        ((ImageView) header.findViewById(R.id.img_profile_option2)).setImageResource(R.drawable.ic_menu_reviews);
        // PROFILE
        if (profileType == Params.ProfileType.PROFILE_USER) {
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
            if (profileOwn == true) {
                myOwnProfile();
            } else {
                // SOMEONE'S PROFILE ( will be processed after loading data )
                //header.findViewById(R.id.btn_profile_on_picture).setVisibility(View.INVISIBLE);
            }
        }
        // BUSINESS
        if (profileType == Params.ProfileType.PROFILE_BUSINESS) {
            ((TextViewFont) header.findViewById(R.id.txt_profile_status)).setVisibility(View.GONE);
            ((RatingBar) header.findViewById(R.id.ratingBar_profile)).setVisibility(View.VISIBLE);
            // TODO: SHOULD BE REPLACED WITH ic_menu_call::
            ((ImageView) header.findViewById(R.id.img_profile_option3)).setImageResource(R.drawable.ic_menu_products);
            // CALL INFO
            header.findViewById(R.id.ll_profile_option3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (profile_business == null) return;
                    PassingBusiness.getInstance().setValue(profile_business);
                    InnerFragment innerFragment = new InnerFragment(getActivity());
                    innerFragment.newCallInfo();
                }
            });
            // REVIEWS OF BUSINESS
            header.findViewById(R.id.ll_profile_option2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InnerFragment innerFragment = new InnerFragment(getActivity());
                    innerFragment.newReviews(profileId);
                }
            });
            // FOLLOWERS
            header.findViewById(R.id.ll_profile_option1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InnerFragment innerFragment = new InnerFragment(getActivity());
                    innerFragment.newFollowers(profileId);
                }
            });
            if (profileOwn) {
                myOwnBusiness();
            } else {
                // SOMEONE'S PROFILE ( will be processed after loading data )
                //header.findViewById(R.id.btn_profile_on_picture).setVisibility(View.INVISIBLE);
            }
        }

        // TODO: NOW LOAD AND SHOW PROFILES DETAILS BASED ON PROFILE TYPE
        // for example, i've made some fake data in user and business::
/*        profile_business = new Business();
        profile_business.location_m = new Location_M("35.7014396", "51.3498186");
        profile_business.businessID = "RASEN Corporation";
        profile_business.name = "شرکت نرم افزاری راسن";
        profile_business.followersNumber = 22;
        profile_business.reviewsNumber = 11;
        profile_business.description = "توسعه و تولید...";
        WorkTime workTime = new WorkTime();
        try {
            workTime.setWorkDaysFromString("0,1,2,3");
        } catch (Exception e) {
        }
        workTime.time_open = 600;
        workTime.time_close = 1000;
        profile_business.workTime = workTime;
        profile_business.email = "rasen@rasen.com";
        profile_business.mobile = "09123456789";
        profile_business.phone = "02123456789";
        profile_business.rate = (float) 4.5;
        profile_business.webSite = "http://www.rasen.com";

        profile_user = new User();
        profile_user.userID = "sina_kh";
        profile_user.name = "سینا خلیلی";
        profile_user.followedBusinessesNumber = 24;
        profile_user.reviewsNumber = 12;
        profile_user.friendsNumber = 6;
        profile_user.aboutMe = "عشق یعنی انتظار, تو دل یه مادر بی قرار";
*/


        // TODO:: AFTER LOADING DATA COMPLETELY
        ///header.findViewById(R.id.btn_profile_on_picture).setVisibility(View.VISIBLE);
        // TODO: Change Adapter to display your content
        posts = new ArrayList<Post>();
        /*
            for example, i've made some fake data to show ::
        */
        /*Post post1 = new Post();
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
*/ // TODO: FOR TEST::
        listAdapter = new PostsAdapter(getActivity(), posts, webserviceResponse, FragmentProfile.this);
        gridAdapter = new ProfilePostsGridAdapter(getActivity(), posts);
        grid.setAdapter(gridAdapter);
    }

    void sendFriendRequest() {
        // TODO: SEND FRIEND REQUEST
    }


    void sendFollowRequest() {

        //TODO insert business_id here
        //new FollowBusiness(LoginInfo.getUserId(cont),"food_1",FragmentProfile.this).execute();

        //TODO remove test part
        new FollowBusiness(1, 1, FragmentProfile.this).execute();
    }

    void myOwnProfile() {
        ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.profile_edit_profile);
        // EDIT PROFILE
        header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityUserProfileEdit.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.to_0, R.anim.to_left);
            }
        });
    }

    void myOwnBusiness() {
        ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.business_settings);
        // EDIT BUSINESS
        header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit business now
                PassingBusiness.getInstance().setValue(profile_business);
                Intent intent = new Intent(getActivity(), ActivityBusinessSettings.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.to_0, R.anim.to_left);
            }
        });
        view.findViewById(R.id.btn_profile_new_post).setVisibility(View.VISIBLE);
        view.findViewById(R.id.btn_profile_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityNewPost_Step1.class);
                intent.putExtra(Params.BUSINESS_ID, profileId);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.to_0, R.anim.to_left);
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
                isLoadingMore = false;
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

    public void checkDrawerLock() {
        ((ActivityMain) getActivity()).rightDrawer();
    }

    @Override
    public void getResult(Object result) {

        try {

            if (result instanceof ResultStatus) {
                //delete post,follow business

            } else if (result instanceof User) {
                //get user home info
                User user = (User) result;
                int userID = user.id;
                String userName = user.userName;
                String name = user.name;
                String aboutMe = user.aboutMe;
                profile_pic = Image_M.getBitmapFromString(user.profilePicture);
                cover_pic = Image_M.getBitmapFromString(user.coverPicture);
                int friendRequestNumber = user.friendRequestNumber;
                int reviewsNumber = user.reviewsNumber;
                int followedBusinessNumber = user.followedBusinessesNumber;
                int friendsNumber = user.friendsNumber;
                Permission permissions = user.permissions;
                FriendshipRelation.Status friendshipRelationStatus = user.friendshipRelationStatus;

                //TODO assign
                profileType = Params.ProfileType.PROFILE_USER;
                assignNow();

                if (!isBusinessProfile) {
                    new GetSharedPosts(LoginInfo.getUserId(cont), 0, cont.getResources().getInteger(R.integer.lazy_load_limitation), FragmentProfile.this);
                    runningWebserviceType = RunningWebserviceType.getUserPosts;
                } else {
                    new GetBusinessPosts(1, 0, cont.getResources().getInteger(R.integer.lazy_load_limitation), FragmentProfile.this);
                    runningWebserviceType = RunningWebserviceType.getBustinessPosts;
                }

            } else if (result instanceof ArrayList) {


                //TODO assign
                if (runningWebserviceType == RunningWebserviceType.getUserPosts) {
                    //user shared posts
                    posts = (ArrayList<Post>) result;
                } else if (runningWebserviceType == RunningWebserviceType.getBustinessPosts) {
                    //business posts
                    posts = (ArrayList<Post>) result;
                }
                listAdapter = new PostsAdapter(getActivity(), posts, webserviceResponse, FragmentProfile.this);
                gridAdapter = new ProfilePostsGridAdapter(getActivity(), posts);
                grid.setAdapter(gridAdapter);

            } else if (result instanceof Business) {
                //business home info
                Business business = (Business) result;

                //TODO assign business
                profileType = Params.ProfileType.PROFILE_BUSINESS;
                profile_pic = Image_M.getBitmapFromString(business.profilePicture);
                cover_pic = Image_M.getBitmapFromString(business.coverPicture);

                assignNow();
            }

        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        try {
            //TODO display error
            String errorMessage = ServerAnswer.getError(cont, errorCode);
            Dialogs.showMessage(cont, errorMessage);
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    private void assignNow() {
        // TODO assigning values - uncomment these 2 lines:
        //((ImageViewCircle) header.findViewById(R.id.img_profile_pic)).setImageBitmap(profile_pic);
        //((ImageViewSquare) header.findViewById(R.id.img_profile_cover)).setImageBitmap(cover_pic);
        if (profileType == Params.ProfileType.PROFILE_BUSINESS) {
            ((TextViewFont) header.findViewById(R.id.txt_profile_name)).setText(profile_business.name);
            ((RatingBar) header.findViewById(R.id.ratingBar_profile)).setRating(profile_business.rate);
            ((TextViewFont) header.findViewById(R.id.txt_profile_option1)).setText(profile_business.followersNumber + " " + getString(R.string.followers_num));
            ((TextViewFont) header.findViewById(R.id.txt_profile_option2)).setText(profile_business.reviewsNumber + " " + getString(R.string.review));
            ((TextViewFont) header.findViewById(R.id.txt_profile_option3)).setText(R.string.call_info);
            // MY OWN BUSINESS
            if (profileOwn == true) {
                myOwnBusiness();
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
        } else if (profileType == Params.ProfileType.PROFILE_USER) {
            ((TextViewFont) header.findViewById(R.id.txt_profile_name)).setText(profile_user.name);
            ((TextViewFont) header.findViewById(R.id.txt_profile_status)).setText(profile_user.aboutMe);
            ((TextViewFont) header.findViewById(R.id.txt_profile_option1)).setText(profile_user.friendsNumber + " " + getString(R.string.friend));
            ((TextViewFont) header.findViewById(R.id.txt_profile_option2)).setText(profile_user.reviewsNumber + " " + getString(R.string.review));
            ((TextViewFont) header.findViewById(R.id.txt_profile_option3)).setText(profile_user.followedBusinessesNumber + " " + getString(R.string.business));
            // MY OWN USER'S PROFILE
            if (profileOwn == true) {
                myOwnProfile();
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
    }
}
