package ir.rasen.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.ButtonFont;
import ir.rasen.myapplication.ui.TextViewFont;

/**
 * Created by 'Sina KH'.
 */
public class FragmentCallInfo extends Fragment {
    private static final String TAG = "FragmentCallInfo";

    private View view;
    private ListAdapter mAdapter;

    // business id is received here
    private int businessId;

    private double markerLatitude=35.7014396, markerLongitude=51.3498186;

    private GoogleMap mMap;

    public static FragmentCallInfo newInstance (String businessId){
        FragmentCallInfo fragment = new FragmentCallInfo();

        Bundle bundle = new Bundle();
        bundle.putString(Params.BUSINESS_ID, businessId);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentCallInfo() {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            businessId = bundle.getInt(Params.BUSINESS_ID);
        } else {
            Log.e(TAG, "bundle is null!!");
            if(getActivity()!=null) getActivity().finish();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_info, container, false);
        this.view = view;

        setUpMapIfNeeded();
        return view;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
            if(resultCode != ConnectionResult.SUCCESS)
            {
                // GOOGLE PLAY SERVICES NOT FOUND !!
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        googlePlayNotFound();
                    }
                }, 100);
            } else {
                // LOADING MAP
                FragmentManager fm = getChildFragmentManager();
                mMap = ((SupportMapFragment) fm.findFragmentById(R.id.map)).getMap();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                //mMap.setInfoWindowAdapter(new PopupAdapter(getBaseContext(), getLayoutInflater()));
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setUpMapEvents();
                    }
                }, 3000);

                // set map fragment's place
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setUpMapPlace();
                    }
                }, 100);

            }
        }
    }

    public void googlePlayNotFound() {
        view.findViewById(R.id.map).setVisibility(View.INVISIBLE);

        // SHOWING POPUP WINDOW
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(R.string.error)
                .setMessage(R.string.error_google_play_services)
                .setPositiveButton(R.string.download, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // TODO RECEIVE AND INSTALL
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms"));
                        startActivity(marketIntent);
                        getActivity().onBackPressed();
                    }
                })
                .setNegativeButton(R.string.not_now, null)
                .create().show();
    }

    private void setUpMapEvents() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String strUri = "geo:"+markerLatitude+","+markerLongitude+"?z=13";
                Uri uri = Uri.parse(strUri);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void setUpMapPlace() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        params.setMargins(margin,margin,margin,margin);
        params.height = view.findViewById(R.id.ll_call_info_map).getMeasuredHeight()-2*margin;
        view.findViewById(R.id.map).setLayoutParams(params);
    }
}
