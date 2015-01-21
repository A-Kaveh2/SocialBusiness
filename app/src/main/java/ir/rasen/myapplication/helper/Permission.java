package ir.rasen.myapplication.helper;


import org.json.JSONObject;

public class Permission {

    public boolean followedBusiness;
    public boolean friends;
    public boolean reviews;

    public void getFromJsonObject(JSONObject jsonObject) throws Exception {
        followedBusiness = jsonObject.getBoolean(Params.FOLLOWED_BUSINESS);
        friends = jsonObject.getBoolean(Params.FRIENDS);
        reviews = jsonObject.getBoolean(Params.REVIEWS);
    }
}
