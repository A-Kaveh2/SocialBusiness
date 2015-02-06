package ir.rasen.myapplication.helper;

import java.util.ArrayList;

public class Params {

    /**
     * Created by 'SINA KH'.
     */
    public static String USER_USERNAME_VALIDATION = "[0-9a-zA-Z._]*";
    public static int USER_USERNAME_MIN_LENGTH = 4;
    public static int USER_USERNAME_MAX_LENGTH = 15;
    public static String USER_NAME_VALIDATION = "[0-9a-zA-Z._ \\u0600-\\u06FF\\uFB8A\\u067E\\u0686\\u06AF]*";
    public static int USER_NAME_MIN_LENGTH = 3;
    public static int USER_NAME_MAX_LENGTH = 25;
    public static int USER_PASSWORD_MIN_LENGTH = 5;
    public static int BUSINESS_DESCRIPTION_MIN_LENGTH = 5;
    public static int COMMENT_TEXT_MIN_LENGTH = 3;
    public static int COMMENT_TEXT_MAX_LENGTH = 255;
    public static String TEXT_HASHTAG_VALIDATION = "[0-9a-zA-Z._\\u0600-\\u06FF\\uFB8A\\u067E\\u0686\\u06AF]*";
    public static int HASHTAG_MIN_LENGTH = 2;

    public static String BUSINESS = "business";

    public static int HOME = 0;
    public static int SEARCH = 1;
    public static int PROFILE = 2;
    public static String PROFILE_TYPE = "profile_type";
    public static String PROFILE_OWN = "profile_own";

    public static class ProfileType {
        public static int PROFILE_USER = 0;
        public static int PROFILE_BUSINESS = 1;
    }

    public static String HOME_TYPE = "home_type";
    public static class HomeType {
        public static int HOME_HOME = 0;
        public static int HOME_SEARCH = 1;
        public static int HOME_POST = 2;
    }

    /*public static class ActionBarSensitivity {
        public static int TO_HIDE = 100;
        public static int TO_SHOW = 50;
    }*/

    public static String SET_LOCATION_TYPE = "set_location_type";
    public static String SEARCH_TYPE = "search_type";
    public static class SearchType {
        public static int BUSINESSES = 0;
        public static int PRODUCTS = 1;
    }
    public static int INTENT_LOCATION = 5;
    public static int INTENT_WORK_TIME = 6;
    public static int INTENT_ERROR = -1;
    public static int INTENT_OK = 1;

    public static String WORK_TIME_OPEN_HOUR= "work_time_open_hour";
    public static String WORK_TIME_OPEN_MINUTE= "work_time_open_minute";
    public static String WORK_TIME_CLOSE_HOUR= "work_time_close_hour";
    public static String WORK_TIME_CLOSE_MINUTE= "work_time_close_minute";

    public static String EDIT_MODE = "edit_mode";

    public static int LOCATION_REFRESH = 60000;
    public static int LOCATION_REFRESH_DISTANCE = 10;

    public static String CLOSED_BEFORE_RESPONSE = "closed before webservice response...";

