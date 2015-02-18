package ir.rasen.myapplication.helper;


import org.json.JSONObject;

public class Permission {

    public boolean followedBusiness = true;
    public boolean friends= true;
    public boolean reviews = true;

    public void getFromJsonObject(JSONObject jsonObject) throws Exception {
        followedBusiness = jsonObject.getBoolean(Params.FOLLOWED_BUSINESS);
        friends = jsonObject.getBoolean(Params.FRIENDS);
        reviews = jsonObject.getBoolean(Params.REVIEWS);
    }
}
