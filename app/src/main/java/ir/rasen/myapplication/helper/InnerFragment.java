package ir.rasen.myapplication.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import ir.rasen.myapplication.ActivityMain;
import ir.rasen.myapplication.FragmentAllComments;
import ir.rasen.myapplication.FragmentBlockeds;
import ir.rasen.myapplication.FragmentBusinesses;
import ir.rasen.myapplication.FragmentCallInfo;
import ir.rasen.myapplication.FragmentComments;
import ir.rasen.myapplication.FragmentFollowers;
import ir.rasen.myapplication.FragmentFriends;
import ir.rasen.myapplication.FragmentHome;
import ir.rasen.myapplication.FragmentProfile;
import ir.rasen.myapplication.FragmentRequests;
import ir.rasen.myapplication.FragmentResults;
import ir.rasen.myapplication.FragmentResultsUsers;
import ir.rasen.myapplication.FragmentSearch;
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
    public void newProfile(Context context,int profileType, boolean owner, int profileId) {
        Fragment newFragment = new FragmentProfile().newInstance(context,profileType, owner, profileId);
        addFragment(newFragment);
    }
    // add new profile fragment to current place ( with profile username )
    public void newProfile(Context context,int profileType, boolean owner, String username) {
        //Fragment newFragment = new FragmentProfile().newInstance(context,profileType, owner, username);
        //addFragment(newFragment);
    }

    // add new comments fragment to current place
    public void newComments(int postId) {
        Fragment newFragment = new FragmentComments().newInstance(postId);
        addFragment(newFragment);
    }

    // add new friends fragment to current place
    public void newFriends(int userId, int userFriendRequests) {
        Fragment newFragment = new FragmentFriends().newInstance(userId, userFriendRequests);
        addFragment(newFragment);
    }

    // add new followers fragment to current place
    public void newFollowers(int businessId, boolean businessOwner) {
        Fragment newFragment = new FragmentFollowers().newInstance(businessId, businessOwner);
        addFragment(newFragment);
    }

    // add new followers fragment to current place
    public void newBlockedsFragment(int businessId) {
        Fragment newFragment = new FragmentBlockeds().newInstance(businessId);
        addFragment(newFragment);
    }

    // add new call info fragment to current place
    public void newCallInfo() {
        Fragment newFragment = new FragmentCallInfo().newInstance();
        addFragment(newFragment);
    }

    // add new reviews fragment to current place
    public void newReviews(int businessId, int businessOwner) {
        Fragment newFragment = new FragmentReviews().newInstance(businessId, businessOwner);
        addFragment(newFragment);
    }

    // add new profile reviews fragment to current place
    public void newPrfoileReviews(int userId) {
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
    public void newBusinessesFragment(int userId) {
        Fragment newFragment = new FragmentBusinesses().newInstance(userId);
        addFragment(newFragment);
    }

    // new results fragment
    public void newResultsFragment(String searchString, int sucategoryId, boolean nearby, Location_M location_m, int searchType) {
        Fragment newFragment = new FragmentResults().newInstance(searchString, sucategoryId, location_m, searchType);
        addFragment(newFragment);
    }

    // new search users fragment
    public void newSearchFragment(String hashtag) {
        Fragment newFragment = new FragmentSearch().newInstance(hashtag);
        addFragment(newFragment);
    }

    // new search users fragment
    public void newSearchUsers() {
        Fragment newFragment = new FragmentSearchUsers().newInstance();
        addFragment(newFragment);
    }

    // new results fragment for users
    public void newResultsUsersFragment(String searchString) {
        Fragment newFragment = new FragmentResultsUsers().newInstance(searchString);
        addFragment(newFragment);
    }

    // new requests fragment
    public void newRequestsFragment(int userId) {
        Fragment newFragment = new FragmentRequests().newInstance(userId);
        addFragment(newFragment);
    }

    // new search users fragment
    public void newAllCommentsFragment() {
        Fragment newFragment = new FragmentAllComments().newInstance();
        addFragment(newFragment);
    }

    // add and show created fragment
    private void addFragment(Fragment newFragment) {
        ((ActivityMain) context).lockDrawers();
        ((ActivityMain) context).fragCount[fragPlace]++;
        FragmentTransaction ft = ((ActivityMain) context).getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.to_0, R.anim.to_left);
        if(fragPlace== Params.HOME)
            ft.add(R.id.rl_home,newFragment, fragPlace+"."+((ActivityMain) context).fragCount[fragPlace]);
        else if(fragPlace==Params.SEARCH)
            ft.add(R.id.rl_search, newFragment, fragPlace + "." + ((ActivityMain) context).fragCount[fragPlace]);
        else if(fragPlace==Params.PROFILE)
            ft.add(R.id.rl_profile, newFragment, fragPlace + "." + ((ActivityMain) context).fragCount[fragPlace]);
        ft.commit();
    }

}