    /**
     * Created by android on 12/15/2014.
     */
    public static String EMAIL = "Email";
    public static String PASSWORD = "Password";
    public static String PASSWORD_NEW = "PasswordNew";
    public static String USER_ID = "UserId";
    public static String NAME = "Name";
    public static String ABOUT_ME = "AboutMe";
    public static String SEX = "Sex";
    public static String BIRTH_DATE = "BirthDate";
    public static String PROFILE_PICTURE = "ProfilePicture";
    public static String COVER_PICTURE = "CoverPicture";
    public static String FRIEND_REQUEST_NUMBER = "FriendRequestNumber";
    public static String REVIEWS_NUMBER = "ReviewsNumber";
    public static String FOLLOWED_BUSINESSES_NUMBER = "FollowedBusinessNumber";
    public static String FRIENDS_NUMBER = "FriendsNumber";
    public static String PERMISSION = "Permission";
    public static String FOLLOWED_BUSINESS = "FollowedBusinesses";
    public static String FRIENDS = "Friends";
    public static String REVIEWS = "Reviews";
    public static String FRIENDSHIP_RELATION_STATUS = "FriendshipRelationStatus";
    public static String FRIENDSHIP_RELATION_STATUS_CODE = "FriendshipRelationStatusCode";
    public static String PICTURE = "Picture";
    public static String PICTURE_ID = "PictureId";
    public static String SEARCH_PICTURE_ID = "SearchPictureId";
    public static String TITLE = "Title";
    public static String PERMISSION_FOLLOWED_BUSINESSES = "PermissionFollowedBusinesses";
    public static String PERMISSION_FRIENDS = "PermissionFriends";
    public static String PERMISSION_REVIEWS = "PermissionReviews";
    public static String FROM_INDEX = "FromIndex";
    public static String UNTIL_INDEX = "UntilIndex";
    public static String ID = "Id";
    public static String CREATION_DATAE= "CreationDate";
    public static String PICTURE_SMALL= "PictureSmall";
    public static String DESCRIPTION= "Description";
    public static String PRICE = "Price";
    public static String CODE= "Code";
    public static String COMMENTS = "Comments";
    public static String HASHTAG_LIST= "HashTagList";
    public static String POST_ID= "PostId";
    public static String POST_PICTURE_ID= "PostId";
    public static String TEXT= "Text";
    public static String APPLICATOR_USER_ID= "ApplicatorId";
    public static String REQUESTED_USER_ID= "RequestedId";
    public static String ANSWER= "Answer";
    public static String BUSINESS_ID= "BusinessId";
    public static String BUSINESS_USER_NAME= "BusinessUserName";
    public static String REVIEW_ID= "ReviewId";
    public static String SEARCH_TEXT= "SearchText";
    public static String LOCATION_LATITUDE= "LocationLatitude";
    public static String LOCATION_LONGITUDE= "LocationLongitude";
    public static String NEAR_BY= "NearBy";
    public static String CATEGORY= "Category";
    public static String SUBCATEGORY= "SubCategory";
    public static String WORK_DAYS= "WorkDays";
    public static String PHONE= "Phone";
    public static String ADDRESS= "Address";
    public static String MOBILE= "Mobile";
    public static String FOLLOWERS_NUMBER= "FollowersNumber";
    public static String IS_FOLLOWING= "IsFollowing";
    public static String WORK_TIME_OPEN= "WorkTimeOpen";
    public static String WORK_TIME_CLOSE= "WorkTimeClose";
    public static String COMMENT_ID= "CommentId";
    public static String COMMENT= "Comment";
    public static String RATE= "Rate";
    public static String REVIEW= "Review";
    public static String STATE= "State";
    public static String CITY= "City";
    public static String IS_EMAIL_COMFIRMED= "IsEmailConfirmed";
    public static String SUCCESS = "SuccessStatus";
    public static String RESULT = "Result";
    public static String ERROR = "Error";
    public static String POST_PICUTE = "PostPicture";
    public static String USER_PICUTE = "UserPicture";
    public static String USER_NAME= "UserName";
    public static String BUSINESS_PICUTE = "BusinessPicture";
    public static String BUSINESS_PICUTE_ID = "BusinessPictureId";
    public static String BUSINESS_PROFILE_PICUTE_ID = "BusinessProfilePictureId";
    public static String INTERVAL_TIME = "IntervalTime";
    public static String ERROR_CODE = "ErrorCode";
    public static String ACCESS_TOKEN = "AccessToken";
    public static String IMAGE = "image";
    public static String CATEGORY_ID = "CategoryId";
    public static String SUB_CATEGORY_ID = "SubCategoryId";
    public static String BUSINESSES = "Businesses";
    public static String PROFILE_PICTURE_ID = "ProfilePictureId";



}
