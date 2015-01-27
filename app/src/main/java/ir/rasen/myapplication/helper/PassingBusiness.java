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

    public void setValue(Business business){
        this.business = business;
    }

    public Business getValue() {
        return business;
    }

}
