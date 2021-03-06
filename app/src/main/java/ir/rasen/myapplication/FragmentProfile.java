package ir.rasen.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.PostsAdapter;
import ir.rasen.myapplication.adapters.ProfilePostsGridAdapter;
import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.FriendshipRelation;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.GridViewHeader;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.ImageViewCover;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.DownloadImages;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.GetBusinessHomeInfo;
import ir.rasen.myapplication.webservice.friend.RequestCancelFriendship;
import ir.rasen.myapplication.webservice.friend.RequestFriendship;
import ir.rasen.myapplication.webservice.post.DeletePost;
import ir.rasen.myapplication.webservice.post.GetBusinessPosts;
import ir.rasen.myapplication.webservice.post.GetSharedPosts;
import ir.rasen.myapplication.webservice.user.FollowBusiness;
import ir.rasen.myapplication.webservice.user.GetUserHomeInfo;
import ir.rasen.myapplication.webservice.user.UnFollowBusiness;


/**
 * Created by 'Sina KH'.
 */
public class FragmentProfile extends Fragment implements WebserviceResponse, EditInterface {
    private static final String TAG = "FragmentProfile";

    public static FragmentProfile fragmentProfile;

    private SwipeRefreshLayout swipeView;
    private GridViewHeader grid;
    private ImageButton btnGrid, btnList;

    private View view, listFooterView, header;
    private boolean isLoadingMore = false;

    private int profileType; // returns the type, USER or BUSINESS
    private boolean profileOwn; // true if user is the oawner of user or business
    private int profileId; // id of user of business
    private String profileUsername;

    private Business profile_business;
    private User profile_user;

    BaseAdapter listAdapter, gridAdapter;

    private WebserviceResponse webserviceResponse;
    private static Context cont;

    int editingId;
    String editingText;
    Dialog editingDialog;

    private ProgressDialogCustom pd;
    private DownloadImages downloadImages;

    private boolean follow_friend_request_sent = false;


    private enum RunningWebserviceType {getUserHomeInfo, getUserPosts, getBustinessPosts, getBusinessHomeInfo}

    private static RunningWebserviceType runningWebserviceType;

    public ArrayList<Post> uPosts;//user posts
    public ArrayList<Post> bPosts;//business posts


    @Override
    public void setEditing(int id, String text, Dialog dialog) {
        editingId = id;
        editingText = text;
        editingDialog = dialog;
    }

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

