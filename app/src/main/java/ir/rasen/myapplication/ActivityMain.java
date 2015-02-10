package ir.rasen.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.UsersBusinessesAdapter;
import ir.rasen.myapplication.alarm.Alarm_M;
import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.MyNotification;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.helper.Permission;
import ir.rasen.myapplication.helper.Results;
import ir.rasen.myapplication.ui.ViewPagerPaging;

/**
 * Created by 'Sina KH'.
 */

public class ActivityMain extends FragmentActivity {
//

    public static ActivityMain activityMain;

    private static int FRAGMENTS_COUNT = 3;
    public int fragCount[];
    public ViewPagerPaging pager;
    private BaseAdapter mAdapter;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ImageButton btnHome, btnSearch, btnProfile;

    private DrawerLayout drawerLayout;
    private RelativeLayout drawerLayoutRight;

    public Permission permission;

    boolean drawerIsShowing = false;

    private ArrayList<Business> businesses = new ArrayList<Business>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMain = this;

        //set alarm manager to run GetLastCommentNotification periodically
        Alarm_M alarm_m = new Alarm_M();
        //TODO uncomment
        //alarm_m.set(this);

        btnHome = (ImageButton) findViewById(R.id.btn_main_home);
        btnSearch = (ImageButton) findViewById(R.id.btn_main_search);
        btnProfile = (ImageButton) findViewById(R.id.btn_main_profile);

        fragCount = new int[3];

        // DRAWER VIEWS
        drawerViews();

