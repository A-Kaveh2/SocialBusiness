package ir.rasen.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.TextViewFont;

/**
 * Created by 'Sina KH'.
 */
public class FragmentWelcome extends Fragment {
    private static final String TAG = "FragmentWelcome";

    private int pageNumber;

    public static FragmentWelcome newInstance (int pageNumber){
        FragmentWelcome fragment = new FragmentWelcome();

        Bundle bundle = new Bundle();
        bundle.putInt(Params.ID, pageNumber);
        fragment.setArguments(bundle);

        return fragment;
    }

    public FragmentWelcome() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            pageNumber = bundle.getInt(Params.ID);
        } else {
            Log.e(TAG, "bundle is null!!");
            if(getActivity()!=null){
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        RelativeLayout welcome = (RelativeLayout) view.findViewById(R.id.rl_welcome);
        ImageView image = (ImageView) view.findViewById(R.id.img_welcome_img);
        TextViewFont text = (TextViewFont) view.findViewById(R.id.txt_welcome_text);
        switch (pageNumber) {
            case 0:
                image.setImageResource(R.drawable.welcome_1);
                text.setText(getString(R.string.welcome_1));
                text.setTextColor(Color.WHITE);
                welcome.setBackgroundResource(R.color.button_on_dark);
                break;
            case 1:
                image.setImageResource(R.drawable.welcome_2);
                text.setText(getString(R.string.welcome_2));
                welcome.setBackgroundResource(android.R.color.white);
                break;
            case 2:
                image.setImageResource(R.drawable.welcome_3);
                text.setText(getString(R.string.welcome_3));
                text.setTextColor(Color.WHITE);
                welcome.setBackgroundResource(R.color.button_on_dark);
                break;
        }

        return view;
    }

}