        startLoad();

    }

    public void startLoad() {
        webserviceResponse = this;
        downloadImages = new DownloadImages(getActivity());
        pd = new ProgressDialogCustom(getActivity());
        downloadImages = new DownloadImages(getActivity());
        fragmentProfile = this;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            profileType = bundle.getInt(Params.PROFILE_TYPE);
            profileOwn = bundle.getBoolean(Params.PROFILE_OWN);
            if (bundle.containsKey(Params.ID))
                profileId = bundle.getInt(Params.ID);
            else if (bundle.containsKey(Params.NAME))
                profileUsername = bundle.getString(Params.NAME);
        } else {
            Log.e(TAG, "bundle is null!!");
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
        }

        pd.show();

        if (profileType == Params.ProfileType.PROFILE_USER) {
            //get user home info

            //TODO remove test part
            //get user home info by sending user_id
            if (profileId != 0) {

                new GetUserHomeInfo(profileId, LoginInfo.getUserId(cont), webserviceResponse).execute();
                runningWebserviceType = RunningWebserviceType.getUserHomeInfo;
            } else {
                // TODO :: GET with username
            }
        } else {
            //get business home info

            // TODO ::
            if (profileId != 0) {
                new GetBusinessHomeInfo(profileId, LoginInfo.getUserId(cont), FragmentProfile.this).execute();
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

        header = getActivity().getLayoutInflater().inflate(R.layout.fragment_profile_header, null);

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
        header.findViewById(R.id.btn_profile_search).setOnClickListener(new View.OnClickListener() {
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
            // TODO:: if are not friends, show lock!! (uncommment)
            //if(profile_user.friendshipRelationStatus== FriendshipRelation.Status.FRIEND)
            //header.findViewById(R.id.img_profile_lock).setVisibility(View.VISIBLE);
            ((ImageView) header.findViewById(R.id.img_profile_option3)).setImageResource(R.drawable.ic_menu_businesses);
            // FRIENDS
            header.findViewById(R.id.ll_profile_option1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO:: if are friends (uncomment)
                    //if(profile_user.friendshipRelationStatus== FriendshipRelation.Status.FRIEND)
                    if (profile_user.friendsNumber > 0) {
                        InnerFragment innerFragment = new InnerFragment(getActivity());
                        innerFragment.newFriends(profileId, profile_user.friendRequestNumber);
                    }
                }
            });
            // REVIEWS OF USER
            header.findViewById(R.id.ll_profile_option2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (profile_user.reviewsNumber > 0) {
                        InnerFragment innerFragment = new InnerFragment(getActivity());
                        innerFragment.newPrfoileReviews(profileId);
                    }
                }
            });
            // BUSINESSES OF USER
            header.findViewById(R.id.ll_profile_option3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO:: if are friends ( uncomment )
                    //if(profile_user.friendshipRelationStatus== FriendshipRelation.Status.FRIEND)
                    if (profile_user.followedBusinessesNumber > 0) {
                        InnerFragment innerFragment = new InnerFragment(getActivity());
                        innerFragment.newBusinessesFragment(profileId);
                    }
                }
            });
        }
        // BUSINESS
        if (profileType == Params.ProfileType.PROFILE_BUSINESS) {
            //header.findViewById(R.id.txt_profile_status).setVisibility(View.GONE);
            header.findViewById(R.id.ratingBar_profile).setVisibility(View.VISIBLE);
            header.findViewById(R.id.txt_profile_status).setVisibility(View.INVISIBLE);
            ((ImageView) header.findViewById(R.id.img_profile_option3)).setImageResource(R.drawable.ic_menu_call);
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
                    if (profile_business.reviewsNumber > 0) {
                        InnerFragment innerFragment = new InnerFragment(getActivity());
                        innerFragment.newReviews(profileId, profile_business.userID);
                    }
                }
            });
            // FOLLOWERS
            header.findViewById(R.id.ll_profile_option1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (profile_business.followersNumber > 0) { // TODO:: what if we have blockeds without any followers ??
                        InnerFragment innerFragment = new InnerFragment(getActivity());
                        innerFragment.newFollowers(profileId, profileOwn);
                    }
                }
            });
        }

        uPosts = new ArrayList<>();
        bPosts = new ArrayList<>();

        //TODO posts is empty. Why did you pass it to the adatper class?!!!!
        //listAdapter = new PostsAdapter(getActivity(), posts, webserviceResponse, FragmentProfile.this);
        //gridAdapter = new ProfilePostsGridAdapter(getActivity(), posts);

        //TODO why you set adapter here while you DO NOT HAVE DATA here.
        //TODO data are given in getResult not here
        //grid.setAdapter(gridAdapter);
    }

    void sendFriendRequest() {
        follow_friend_request_sent = true;
        new RequestFriendship(LoginInfo.getUserId(getActivity()), profileId, FragmentProfile.this).execute();
        pd.show();
    }

    void sendUnfriendRequest() {
        follow_friend_request_sent = true;
        new RequestCancelFriendship(LoginInfo.getUserId(getActivity()), profileId, FragmentProfile.this).execute();
        pd.show();
    }


    void sendFollowRequest() {
        follow_friend_request_sent = true;
        new FollowBusiness(LoginInfo.getUserId(cont), profileId, FragmentProfile.this).execute();
        pd.show();
    }

    void sendUnfollowRequest() {
        follow_friend_request_sent = true;
        new UnFollowBusiness(LoginInfo.getUserId(cont), profileId, FragmentProfile.this).execute();
        pd.show();
    }

    void myOwnProfile() {
        ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.profile_edit_profile);
        // EDIT PROFILE
        header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityUserProfileEdit.class);
                //startActivity(intent);
                startActivityForResult(intent, Params.ACTION_USER_EDIT_PROFILE);
                getActivity().overridePendingTransition(R.anim.to_0, R.anim.to_left);
            }
        });
        ((ActivityMain) getActivity()).permission = profile_user.permissions;
        ((ActivityMain) getActivity()).businesses(profile_user);
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
                //startActivityForResult(intent,Params.ACTION_DELETE_BUSIENSS);
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
                if (isLoadingMore) {
                    swipeView.setRefreshing(false);
                    return;
                }
                getAgain();
                profile_user = new User();
                profile_business = new Business();
                new GetUserHomeInfo(profileId, LoginInfo.getUserId(cont), webserviceResponse).execute();
                runningWebserviceType = RunningWebserviceType.getUserHomeInfo;
                swipeView.setRefreshing(true);
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
                    if (profileType == Params.ProfileType.PROFILE_USER) {
                        if (!swipeView.isRefreshing() && !isLoadingMore && uPosts.size() > 0 && uPosts.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                            loadMoreData();
                        }
                    } else if (profileType == Params.ProfileType.PROFILE_BUSINESS) {
                        if (!swipeView.isRefreshing() && !isLoadingMore && bPosts.size() > 0 && bPosts.size() % getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                            loadMoreData();
                        }
                    }


                   /* if (!swipeView.isRefreshing() && !isLoadingMore && posts.size() > 0) {
                        loadMoreData();
                    }*/
                }
            }
        });
    }

    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        if (profileType == Params.ProfileType.PROFILE_USER) {
            //new GetSharedPosts(LoginInfo.getUserId(cont), posts.get(posts.size() - 1).id, cont.getResources().getInteger(R.integer.lazy_load_limitation), FragmentProfile.this).execute();
            new GetSharedPosts(LoginInfo.getUserId(cont), uPosts.get(uPosts.size() - 1).id, cont.getResources().getInteger(R.integer.lazy_load_limitation), FragmentProfile.this).execute();
            runningWebserviceType = RunningWebserviceType.getUserPosts;
        } else {
            //new GetBusinessPosts(LoginInfo.getUserId(cont), profileId, posts.get(posts.size() - 1).id, cont.getResources().getInteger(R.integer.lazy_load_limitation), FragmentProfile.this).execute();
            new GetBusinessPosts(LoginInfo.getUserId(cont), profileId, uPosts.get(uPosts.size() - 1).id, cont.getResources().getInteger(R.integer.lazy_load_limitation), FragmentProfile.this).execute();
            runningWebserviceType = RunningWebserviceType.getBustinessPosts;
        }
        isLoadingMore = true;
        listFooterView.setVisibility(View.VISIBLE);
    }

    public void checkDrawerLock() {
        ((ActivityMain) getActivity()).rightDrawer();
    }

    @Override
    public void getResult(Object result) {
        try {

            if (result instanceof ResultStatus) {
                pd.dismiss();
                //delete post,follow business

                if (follow_friend_request_sent) {
                    if (profileType == Params.ProfileType.PROFILE_BUSINESS) {
                        if(profile_business.isFollowing)
                            profile_business.isFollowing = false;
                        else
                            profile_business.isFollowing = true;
                        refreshFollowButton();
                    } else {
                        if(profile_user.friendshipRelationStatus== FriendshipRelation.Status.NOT_FRIEND)
                            profile_user.friendshipRelationStatus = FriendshipRelation.Status.REQUEST_SENT;
                        else
                            profile_user.friendshipRelationStatus = FriendshipRelation.Status.NOT_FRIEND;
                        refreshFriendButton();
                    }
                    follow_friend_request_sent = false;
                }

            } else if (result instanceof User) {
                //get visited user home info
                profile_user = (User) result;

                profileType = Params.ProfileType.PROFILE_USER;

                assignNow();

                //pd.dismiss();
                new GetSharedPosts(LoginInfo.getUserId(cont), 0, cont.getResources().getInteger(R.integer.lazy_load_limitation), FragmentProfile.this).execute();
                runningWebserviceType = RunningWebserviceType.getUserPosts;

            } else if (result instanceof ArrayList) {

                pd.dismiss();

                ArrayList<Post> temp = new ArrayList<>();
                if (runningWebserviceType == RunningWebserviceType.getUserPosts) {
                    temp.addAll(uPosts);
                    temp.addAll((ArrayList<Post>) result);
                    uPosts.clear();
                    uPosts.addAll(temp);
                    initialAdapters(uPosts);

                } else if (runningWebserviceType == RunningWebserviceType.getBustinessPosts) {
                    temp.addAll(bPosts);
                    temp.addAll((ArrayList<Post>) result);
                    bPosts.clear();
                    bPosts.addAll(temp);
                    initialAdapters(bPosts);

                }

                isLoadingMore = false;
                swipeView.setRefreshing(false);
                listFooterView.setVisibility(View.GONE);

            } else if (result instanceof Business) {
                //business home info

                profile_business = (Business) result;
                profileType = Params.ProfileType.PROFILE_BUSINESS;

                header.findViewById(R.id.btn_profile_search).setVisibility(View.GONE);

                assignNow();

                new GetBusinessPosts(LoginInfo.getUserId(cont), profile_business.id, 0, cont.getResources().getInteger(R.integer.lazy_load_limitation), FragmentProfile.this).execute();
                runningWebserviceType = RunningWebserviceType.getBustinessPosts;

            } else pd.dismiss();

        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    private void initialAdapters(ArrayList<Post> posts) {
        if (listAdapter == null) {
            listAdapter = new PostsAdapter(getActivity(), posts, webserviceResponse, FragmentProfile.this, pd);
        } else
            listAdapter.notifyDataSetChanged();

        if (gridAdapter == null) {
            gridAdapter = new ProfilePostsGridAdapter(getActivity(), posts);
            grid.setAdapter(gridAdapter);
        } else
            gridAdapter.notifyDataSetChanged();
    }

    @Override
    public void getError(Integer errorCode) {
        editingId=0;
        if (bPosts.size() == 0 && uPosts.size() == 0)
            initialAdapters(bPosts);
        pd.dismiss();
        try {
            String errorMessage = ServerAnswer.getError(getActivity(), errorCode);
            Dialogs.showMessage(getActivity(), errorMessage, false);
            pd.dismiss();
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    private void assignNow() {
        if (profileType == Params.ProfileType.PROFILE_BUSINESS) {
            if (profile_business.profilePictureId != 0) {
                downloadImages.download(profile_business.profilePictureId, Image_M.getImageSize(Image_M.ImageSize.MEDIUM), (ImageViewCircle) header.findViewById(R.id.img_profile_pic));
                //throws exception: calss cast exception : imageViewCircle to imageViewCover
                //1downloadImages.download(profile_business.profilePictureId, Image_M.getImageSize(Image_M.ImageSize.LARGE), (ImageViewCircle) header.findViewById(R.id.img_profile_cover));
                downloadImages.download(profile_business.profilePictureId, Image_M.getImageSize(Image_M.ImageSize.LARGE), (ImageViewCover) header.findViewById(R.id.img_profile_cover));


            }

            ((TextViewFont) header.findViewById(R.id.txt_profile_name)).setText(profile_business.businessUserName);
            ((TextViewFont) header.findViewById(R.id.txt_profile_status)).setText(profile_business.description);
            ((RatingBar) header.findViewById(R.id.ratingBar_profile)).setRating(profile_business.rate);
            ((TextViewFont) header.findViewById(R.id.txt_profile_option1)).setText(profile_business.followersNumber + " " + getString(R.string.followers_num));
            ((TextViewFont) header.findViewById(R.id.txt_profile_option2)).setText(profile_business.reviewsNumber + " " + getString(R.string.review));
            ((TextViewFont) header.findViewById(R.id.txt_profile_option3)).setText(R.string.call_info);
            // MY OWN BUSINESS
            if (profileOwn) {
                myOwnBusiness();
            } else { // SOMEONE'S BUSINESS
                refreshFollowButton();
            }
        } else if (profileType == Params.ProfileType.PROFILE_USER) {
            assignUserProfileInfo();
        }
    }

    public void assignUserProfileInfo() {
        if (profile_user.profilePictureId != 0) {
            downloadImages.download(profile_user.profilePictureId, Image_M.getImageSize(Image_M.ImageSize.MEDIUM), (ImageViewCircle) header.findViewById(R.id.img_profile_pic));
            downloadImages.download(profile_user.profilePictureId, Image_M.getImageSize(Image_M.ImageSize.LARGE), (ImageViewCover) header.findViewById(R.id.img_profile_cover));
        }
        ((TextViewFont) header.findViewById(R.id.txt_profile_name)).setText(profile_user.name);
        ((TextViewFont) header.findViewById(R.id.txt_profile_status)).setText(profile_user.aboutMe);
        ((TextViewFont) header.findViewById(R.id.txt_profile_option1)).setText(profile_user.friendsNumber + " " + getString(R.string.friend));
        ((TextViewFont) header.findViewById(R.id.txt_profile_option2)).setText(profile_user.reviewsNumber + " " + getString(R.string.review));
        ((TextViewFont) header.findViewById(R.id.txt_profile_option3)).setText(profile_user.followedBusinessesNumber + " " + getString(R.string.business));
        // MY OWN USER'S PROFILE
        if (profileOwn == true) {
            myOwnProfile();
        } else { // SOMEONE'S PROFILE
            refreshFriendButton();
        }
    }

    public void getAgain() {
        profile_user = new User();
        profile_business = new Business();
        new GetUserHomeInfo(profileId, LoginInfo.getUserId(cont), webserviceResponse).execute();
        runningWebserviceType = RunningWebserviceType.getUserHomeInfo;
        swipeView.setRefreshing(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Params.ACTION_USER_EDIT_PROFILE) {
                String profilePictureFilePath = data.getStringExtra(Params.USER_PROFILE_PICTURE);
                String userName = data.getStringExtra(Params.USER_NAME);
                String aboutMe = data.getStringExtra(Params.ABOUT_ME);

                if (!profilePictureFilePath.equals("null"))
                    ((ImageViewCircle) header.findViewById(R.id.img_profile_pic)).setImageBitmap(Image_M.readBitmapFromStorate(profilePictureFilePath));
                ((TextViewFont) header.findViewById(R.id.txt_profile_name)).setText(userName);
                //TODO assign aboutMe
            }

        }
    }

    public void showBusinessUnfollowPopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(R.string.unfollow_business)
                .setMessage(R.string.popup_unfollow_business)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendUnfollowRequest();
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        Dialogs.showCustomizedDialog(getActivity(), builder);
    }

    public void showUserUnfriendPopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(R.string.delete_friend)
                .setMessage(R.string.popup_delete_friend)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendUnfollowRequest();
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        Dialogs.showCustomizedDialog(getActivity(), builder);
    }

    private void refreshFollowButton() {
        if (profile_business.isFollowing) {
            ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.following);
            header.findViewById(R.id.btn_profile_on_picture).setBackgroundResource(R.color.green_dark);
            header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showBusinessUnfollowPopup();
                }
            });
        } else {
            header.findViewById(R.id.btn_profile_on_picture).setBackgroundResource(R.drawable.style_textview_on_pic);
            ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.follow_request);
            header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendFollowRequest();
                }
            });
        }
    }

    private void refreshFriendButton() {
        if (profile_user.friendshipRelationStatus == FriendshipRelation.Status.NOT_FRIEND) {
            header.findViewById(R.id.btn_profile_on_picture).setBackgroundResource(R.drawable.style_textview_on_pic);
            ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.friend_request);
            // FRIEND REQUEST
            header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendFriendRequest();
                }
            });
        } else if (profile_user.friendshipRelationStatus == FriendshipRelation.Status.FRIEND) {
            ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.your_friend);
            header.findViewById(R.id.btn_profile_on_picture).setBackgroundResource(R.color.green_dark);
            header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUserUnfriendPopup();
                }
            });
        } else if (profile_user.friendshipRelationStatus == FriendshipRelation.Status.REQUEST_SENT) {
            ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.request_sent);
            header.findViewById(R.id.btn_profile_on_picture).setBackgroundResource(R.color.button_on_dark);
            header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(null);
        }
    }
}
