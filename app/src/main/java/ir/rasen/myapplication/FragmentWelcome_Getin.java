package ir.rasen.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ir.rasen.myapplication.ui.TextViewFont;

/**
 * Created by 'Sina KH'.
 */
public class FragmentWelcome_Getin extends Fragment {
    private static final String TAG = "FragmentWelcomeGetin";

    public static FragmentWelcome_Getin newInstance (){
        FragmentWelcome_Getin fragment = new FragmentWelcome_Getin();
        return fragment;
    }

    public FragmentWelcome_Getin() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_getin, container, false);

        return view;
    }

}
