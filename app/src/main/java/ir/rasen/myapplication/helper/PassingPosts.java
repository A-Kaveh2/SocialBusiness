package ir.rasen.myapplication.helper;

import java.util.ArrayList;

import ir.rasen.myapplication.classes.Post;

public class PassingPosts {

    /**
     * Created by 'SINA KH'.
     */
    private static final PassingPosts instance = new PassingPosts();

    private ArrayList<Post> posts = new ArrayList<>();

    private PassingPosts(){}

    public static PassingPosts getInstance(){
        return instance;
    }

    public void setValue(ArrayList<Post> posts){
        this.posts = posts;
    }

    public ArrayList<Post> getValue(){
        return posts;
    }

}
