package ir.rasen.myapplication.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import ir.rasen.myapplication.ActivityMain;
import ir.rasen.myapplication.FragmentBusinesses;
import ir.rasen.myapplication.FragmentCallInfo;
import ir.rasen.myapplication.FragmentComments;
import ir.rasen.myapplication.FragmentFollowers;
import ir.rasen.myapplication.FragmentFriends;
import ir.rasen.myapplication.FragmentHome;
import ir.rasen.myapplication.FragmentProfile;
import ir.rasen.myapplication.FragmentResults;
import ir.rasen.myapplication.FragmentSearchUsers;
import ir.rasen.myapplication.FragmentUserReviews;
import ir.rasen.myapplication.FragmentReviews;
import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Post;

/**
 * Created by 'Sina KH' on 01/12/2015.
 */
public class InnerFragment {

    Context context;
    int fragPlace;

    public InnerFragment(Context context) {
        this.context = context;
        this.fragPlace = ((ActivityMain) context).pager.getCurrentItem();
    }

    // add new profile fragment to current place
    public void newProfile(int profileType, boolean owner) {
        Fragment newFragment = new FragmentProfile().newInstance(profileType, owner);
        addFragment(newFragment);
    }

    // add new comments fragment to current place
    public void newComments(String userId, String postId) {
        Fragment newFragment = new FragmentComments();
        addFragment(newFragment);
    }

    // add new friends fragment to current place
    public void newFriends(String userId) {
        Fragment newFragment = new FragmentFriends().newInstance(userId);
        addFragment(newFragment);
    }

    // add new followers fragment to current place
    public void newFollowers(String businessId) {
        Fragment newFragment = new FragmentFollowers().newInstance(businessId);
        addFragment(newFragment);
    }

    // add new call info fragment to current place
    public void newCallInfo(String businessId) {
        Fragment newFragment = new FragmentCallInfo().newInstance(businessId);
        addFragment(newFragment);
    }

    // add new reviews fragment to current place
    public void newReviews(String businessId) {
        Fragment newFragment = new FragmentReviews().newInstance();
        addFragment(newFragment);
    }

    // add new profile reviews fragment to current place
    public void newPrfoileReviews(String userId) {
        Fragment newFragment = new FragmentUserReviews().newInstance();
        addFragment(newFragment);
    }

    // add new home fragment to current place
    public void newHomeFragment() {
        Fragment newFragment = new FragmentHome().newInstance();
        addFragment(newFragment);
    }

    // add new post fragment to current place
    public void newPostFragment(Post post) {
        ArrayList<Post> posts = new ArrayList<>();
        posts.add(0, post);
        PassingPosts.getInstance().setValue(posts);
        Fragment newFragment = new FragmentHome().newInstance(post.title);
        addFragment(newFragment);
    }

    // add new businesses fragment to current place
    public void newBusinessesFragment(String userId) {
        Fragment newFragment = new FragmentBusinesses().newInstance(userId);
        addFragment(newFragment);
    }

    // new results fragment
    public void newResultsFragment(String searchString, String category, boolean nearby, Location_M location_m) {
        Fragment newFragment = new FragmentResults().newInstance(searchString, category, nearby, location_m);
        addFragment(newFragment);
    }

    // new search users fragment
    public void newSearchUsers() {
        Fragment newFragment = new FragmentSearchUsers().newInstance();
        addFragment(newFragment);
    }

    // add and show created fragment
    private void addFragment(Fragment newFragment) {
        ((ActivityMain) context).lockDrawers();
        ((ActivityMain) context).fragCount[fragPlace]++;
        FragmentTransaction ft = ((ActivityMain) context).getSupportFragmentManager().beginTransaction();
        if(fragPlace== Params.HOME)
            ft.add(R.id.rl_home,newFragment, fragPlace+"."+((ActivityMain) context).fragCount[fragPlace]);
        else if(fragPlace==Params.SEARCH)
            ft.add(R.id.rl_search, newFragment, fragPlace + "." + ((ActivityMain) context).fragCount[fragPlace]);
        else if(fragPlace==Params.PROFILE)
            ft.add(R.id.rl_profile, newFragment, fragPlace + "." + ((ActivityMain) context).fragCount[fragPlace]);
        ft.commit();
    }

}
