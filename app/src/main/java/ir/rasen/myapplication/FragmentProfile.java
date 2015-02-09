package ir.rasen.myapplication;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ListAdapter;
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
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.DownloadImages;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.GetBusinessHomeInfo;
import ir.rasen.myapplication.webservice.friend.RequestFriendship;
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

    private boolean follow_friend_request_sent=false;

    @Override
    public void setEditing(int id, String text, Dialog dialog) {
        editingId = id;
        editingText = text;
        editingDialog = dialog;
    }

    private enum RunningWebserviceType {getUserHomeInfo, getUserPosts, getBustinessPosts, getBusinessHomeInfo}

    private static RunningWebserviceType runningWebserviceType;

    private ArrayList<Post> posts;

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
        downloadImages = new DownloadImages(getActivity());
        pd=new ProgressDialogCustom(getActivity());
        downloadImages = new DownloadImages(getActivity());

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

        pd.show();

        if (profileType==Params.ProfileType.PROFILE_USER) {
            //get user home info

            //TODO remove test part
            //get user home info by sending user_id
            if(profileId!=0) {

                //TODO for the test
                new GetUserHomeInfo(profileId,LoginInfo.getUserId(cont), webserviceResponse).execute();
                runningWebserviceType = RunningWebserviceType.getUserHomeInfo;
            } else {
                // TODO :: GET with username
            }
        } else {
            //get business home info

            // TODO ::
            if(profileId!=0) {
                new GetBusinessHomeInfo(profileId,LoginInfo.getUserId(cont), FragmentProfile.this).execute();
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
            ((ImageView) header.findViewById(R.id.img_profile_option3)).setImageResource(R.drawable.ic_menu_businesses);
            // FRIENDS
            header.findViewById(R.id.ll_profile_option1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InnerFragment innerFragment = new InnerFragment(getActivity());
                    innerFragment.newFriends(profileId, profile_user.friendRequestNumber);
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
        }
        // BUSINESS
        if (profileType == Params.ProfileType.PROFILE_BUSINESS) {
            header.findViewById(R.id.txt_profile_status).setVisibility(View.GONE);
            header.findViewById(R.id.ratingBar_profile).setVisibility(View.VISIBLE);
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
                    InnerFragment innerFragment = new InnerFragment(getActivity());
                    innerFragment.newReviews(profileId, profile_business.userID); // TODO:: USERID is owner, RIGHT ?
                }
            });
            // FOLLOWERS
            header.findViewById(R.id.ll_profile_option1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InnerFragment innerFragment = new InnerFragment(getActivity());
                    innerFragment.newFollowers(profileId, profileOwn);
                }
            });
        }

        // TODO: Change Adapter to display your content
        posts = new ArrayList<>();

        listAdapter = new PostsAdapter(getActivity(), posts, webserviceResponse, FragmentProfile.this);
        gridAdapter = new ProfilePostsGridAdapter(getActivity(), posts);
        grid.setAdapter(gridAdapter);
    }

    void sendFriendRequest() {
        follow_friend_request_sent = true;
        new RequestFriendship(LoginInfo.getUserId(getActivity()), profileId, FragmentProfile.this).execute();
    }

    void sendFollowRequest() {
        follow_friend_request_sent = true;
        new FollowBusiness(LoginInfo.getUserId(cont), profileId, FragmentProfile.this).execute();
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
                profile_user = new User();
                profile_business = new Business();
                posts = new ArrayList<>();
                // TODO get data again
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
                    if (!swipeView.isRefreshing() && !isLoadingMore && posts.size()>0) {
                        loadMoreData();
                    }
                }
            }
        });
    }

    public void loadMoreData() {
        // LOAD MORE DATA HERE...
        if (profileType==Params.ProfileType.PROFILE_USER) {
            new GetSharedPosts(LoginInfo.getUserId(cont), posts.get(posts.size()-1).id, cont.getResources().getInteger(R.integer.lazy_load_limitation), FragmentProfile.this).execute();
            runningWebserviceType = RunningWebserviceType.getUserPosts;
        } else {
            new GetBusinessPosts(LoginInfo.getUserId(cont),profileId, posts.get(posts.size()-1).id, cont.getResources().getInteger(R.integer.lazy_load_limitation), FragmentProfile.this).execute();
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
        pd.dismiss();
        try {

            if (result instanceof ResultStatus) {
                //delete post,follow business
                if(follow_friend_request_sent) {
                    if(profileType==Params.ProfileType.PROFILE_USER) {
                        if (profileType == Params.ProfileType.PROFILE_BUSINESS) {
                            ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.request_sent);
                            header.findViewById(R.id.btn_profile_on_picture).setBackgroundResource(R.color.button_on_dark);
                        } else {
                            ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.following);
                            header.findViewById(R.id.btn_profile_on_picture).setBackgroundResource(R.color.green_dark);
                        }
                    }
                }

            } else if (result instanceof User) {
                //get visited user home info
                profile_user = (User) result;

                profileType = Params.ProfileType.PROFILE_USER;

                assignNow();

                if (profileType==Params.ProfileType.PROFILE_USER) {
                    new GetSharedPosts(LoginInfo.getUserId(cont), 0, cont.getResources().getInteger(R.integer.lazy_load_limitation), FragmentProfile.this).execute();
                    runningWebserviceType = RunningWebserviceType.getUserPosts;
                } else {
                    new GetBusinessPosts(LoginInfo.getUserId(cont),profileId, 0, cont.getResources().getInteger(R.integer.lazy_load_limitation), FragmentProfile.this).execute();
                    runningWebserviceType = RunningWebserviceType.getBustinessPosts;
                }

            } else if (result instanceof ArrayList) {

                pd.dismiss();

                //TODO assign
                if (runningWebserviceType == RunningWebserviceType.getUserPosts) {
                    //user shared posts
                    ArrayList<Post> temp = posts;
                    temp.addAll((ArrayList<Post>) result);
                    posts.clear();
                    posts.addAll(temp);
                } else if (runningWebserviceType == RunningWebserviceType.getBustinessPosts) {
                    //business posts
                    ArrayList<Post> temp = posts;
                    temp.addAll((ArrayList<Post>) result);
                    posts.clear();
                    posts.addAll(temp);
                }
                listAdapter.notifyDataSetChanged();
                gridAdapter.notifyDataSetChanged();
                isLoadingMore=false;
                swipeView.setRefreshing(false);
                listFooterView.setVisibility(View.GONE);

            } else if (result instanceof Business) {
                //business home info

                profile_business = (Business)result;


                profileType = Params.ProfileType.PROFILE_BUSINESS;

                assignNow();

                new GetBusinessPosts(LoginInfo.getUserId(cont),profile_business.id, 0, cont.getResources().getInteger(R.integer.lazy_load_limitation), FragmentProfile.this).execute();
                runningWebserviceType = RunningWebserviceType.getBustinessPosts;

            }

        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        pd.dismiss();
        try {
            String errorMessage = ServerAnswer.getError(getActivity(), errorCode);
            Dialogs.showMessage(getActivity(), errorMessage);
            pd.dismiss();
        } catch(Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    private void assignNow() {
        if (profileType == Params.ProfileType.PROFILE_BUSINESS) {
            if(profile_business.profilePictureId != 0)

                downloadImages.download(profile_business.profilePictureId,Image_M.getImageSize(Image_M.ImageSize.MEDIUM),(ImageViewCircle) header.findViewById(R.id.img_profile_pic));
           /* if(profile_business.profilePicture.length()>0)
                ((ImageViewCircle) header.findViewById(R.id.img_profile_pic)).setImageBitmap(Image_M.getBitmapFromString(profile_business.profilePicture));*/

            /*if(profile_business.coverPicture.length()>0)
                ((ImageViewCircle) header.findViewById(R.id.img_profile_cover)).setImageBitmap(Image_M.getBitmapFromString(profile_business.coverPicture));*/

            ((TextViewFont) header.findViewById(R.id.txt_profile_name)).setText(profile_business.businessUserName);
            ((RatingBar) header.findViewById(R.id.ratingBar_profile)).setRating(profile_business.rate);
            ((TextViewFont) header.findViewById(R.id.txt_profile_option1)).setText(profile_business.followersNumber + " " + getString(R.string.followers_num));
            ((TextViewFont) header.findViewById(R.id.txt_profile_option2)).setText(profile_business.reviewsNumber + " " + getString(R.string.review));
            ((TextViewFont) header.findViewById(R.id.txt_profile_option3)).setText(R.string.call_info);
            // MY OWN BUSINESS
            if (profileOwn) {
                myOwnBusiness();
            } else { // SOMEONE'S BUSINESS
                if(profile_business.isFollowing) {
                    ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.following);
                    header.findViewById(R.id.btn_profile_on_picture).setBackgroundResource(R.color.green_dark);
                } else {
                    ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.follow_request);
                    // FRIEND REQUEST
                    header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendFollowRequest();
                        }
                    });
                }
            }
        } else if (profileType == Params.ProfileType.PROFILE_USER) {
            if(profile_user.profilePicture.length()>0)
                ((ImageViewCircle) header.findViewById(R.id.img_profile_pic)).setImageBitmap(Image_M.getBitmapFromString(profile_user.profilePicture));
            if(profile_user.coverPicture.length()>0)
                ((ImageViewCircle) header.findViewById(R.id.img_profile_cover)).setImageBitmap(Image_M.getBitmapFromString(profile_user.coverPicture));
            ((TextViewFont) header.findViewById(R.id.txt_profile_name)).setText(profile_user.name);
            ((TextViewFont) header.findViewById(R.id.txt_profile_status)).setText(profile_user.aboutMe);
            ((TextViewFont) header.findViewById(R.id.txt_profile_option1)).setText(profile_user.friendsNumber + " " + getString(R.string.friend));
            ((TextViewFont) header.findViewById(R.id.txt_profile_option2)).setText(profile_user.reviewsNumber + " " + getString(R.string.review));
            ((TextViewFont) header.findViewById(R.id.txt_profile_option3)).setText(profile_user.followedBusinessesNumber + " " + getString(R.string.business));
            // MY OWN USER'S PROFILE
            if (profileOwn == true) {
                myOwnProfile();
            } else { // SOMEONE'S PROFILE
                if (profile_user.friendshipRelationStatus == FriendshipRelation.Status.NOT_FRIEND) {
                    ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.friend_request);
                    // FRIEND REQUEST
                    header.findViewById(R.id.btn_profile_on_picture).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendFriendRequest();
                        }
                    });
                } else if(profile_user.friendshipRelationStatus== FriendshipRelation.Status.FRIEND) {
                    ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.your_friend);
                    header.findViewById(R.id.btn_profile_on_picture).setBackgroundResource(R.color.green_dark);
                } else if(profile_user.friendshipRelationStatus== FriendshipRelation.Status.REQUEST_SENT) {
                    ((TextViewFont) header.findViewById(R.id.btn_profile_on_picture)).setText(R.string.request_sent);
                    header.findViewById(R.id.btn_profile_on_picture).setBackgroundResource(R.color.button_on_dark);
                }
            }
        }
    }
}
