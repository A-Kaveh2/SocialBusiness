package ir.rasen.myapplication.classes;

import java.util.ArrayList;

import ir.rasen.myapplication.helper.Location_M;
import ir.rasen.myapplication.helper.WorkTime;


/**
 * Created by android on 12/1/2014.
 */
public class Business {
    public int id;
    public String businessUserName;
    public int userID;
    public String name;
    public String coverPicture;
    public String profilePicture;
    public String category;
    public String subcategory;
    public String description;
    public WorkTime workTime;
    public String phone;
    public String state;
    public String city;
    public String address;
    public Location_M location_m;
    public String email;
    public String mobile;
    public String webSite;
    public ArrayList<String> hashtagList = new ArrayList<String>();
    public int reviewsNumber;
    public int followersNumber;
    public boolean isFollowing;
    public float rate;


}
