package ir.rasen.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.ButtonFont;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.GetBusinessContactInfo;

/**
 * Created by 'Sina KH'.
 */
public class FragmentCallInfo extends Fragment implements WebserviceResponse {
    private static final String TAG = "FragmentCallInfo";

    private View view;

    private double markerLatitude=35.7014396, markerLongitude=51.3498186;

    private GoogleMap mMap;

    private Business business;

    private ProgressDialogCustom pd;

    public static FragmentCallInfo newInstance (){
        FragmentCallInfo fragment = new FragmentCallInfo();

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentCallInfo() {
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

        pd = new ProgressDialogCustom(getActivity());

        business = PassingBusiness.getInstance().getValue();

        new GetBusinessContactInfo(business.id,FragmentCallInfo.this).execute();

        PassingBusiness.getInstance().setValue(null);

        setUpMapIfNeeded();

        pd.show();

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
                }, 1000);

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
                        // receive and install
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
        if(business.location_m==null)
            return;
        markerLatitude  = Double.parseDouble(business.location_m.getLatitude());
        markerLongitude = Double.parseDouble(business.location_m.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(markerLatitude, markerLongitude))
                .title(business.businessUserName)
                .snippet(business.name));
        com.google.android.gms.maps.model.CameraPosition.Builder builder = new CameraPosition.Builder();
        builder.zoom(13);
        builder.target(new LatLng(markerLatitude, markerLongitude));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
    }

    private void setUpMapPlace() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        params.setMargins(margin,margin,margin,margin);
        params.height = view.findViewById(R.id.ll_call_info_map).getMeasuredHeight()-2*margin;
        view.findViewById(R.id.map).setLayoutParams(params);
    }

    private String two_char(int x) {
        if (Integer.toString(x).length()==1)
            return "0"+Integer.toString(x);
        return Integer.toString(x);
    }

    private void assignNow() {

        String workTimeText = "";
        if(business.workTime!=null) {
            boolean[] workDays = business.workTime.getWorkDays();
            if (workDays[0])
                workTimeText += "ش, ";
            for (int i = 1; i < 6; i++) {
                if (workDays[i])
                    workTimeText += i + "ش, ";
            }
            if (workDays[6])
                workTimeText += "جمعه";
            workTimeText +=
                    "\nزمان شروع به کار: " + two_char(((int) business.workTime.time_open_hour)) + ":" + two_char((business.workTime.time_open_minutes))
                            + "\nزمان پایان کار: " + two_char(((int) business.workTime.time_close_hour)) + ":" + two_char((business.workTime.time_close_minutes));
        }
        ((TextViewFont) view.findViewById(R.id.txt_call_info_info)).setText(Html.fromHtml(
                "<font color=#3F6F94>" +  getString(R.string.business_description)
                        + ":</font>" + business.description
                        + (business.phone!=null ? "<font color=#3F6F94>" + getString(R.string.phone) + ":</font> " + business.phone : "")
                        + (business.mobile!=null ? "<br /><font color=#3F6F94>" + getString(R.string.mobile) + ":</font>" + business.mobile : "")
                        + (business.email!=null ? "<br /><font color=#3F6F94>" + getString(R.string.email) + ":</font>" + business.email : "")
                        + (business.webSite!=null ? "<br /><font color=#3F6F94>" + getString(R.string.website) + ":</font>" + business.webSite : "")
                        + (workTimeText!=null ? "<br /><font color=#3F6F94>" + getString(R.string.working_time) + ":</font>" + workTimeText : "")));
    }

    @Override
    public void getResult(Object result) {
        try {
            if (result instanceof Business) {
                business = (Business) result;
                assignNow();
            }
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        try {
            String errorMessage = ServerAnswer.getError(getActivity(), errorCode);
            Dialogs.showMessage(getActivity(), errorMessage, true);
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }
}
