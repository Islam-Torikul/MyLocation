package com.example.torikul.mylocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    Location myLocationRequest;
    GoogleApiClient myGoogleApiClient;
    Marker myCurrLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                { buildGoogleApiClient(); mMap.setMyLocationEnabled(true); } }
                else
                    { buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                    }

       protected synchronized void buildGoogleApiClient() {

           myGoogleApiClient = new
             GoogleApiClient.Builder(this)
              .addConnectionCallbacks(this)
              .addOnConnectionFailedListener(this)
              .addApi(LocationServices.API)
              .build();
             myGoogleApiClient.connect();

        }

    }

    @Override
    public void onLocationChanged(Location location) {

        myLastLocation = location;
        if (myCurrLocationMarker != null)
        { myCurrLocationMarker.remove();
        }

        //Move the marker
        LatLng latLng = new LatLng(location.getLatitude(),
                location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        markerOptions.title("My Position");
        markerOptions.icon(BitmapDescriptorFactory .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        myCurrLocationMarker = mMap.addMarker(markerOptions);

        //Move the map view
        mMap.moveCamera(CameraUpdateFactory .newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory .zoomTo(11));

        //Stop moving the marker
        if (myGoogleApiClient != null)
        { LocationServices.FusedLocationApi .removeLocationUpdates(myGoogleApiClient, this);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        myLocationRequest = new LocationRequest();
        myLocationRequest.setInterval(1000);
        myLocationRequest.setFastestInterval(1000);
        myLocationRequest .setPriority(LocationRequest .PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        { LocationServices.FusedLocationApi .requestLocationUpdates(myGoogleApiClient, myLocationRequest, this);
         }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest .permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION); }
                        else { ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission .ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION); }
                    return false; }
                    else
                        { return true; } }
     public void onReuestPermissionsResult (int requestCode,String permissions[], int[] grantResults){
         switch (requestCode){
             case MY_PERMISSIONS_REQUEST_LOCATION:{
                 if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                     if (ContextCompat.checkSelfPermission(this,
                             Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                         if (myGoogleApiClient == null) { buildGoogleApiClient(); }
                         mMap.setMyLocationEnabled(true);
                     }
                 }
                 else
                 { Toast.makeText(this, "Permission not given.", Toast.LENGTH_LONG).show(); }
                 return;
             }
         }
     }

    private void buildGoogleApiClient() {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
