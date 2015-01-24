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

    /**
     * Created by android on 12/15/2014.
     */
    public static String EMAIL = "email";
    public static String PASSWORD = "password";
    public static String PASSWORD_NEW = "password_new";
    public static String USER_ID = "user_id";
    public static String NAME = "name";
    public static String ABOUT_ME = "about_me";
    public static String SEX = "sex";
    public static String BIRTH_DATE = "birth_date";
    public static String PROFILE_PICTURE = "profile_picture";
    public static String COVER_PICTURE = "cover_picture";
    public static String FRIEND_REQUEST_NUMBER = "friend_requestNumber";
    public static String REVIEWS_NUMBER = "reviews_number";
    public static String FOLLOWED_BUSINESSES_NUMBER = "followed_businesses_number";
    public static String FRIENDS_NUMBER = "friends_number";
    public static String PERMISSION = "permission";
    public static String FOLLOWED_BUSINESS = "followed_business";
    public static String FRIENDS = "friends";
    public static String REVIEWS = "reviews";
    public static String FRIENDSHIP_RELATION_STATUS = "friendship_relation_status";
    public static String PICTURE = "picture";
    public static String TITLE = "title";
    public static String PERMISSION_FOLLOWED_BUSINESSES = "permission_followed_businesses";
    public static String PERMISSION_FRIENDS = "permission_friends";
    public static String PERMISSION_REVIEWS = "permission_reviews";
    public static String FROM_INDEX = "from_index";
    public static String UNTIL_INDEX = "until_index";
    public static String ID = "id";
    public static String CREATION_DATAE= "creation_date";
    public static String PICTURE_SMALL= "picture_small";
    public static String DESCRIPTION= "description";
    public static String PRICE = "price";
    public static String CODE= "code";
    public static String LAST_THREE_COMMENTS = "last_three_comments";
    public static String HASHTAG_LIST= "hashtag_list";
    public static String POST_ID= "post_id";
    public static String TEXT= "text";
    public static String APPLICATOR_USER_ID= "applicator_id";
    public static String REQUESTED_USER_ID= "requested_id";
    public static String ANSWER= "answer";
    public static String BUSINESS_ID= "business_id";
    public static String REVIEW_ID= "review_id";
    public static String SEARCH_TEXT= "search_text";
    public static String LOCATION_LATITUDE= "location_latitude";
    public static String LOCATION_LONGITUDE= "location_longitude";
    public static String NEAR_BY= "near_by";
    public static String CATEGORY= "category";
    public static String SUBCATEGORY= "subcategory";
    public static String WORK_DAYS= "work_days";
    public static String PHONE= "work_days";
    public static String ADDRESS= "address";
    public static String MOBILE= "mobile";
    public static String FOLLOWERS_NUMBER= "followers_number";
    public static String IS_FOLLOWING= "is_following";
    public static String WORK_TIME_OPEN= "work_time_open";
    public static String WORK_TIME_CLOSE= "work_time_close";
    public static String COMMENT_ID= "comment_id";
    public static String COMMENT= "comment";
    public static String RATE= "rate";
    public static String REVIEW= "review";
    public static String STATE= "state";
    public static String CITY= "city";
    public static String IS_EMAIL_COMFIRMED= "is_email_confirmed";
    public static String SUCCESS = "success";
    public static String RESULT = "result";
    public static String ERROR = "error";
    public static String POST_PICUTE = "post_picture";
    public static String USER_PICUTE = "user_picture";
    public static String BUSINESS_PICUTE = "business_picture";
    public static String INTERVAL_TIME = "interval_time";
    public static String ERROR_CODE = "ErrorCode";
    public static String ACCESS_TOKEN = "access_token";


}
