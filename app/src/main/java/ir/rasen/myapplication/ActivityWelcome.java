package ir.rasen.myapplication;

import android.content.Intent;
import android.graphics.Color;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ir.rasen.myapplication.adapters.UsersBusinessesAdapter;
import ir.rasen.myapplication.alarm.AlarmReciever;
import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.MyNotification;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.ViewPagerPaging;

/**
 * Created by 'Sina KH'.
 */

public class ActivityWelcome extends FragmentActivity {

    private static int FRAGMENTS_COUNT = 4;
    public ViewPagerPaging pager;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ImageView page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if(LoginInfo.isLoggedIn(this)) {
            gotoActivity(ActivityMain.class);
            finish();
        }

        page = (ImageView) findViewById(R.id.img_welcome_page);

        pager = (ViewPagerPaging) findViewById(R.id.pager_main);
        fragmentPagerAdapter = new fragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(fragmentPagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int fragmentNumber) {
                // SET COLOR OF BUTTONS
                switch (fragmentNumber) {
                    case 0:
                        page.setImageResource(R.drawable.page_1);
                        break;
                    case 1:
                        page.setImageResource(R.drawable.page_2);
                        break;
                    case 2:
                        page.setImageResource(R.drawable.page_3);
                        break;
                    case 3:
                        page.setImageResource(R.drawable.page_4);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        pager.setCurrentItem(0);
        page.setImageResource(R.drawable.page_1);

        if(getIntent().getExtras()!=null) {
            if(getIntent().getExtras().containsKey(Params.WELCOME_PAGE)) {
                pager.setCurrentItem(getIntent().getIntExtra(Params.WELCOME_PAGE, 0));
            }
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
            if(fragmentNumber<3)
                return new FragmentWelcome().newInstance(fragmentNumber);
            else
                return new FragmentWelcome_Getin().newInstance();
        }
    }

    public void back(View v) {
        onBackPressed();
    }
    // ON BACK PRESSED
    public void onBackPressed() {
        if(pager.getCurrentItem()>0) {
            pager.setCurrentItem(pager.getCurrentItem()-1);
        } else {
            finish();
            overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
        }
    }

    public void onResume() {
        super.onResume();
        if(LoginInfo.isLoggedIn(this)) {
            finish();
        }
    }

    // LOGIN TOUCHED
    public void login(View view) {
        gotoActivity(ActivityLogin.class);
    }

    // REGISTER TOUCHED
    public void register(View view) {
        gotoActivity(ActivityRegister.class);
    }

    public void gotoActivity(Class targetClass) {
        Intent intent = new Intent(getBaseContext(), targetClass);
        startActivity(intent);
        overridePendingTransition(R.anim.to_0, R.anim.to_left);
    }

}
