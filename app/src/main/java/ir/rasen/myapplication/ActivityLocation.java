package ir.rasen.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.rasen.myapplication.adapters.MarkerPopupAdapter;
import ir.rasen.myapplication.helper.Params;

/**
 * Created by 'Sina KH'.
 */
public class ActivityLocation extends FragmentActivity {

    private GoogleMap mMap;
    private Location myLocation;
    private Marker marker;
    private String businessName="نام کسب و کار", businessCategory="موضوع کسب و کار";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_location);

        setUpMapIfNeeded();
    }

    // SUBMIT TOUCHED
    public void submit(View view) {
        Intent i = new Intent();
        i.putExtra(Params.LOCATION_LATITUDE, marker.getPosition().latitude+"");
        i.putExtra(Params.LOCATION_LONGITUDE, marker.getPosition().longitude+"");
        setResult(Params.INTENT_OK , i);
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }

    public void onBackPressed() {
        setResult(Params.INTENT_ERROR, null);
		finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }
    public void back(View v) {
        onBackPressed();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
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
                mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
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

            }
        }
    }

    public void googlePlayNotFound() {
        findViewById(R.id.map).setVisibility(View.INVISIBLE);

        // SHOWING POPUP WINDOW
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
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
                    finish();
                    overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
                }
            })
            .setNegativeButton(R.string.not_now, null)
            .create().show();
    }

    private void setUpMapEvents() {

        // first views...
        final Location location = mMap.getMyLocation();
        LatLng target;
        com.google.android.gms.maps.model.CameraPosition.Builder builder = new CameraPosition.Builder();
        if(location==null) {
            target = new LatLng(35.7014396,51.3498186);
            builder.zoom(10);
        } else {
            target = new LatLng(location.getLatitude(),location.getLongitude());
            builder.zoom(13);
        }
        builder.target(target);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
        // change camera place to my location if my place was found
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(myLocation==null && location!=null) {
                    myLocation = location;
                    LatLng target;
                    com.google.android.gms.maps.model.CameraPosition.Builder builder = new CameraPosition.Builder();
                    if(getIntent().getExtras().containsKey(Params.LOCATION_LATITUDE))
                        target = new LatLng(Double.parseDouble(getIntent().getStringExtra(Params.LOCATION_LATITUDE))
                                , Double.parseDouble(getIntent().getStringExtra(Params.LOCATION_LONGITUDE)));
                    else
                        target = new LatLng(location.getLatitude(), location.getLongitude());
                    builder.zoom(13);
                    builder.target(target);
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
                    if(marker==null && getIntent().getIntExtra(Params.SET_LOCATION_TYPE, Params.SEARCH)!=Params.SEARCH) {
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                                .title(businessName)
                                .snippet(businessCategory)
                                .draggable(true));
                        findViewById(R.id.btn_location_submit).setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        // add/move marker on click
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(marker==null) {
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(businessName)
                            .snippet(businessCategory)
                            .draggable(true));
                    findViewById(R.id.btn_location_submit).setVisibility(View.VISIBLE);
                } else
                    marker.setPosition(latLng);
            }
        });
        // set popup info adapter
        if(getIntent().getIntExtra(Params.SET_LOCATION_TYPE, Params.SEARCH)!=Params.SEARCH)
            mMap.setInfoWindowAdapter(new MarkerPopupAdapter(getBaseContext(), getLayoutInflater()));
        // set marker prev place
        if(getIntent().getExtras().containsKey(Params.LOCATION_LATITUDE)) {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(getIntent().getStringExtra(Params.LOCATION_LATITUDE))
                                , Double.parseDouble(getIntent().getStringExtra(Params.LOCATION_LONGITUDE))))
                        .title(businessName)
                        .snippet(businessCategory)
                        .draggable(true));
                findViewById(R.id.btn_location_submit).setVisibility(View.VISIBLE);
        }
    }

}
