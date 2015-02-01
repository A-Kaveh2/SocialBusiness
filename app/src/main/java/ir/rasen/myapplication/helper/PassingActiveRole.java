package ir.rasen.myapplication.helper;

import java.util.ArrayList;

import ir.rasen.myapplication.classes.Post;

public class PassingActiveRole {

    /**
     * Created by 'SINA KH'.
     */
    private static final PassingActiveRole instance = new PassingActiveRole();

    private int businessId;

    private PassingActiveRole(){}

    public static PassingActiveRole getInstance(){
        return instance;
    }

    public void setValue(int businessId){
        this.businessId = businessId;
    }

    public int getValue(){
        return businessId;
    }

}
