package com.app.geofancing;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by beyond on 21-Mar-17.
 */

public class GeofenceTrasitionService extends IntentService {

    private static final String TAG = GeofenceTrasitionService.class.getSimpleName();


    public GeofenceTrasitionService() {
        super(TAG);
    }
//use to assign id to notification
    int i = 0;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("intent service", "strated");

        // Retrieve the Geofencing intent
        HashMap<String, GeoFenceObject> listFenceObjectHashMap = GeoFencingService.getInstance().getList();
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        // Handling errors
        if (geofencingEvent.hasError()) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMsg);
            return;
        }
        // Retrieve GeofenceTrasition
        int geoFenceTransition = geofencingEvent.getGeofenceTransition();
        // Check if the transition type
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            // Create a detail message with Geofences received

            // Send notifications details as a String
            for (Geofence geofence : triggeringGeofences) {
                sendNotification(listFenceObjectHashMap.get(geofence.getRequestId()));
            }

        }
    }


    // Send a notification
    private void sendNotification(GeoFenceObject geofenceTransitionDetails) {
        Notification n = new Notification.Builder(this)
                .setContentTitle(geofenceTransitionDetails.getAdvertisment().getTitle())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(geofenceTransitionDetails.getAdvertisment().getDiscription())
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setAutoCancel(false)
                .build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(i, n);
        i++;
    }

    // Handle errors
    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFenceObject not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }
}
