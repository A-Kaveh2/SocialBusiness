package ir.rasen.myapplication.helper;

import ir.rasen.myapplication.classes.Business;

public class PassingBusiness {

    /**
     * Created by 'SINA KH'.
     */
    private static final PassingBusiness instance = new PassingBusiness();

    private Business business = null;

    private PassingBusiness() {
    }

    public static PassingBusiness getInstance() {
        return instance;
    }

    public void setValueStep1(Business business) {
        if (business == null) {
            this.business = null;
            return;
        }
        this.business = new Business();
        this.business.businessID = business.businessID;
        this.business.name = business.name;
        this.business.category = business.category;
        this.business.subcategory = business.subcategory;
        this.business.description = business.description;
        this.business.profilePicture = business.profilePicture;
        ;
    }

    public void setValueStep2(Business business) {

        if (business != null) {
            this.business.phone = business.phone;
            this.business.webSite = business.webSite;
            this.business.email = business.email;
            this.business.mobile = business.mobile;
            this.business.workTime = business.workTime;
            this.business.location_m = business.location_m;
        } else {
            this.business.phone = null;
            this.business.webSite = null;
            this.business.email = null;
            this.business.mobile = null;
            this.business.workTime = null;
            this.business.location_m = null;
        }

    }

    public Business getValue() {
        return business;
    }

}
