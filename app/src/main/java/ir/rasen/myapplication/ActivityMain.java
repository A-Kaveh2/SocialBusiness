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
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.UsersBusinessesAdapter;
import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.helper.Permission;
import ir.rasen.myapplication.ui.ViewPagerPaging;

/**
 * Created by 'Sina KH'.
 */

public class ActivityMain extends FragmentActivity {

    private static int FRAGMENTS_COUNT = 3;
    public int fragCount[];
    public ViewPagerPaging pager;
    private ListAdapter mAdapter;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ImageButton btnHome, btnSearch, btnProfile;

    private DrawerLayout drawerLayout;
    private RelativeLayout drawerLayoutRight;

    public Permission permission;

    boolean drawerIsShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHome = (ImageButton) findViewById(R.id.btn_main_home);
        btnSearch = (ImageButton) findViewById(R.id.btn_main_search);
        btnProfile = (ImageButton) findViewById(R.id.btn_main_profile);

        fragCount=new int[3];

        // DRAWER VIEWS
        drawerViews();

        pager = (ViewPagerPaging) findViewById(R.id.pager_main);
        pager.setPaging(false);
        fragmentPagerAdapter = new fragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(fragmentPagerAdapter);
        pager.setOffscreenPageLimit(3);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {}
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
            public void onPageScrollStateChanged(int i) {}
        });
        lockDrawers();
        pager.setCurrentItem(0);

        // TODO: Change Adapter to display user's business list
        ArrayList<Business> businesses = new ArrayList<Business>();

        /*
            for example, i've made some fake data to show ::
        */
        Business b1 = new Business();
        Business b2 = new Business();
        Business b3 = new Business();
        b1.name=("IRAN AIR");
        b2.name=("ترشک سازی شرق");
        b3.name=("RSEN");
        businesses.add(b1);
        businesses.add(b2);
        businesses.add(b3);

        mAdapter = new UsersBusinessesAdapter(ActivityMain.this, businesses);
        ((AdapterView<ListAdapter>) findViewById(R.id.list_drawer)).setAdapter(mAdapter);

    }

    public void home(View v) {
        // TODO: GOTO HOME FRAGMENT
        if(pager.getCurrentItem()!=0)
            pager.setCurrentItem(0);
        else toRoot();
    }
    public void search(View v) {
        // TODO: GOTO SEARCH FRAGMENT
        if(pager.getCurrentItem()!=1)
            pager.setCurrentItem(1);
        else toRoot();
    }
    public void profile(View v) {
        // TODO: GOTO PROFILE FRAGMENT
        if(pager.getCurrentItem()!=2)
            pager.setCurrentItem(2);
        else toRoot();
    }

    void toRoot() {
        while(fragCount[pager.getCurrentItem()]>0) {
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
                    return new FragmentProfile().newInstance(ActivityMain.this,Params.ProfileType.PROFILE_USER, true, userId);
            }
            // CASE 0 :
            btnHome.setBackgroundResource(R.color.text_details);
            return new FragmentHome().newInstance();
        }
    }

    void openDrawer(int gravity) {
        drawerLayout.openDrawer(gravity);
        drawerIsShowing=true;
        rightDrawer();
    }
    public void closeDrawer(int gravity) {
        drawerLayout.closeDrawer(gravity);
        drawerIsShowing=false;
        lockDrawers();
    }
    public void closeDrawer(View view) {
        drawerLayout.closeDrawer(Gravity.RIGHT);
        drawerIsShowing=false;
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
                startActivity(intent);
                overridePendingTransition(R.anim.to_0, R.anim.to_left);
            }
        });
        // SETTINGS
        findViewById(R.id.ll_profile_menu_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(permission!=null) {
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
        if(drawerIsShowing) {
            drawerLayout.closeDrawers();
            drawerIsShowing=false;
        } else if(fragCount[pager.getCurrentItem()]>0) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(pager.getCurrentItem()+"."+fragCount[pager.getCurrentItem()]);
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

    public void lockDrawers(){
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, drawerLayoutRight);
    }
    public void rightDrawer(){
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, drawerLayoutRight);
    }

    private void checkDrawerLock() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(pager.getCurrentItem()+"."+fragCount[pager.getCurrentItem()]);
        try {
            ((FragmentProfile) fragment).checkDrawerLock();
        } catch (Exception e) {
            lockDrawers();
        }
    }

}
