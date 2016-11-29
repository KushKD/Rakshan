package rakshan.himachal.dit.sms.Activity;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import rakshan.himachal.dit.sms.Helper.AppStatus;
import rakshan.himachal.dit.sms.Interfaces.MapsActivityInterface;
import rakshan.himachal.dit.sms.Model.Route;
import rakshan.himachal.dit.sms.Presentation.Custom_Dialog;
import rakshan.himachal.dit.sms.R;
import rakshan.himachal.dit.sms.Utils.Directions;
import rakshan.himachal.dit.sms.Utils.PermissionUtils;

public class TravelTrackingMaps extends FragmentActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowLongClickListener,
        MapsActivityInterface {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText originAddressInput;
    private EditText destinationAddressInput;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private boolean mPermissionDenied = false;
    Marker currLocationMarker;
    LatLng latLng;
    Custom_Dialog CD = new Custom_Dialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_tracking_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        originAddressInput = (EditText) findViewById(R.id.etOrigin);
        destinationAddressInput = (EditText) findViewById(R.id.etDestination);

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppStatus.getInstance(TravelTrackingMaps.this).isOnline()){
                    triggerDirectionRequest();
                }else{
                    CD.showDialog(TravelTrackingMaps.this,"You are not connected to Internet. Please Connect to Internet!!");
                }

            }
        });
    }

    private void triggerDirectionRequest() {
        String origin = originAddressInput.getText().toString();
        String destination = destinationAddressInput.getText().toString();
        if (origin.isEmpty()) {
            CD.showDialog(TravelTrackingMaps.this,"Please enter origin address!");
            return;
        }
        if (destination.isEmpty()) {
            CD.showDialog(TravelTrackingMaps.this,"Please enter destination address!");
            return;
        }

        try {
            Log.e("ORIGIN",origin);
            Log.e("DESTINATION",destination);
            new Directions(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("ERROR",e.getLocalizedMessage().toString());
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission_Other(TravelTrackingMaps.this, LOCATION_PERMISSION_REQUEST_CODE,  android.Manifest.permission.ACCESS_FINE_LOCATION, true);
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
    public void clearPrevious() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }

    }

    @Override
    public void addNewRoutes(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 9));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.getDurationText());
            ((TextView) findViewById(R.id.tvDistance)).setText(route.getDistanceText());

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.RED).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
        mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }catch(SecurityException s){
            //  Toast.makeText(getApplicationContext(),"Something's not Good.",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
        if (latLng != null) {

            //   tv_latitude.setText(Double.toString((location.getLatitude())));
            //  tv_longitude.setText(Double.toString((location.getLongitude())));

            Log.e("Latitude", Double.toString(location.getLatitude()));
            Log.e("Longitude", Double.toString(location.getLongitude()));
            // markerOptions.title("Latitude: \t"+Double.toString(location.getLatitude())+"\n Longitude:-"+ Double.toString(location.getLongitude()));

            //Update TextView
            // new VacationTraveller.update_View_GPS().execute(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()));

            Location CurrentLocation = new Location("Current Location");
            CurrentLocation.setLatitude(latLng.latitude);
            CurrentLocation.setLongitude(latLng.longitude);


            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(14).build();  //default was 14

            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));


        }

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
            CD.showDialog(TravelTrackingMaps.this,"Please go to the settings and  enable your GPS Location.");

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        TravelTrackingMaps.this.finish();
    }
}
