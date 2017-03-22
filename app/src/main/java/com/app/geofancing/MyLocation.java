package com.app.geofancing;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by beyond on 21-Mar-17.
 */

public class MyLocation  {

    private static final int REQ_PERMISSION = 1;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private LocationRequest locationRequest;
    final String ACTION_BEACON_TRIGGER = "101";
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;

    private Context context;
    DemoService geoFancingService;

    public MyLocation(Context context, DemoService geoFancingService) {
        this.context = context;
        this.geoFancingService = geoFancingService;
    }

    // Create GoogleApiClient instance
    public GoogleApiClient createGoogleApi(DemoService geoFancingService) {
        Log.d("tag", "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(geoFancingService)
                    .addOnConnectionFailedListener(geoFancingService)
                    .addApi(LocationServices.API)
                    .build();
        }
        return googleApiClient;
    }

    // Get last known mylocation
    public void getLastKnownLocation() {
        Log.d("TAG", "getLastKnownLocation()");
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Log.i("TAG", "LastKnown mylocation. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());

                startLocationUpdates(geoFancingService);
            } else {
                Log.w("TAG", "No mylocation retrieved yet");
                startLocationUpdates(geoFancingService);
            }
        } else {
            Intent i = new Intent();
            i.setAction(ACTION_BEACON_TRIGGER);
            LocalBroadcastManager.getInstance(context).sendBroadcast(i);
        }

    }

    // Start mylocation Updates
    private void startLocationUpdates(DemoService geoFancingService ) {
        Log.i("TAG", "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (checkPermission())
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, geoFancingService);
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d("TAG", "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    // Verify user's response of the permission requested


    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w("TAG", "permissionsDenied()");
    }



    public Location getLocation() {

        return lastLocation;
    }
}
