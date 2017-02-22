package rakshan.himachal.dit.sms.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import rakshan.himachal.dit.sms.Presentation.Custom_Dialog;
import rakshan.himachal.dit.sms.R;
import rakshan.himachal.dit.sms.Utils.PermissionUtils;

public class SOS_Detailed extends FragmentActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowLongClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean mPermissionDenied = false;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    Marker currLocationMarker;
    private GoogleMap mMap;
    public TextView tv_latitude,tv_longitude;
    Button addparking_bt;
    Custom_Dialog CD  = new Custom_Dialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos__detailed);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tv_latitude = (TextView)findViewById(R.id.latitudetv);
        tv_longitude = (TextView)findViewById(R.id.longitudetv);
        addparking_bt = (Button)findViewById(R.id.addparking);

        addparking_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(tv_latitude.getText().length()!= 0 && tv_longitude.getText().length()!=0){

                    Intent i = new Intent(SOS_Detailed.this,VacationTravelDetails.class);
                    i.putExtra("LATITUDE",tv_latitude.getText().toString());
                    i.putExtra("LONGITUDE",tv_longitude.getText().toString());
                    startActivity(i);
                }else{


                    CD.showDialog(SOS_Detailed.this,"Please go to the settings and  enable your GPS Location.");
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        // mMap.setOnInfoWindowLongClickListener(this);
        try {
            mMap.setMyLocationEnabled(true);
        }catch(SecurityException s){
            // Toast.makeText(getApplicationContext(),"Not Good",Toast.LENGTH_SHORT).show();
        }

        buildGoogleApiClient();

        mGoogleApiClient.connect();

        enableMyLocation();
    }

    protected synchronized void buildGoogleApiClient() {
        // Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermissionOther(this, LOCATION_PERMISSION_REQUEST_CODE,  android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this,"onConnected",Toast.LENGTH_SHORT).show();
        Location mLastLocation = null;
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }catch(SecurityException r){
            //
            //  Toast.makeText(getApplicationContext(),"There is a problem with the GPS device.",Toast.LENGTH_SHORT).show();
        }
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mMap.addMarker(markerOptions);
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(150000); //5 seconds
        mLocationRequest.setFastestInterval(100000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }catch(SecurityException s){
            //  Toast.makeText(getApplicationContext(),"Something's not Good.",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
//place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //MarkerOptions markerOptions = new MarkerOptions();
        // markerOptions.position(latLng);
        if(latLng!=null){

            //   tv_latitude.setText(Double.toString((location.getLatitude())));
            //  tv_longitude.setText(Double.toString((location.getLongitude())));

            Log.e("Latitude",Double.toString(location.getLatitude()));
            Log.e("Longitude",Double.toString(location.getLongitude()));
            // markerOptions.title("Latitude: \t"+Double.toString(location.getLatitude())+"\n Longitude:-"+ Double.toString(location.getLongitude()));

            //Update TextView
            new SOS_Detailed.update_View_GPS().execute(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()));


        }else{
            tv_latitude.setText("N/A");
            tv_longitude.setText("N/A");
        }
        //
        // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        //currLocationMarker = mMap.addMarker(markerOptions);


        Location CurrentLocation = new Location("Current Location");
        CurrentLocation.setLatitude(latLng.latitude);
        CurrentLocation.setLongitude(latLng.longitude);


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();  //default was 14

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {

    }
    @Override
    public boolean onMyLocationButtonClick() {
        if((latLng==null)) {
            CD.showDialog(SOS_Detailed.this,"Please go to the settings and  enable your GPS Location.");

        }
        return false;
    }

    public class update_View_GPS extends AsyncTask<String,String,String[]> {

        String[] GPS_ = new String[2]; // <--initialized statement



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv_latitude.setText("");
            tv_longitude.setText("");
        }

        @Override
        protected String[] doInBackground(String... params) {


            GPS_[0] = params[0];
            GPS_[1] = params[1];

            Log.e("Latitude Async", GPS_[0] );
            Log.e("Longitude Async", GPS_[1] );

            return GPS_;
        }

        @Override
        protected void onPostExecute(String[] s) {
            // super.onPreExecute(s);
            Log.e("Latitude Async Post", s[0] );
            Log.e("Longitude Async Post", s[1] );
            tv_latitude.setText(s[0]);
            tv_longitude.setText(s[1]);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        SOS_Detailed.this.finish();
    }
}