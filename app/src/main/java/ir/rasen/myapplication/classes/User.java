package ir.rasen.myapplication.classes;


import ir.rasen.myapplication.helper.FriendshipRelation;
import ir.rasen.myapplication.helper.Permission;
import ir.rasen.myapplication.helper.Sex;

/**
 * Created by A.Kaveh on 11/29/2014.
 */
public class User {

    public String userID;
    public String name;
    public String email;
    public boolean isEmailConfirmed;
    public String password;
    public String aboutMe;
    public Sex sex;
    public String birthDate;
    public String profilePicture;
    public String coverPicture;
    public Permission permissions;
    public FriendshipRelation.Status friendshipRelationStatus;
    public int friendRequestNumber;
    public int reviewsNumber;
    public int followedBusinessesNumber;
    public int friendsNumber;



}