        pager = (ViewPagerPaging) findViewById(R.id.pager_main);
        pager.setPaging(false);
        fragmentPagerAdapter = new fragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(fragmentPagerAdapter);
        pager.setOffscreenPageLimit(3);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int fragmentNumber) {
                // SET COLOR OF BUTTONS
                btnHome.setBackgroundResource(R.drawable.selected_tab_u);
                btnSearch.setBackgroundResource(R.drawable.selected_tab_u);
                btnProfile.setBackgroundResource(R.drawable.selected_tab_u);
                switch (fragmentNumber) {
                    case 0:
                        btnHome.setBackgroundResource(R.color.text_details);
                        lockDrawers();
                        break;
                    case 1:
                        btnSearch.setBackgroundResource(R.color.text_details);
                        lockDrawers();
                        break;
                    case 2:
                        btnProfile.setBackgroundResource(R.color.text_details);
                        checkDrawerLock();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        lockDrawers();
        pager.setCurrentItem(0);

        // TODO: Change Adapter to display user's business list
        businesses = new ArrayList<Business>();
        mAdapter = new UsersBusinessesAdapter(ActivityMain.this, businesses);
        ((AdapterView<ListAdapter>) findViewById(R.id.list_drawer)).setAdapter(mAdapter);

    }

    public void home(View v) {
        // TODO: GOTO HOME FRAGMENT
        if (pager.getCurrentItem() != 0)
            pager.setCurrentItem(0);
        else toRoot();
    }

    public void search(View v) {
        // TODO: GOTO SEARCH FRAGMENT
        if (pager.getCurrentItem() != 1)
            pager.setCurrentItem(1);
        else toRoot();
    }

    public void profile(View v) {
        // TODO: GOTO PROFILE FRAGMENT
        if (pager.getCurrentItem() != 2)
            pager.setCurrentItem(2);
        else toRoot();
    }

    void toRoot() {
        while (fragCount[pager.getCurrentItem()] > 0) {
            onBackPressed();
        }
    }


    // FRAGMENT PAGER ADAPTER
    public class fragmentPagerAdapter extends FragmentPagerAdapter {
        public fragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return FRAGMENTS_COUNT;
        }

        @Override
        public Fragment getItem(int fragmentNumber) {
            switch (fragmentNumber) {
                case 1:
                    return new FragmentSearch();
                case 2:
                    int userId = LoginInfo.getUserId(ActivityMain.this);
                    return new FragmentProfile().newInstance(ActivityMain.this, Params.ProfileType.PROFILE_USER, true, userId);
            }
            // CASE 0 :
            btnHome.setBackgroundResource(R.color.text_details);
            return new FragmentHome().newInstance();
        }
    }

    void openDrawer(int gravity) {
        drawerLayout.openDrawer(gravity);
        drawerIsShowing = true;
        rightDrawer();
    }

    public void closeDrawer(int gravity) {
        drawerLayout.closeDrawer(gravity);
        drawerIsShowing = false;
        lockDrawers();
    }

    public void closeDrawer(View view) {
        drawerLayout.closeDrawer(Gravity.RIGHT);
        drawerIsShowing = false;
    }

    void drawerViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_main);
        drawerLayout.setDrawerShadow(android.R.color.transparent, Gravity.RIGHT);
        drawerLayoutRight = (RelativeLayout) findViewById(R.id.rl_drawer_right);
        // NEW BUSINESS
        findViewById(R.id.ll_profile_menu_new_business).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ActivityNewBusiness_Step1.class);
                startActivityForResult(intent,Params.ACTION_ADD_NEW_BUSIENSS_SUCCESS);
                //startActivity(intent);
                overridePendingTransition(R.anim.to_0, R.anim.to_left);
            }
        });
        // SETTINGS
        findViewById(R.id.ll_profile_menu_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permission != null) {
                    Intent intent = new Intent(getBaseContext(), ActivitySettings.class);
                    intent.putExtra(Params.PERMISSION_FRIENDS, permission.friends);
                    intent.putExtra(Params.PERMISSION_REVIEWS, permission.reviews);
                    intent.putExtra(Params.PERMISSION_FOLLOWED_BUSINESSES, permission.followedBusiness);
                    startActivity(intent);
                    overridePendingTransition(R.anim.to_0, R.anim.to_left);
                }
            }
        });
        // EXIT
        findViewById(R.id.ll_profile_menu_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginInfo.logout(ActivityMain.this);
                Intent intent = new Intent(getBaseContext(), ActivityWelcome.class);
                intent.putExtra(Params.WELCOME_PAGE, 3);
                startActivity(intent);
                overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
            }
        });
    }

    public void back(View v) {
        onBackPressed();
    }

    // ON BACK PRESSED
    public void onBackPressed() {
        if (drawerIsShowing) {
            drawerLayout.closeDrawers();
            drawerIsShowing = false;
        } else if (fragCount[pager.getCurrentItem()] > 0) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(pager.getCurrentItem() + "." + fragCount[pager.getCurrentItem()]);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.to_0_from_left, R.anim.to_right);
            ft.remove(fragment);
            ft.commit();
            fragCount[pager.getCurrentItem()]--;
            checkDrawerLock();
        } else {
            finish();
            overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
        }
    }

    public void lockDrawers() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, drawerLayoutRight);
    }

    public void rightDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, drawerLayoutRight);
    }

    private void checkDrawerLock() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(pager.getCurrentItem() + "." + fragCount[pager.getCurrentItem()]);
        try {
            ((FragmentProfile) fragment).checkDrawerLock();
        } catch (Exception e) {
            lockDrawers();
        }
    }

    public void businesses(User user) {
        businesses.clear();
        for (User.UserBusinesses userBusiness : user.businesses) {
            Business business = new Business();
            business.id = userBusiness.businessId;
            business.businessUserName = userBusiness.businessUserName;
            businesses.add(business);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void removeBusiness(int id) {
        for (int i = 0; i < businesses.size(); i++)
            if (businesses.get(i).id == id) {
                businesses.remove(i);
                return;
            }
        mAdapter.notifyDataSetChanged();
    }

    public void addBusiness(Business business) {
        businesses.add(0, business);
        mAdapter.notifyDataSetChanged();
    }

    public void backToRoot() {
        while (fragCount[pager.getCurrentItem()] > 0)
            onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Params.ACTION_ADD_NEW_BUSIENSS_SUCCESS) {
                //refresh business' list with new business
                Business business = new Business();
                business.id = data.getIntExtra(Params.BUSINESS_ID, 0);
                business.businessUserName = data.getStringExtra(Params.BUSINESS_USER_NAME);
                addBusiness(business);
            }
        }
    }
}
