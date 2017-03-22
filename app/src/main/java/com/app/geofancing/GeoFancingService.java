package com.app.geofancing;

import android.app.IntentService;

import android.app.PendingIntent;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by kartik on 20-Mar-17.
 */

public class GeoFancingService extends IntentService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    private GoogleApiClient googleApiClient;
    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters
    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;
    MyLocation mylocation;
    Location location;
    boolean isStart;

    public GeoFancingService() {
        super("GeoFancingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       /* Log.d("service", "start");*/
       /* mylocation = new MyLocation(this, this);
        googleApiClient = mylocation.createGoogleApi(this);
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (googleApiClient != null) {
                    googleApiClient.connect();
                    while (!isGeoFenceStart())
                        if (location != null) {
                            startGeofence();
                        }
                }
            }
        },2000,2000);*/

    }

    private boolean isGeoFenceStart() {
        return isStart;


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Service", "Stopped");
    }


    // Create a Geofence
    private Geofence createGeofence(Location latLng, float radius) {
        Log.d("Tag", "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(latLng.getLatitude(), latLng.getLongitude(), radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        Log.d("TAG", "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d("TAG", "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d("TAG", "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    //Start Geofence creation process
    private void startGeofence() {
        isStart = true;
        Log.i("TAG", "startGeofence()");
        if (mylocation != null) {
            Geofence geofence = createGeofence(location, GEOFENCE_RADIUS);
            GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
            addGeofence(geofenceRequest);
        } else {
            Log.e("TAG", "Geofence marker is null");
        }
    }


    private PendingIntent createGeofencePendingIntent() {
        Log.d("TAG", "createGeofencePendingIntent");
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;

        Intent intent = new Intent(this, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public void onResult(@NonNull Status status) {
        //startGeofence();
        isStart=false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mylocation.getLastKnownLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;

    }
}
