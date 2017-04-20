package com.app.geofancing;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.SubscribeListener;

/**
 * Created by beyond on 14-Apr-17.
 */

public class DDPClient extends Service implements MeteorCallback {
    private Meteor mMeteor;
    private GeoFencingService fencingService;
    Context context;
    MyThread myThread;
    static DDPClient ddpClient;
    Thread thread;
    public DDPClient() {

    }

    public static DDPClient getInstance() {
        if (ddpClient != null)
            return ddpClient;

        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("DDP CLIENT", "started");


        myThread = new MyThread(startId, this, context);

        thread = new Thread(myThread);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onConnect(boolean signedInAutomatically) {
        Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
        System.out.println("Connected");
        System.out.println("Is logged in: " + mMeteor.isLoggedIn());
        System.out.println("User ID: " + mMeteor.getUserId());

        if (signedInAutomatically) {

            System.out.println("Successfully logged in automatically");
        } else {
            mMeteor.loginWithEmail("kartikmalik06@gmail.com", "k@23740800", new ResultListener() {

                @Override
                public void onSuccess(String result) {
                    System.out.println("Successfully logged in: " + result);
                    System.out.println("Is logged in: " + mMeteor.isLoggedIn());
                    System.out.println("User ID: " + mMeteor.getUserId());

                }

                @Override
                public void onError(String error, String reason, String details) {
                    System.out.println("Could not log in: " + error + " / " + reason + " / " + details);
                }

            });
        }
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
        System.out.println("Data added to <" + collectionName + "> in document <" + documentID + ">");
        System.out.println("    Added: " + newValuesJson);
        try {
            JSONObject jsonObject = new JSONObject(newValuesJson);
            if (jsonObject.get("lat").toString() != null) {
                GeoFenceObject object = new GeoFenceObject();
                //id of geofence
                object.setId(documentID);
                object.setLatitude(jsonObject.getDouble("lat"));
                object.setLongitude(jsonObject.getDouble("lng"));
                object.setRadius(Float.valueOf(jsonObject.get("radius").toString()));
                JSONArray jsonArray = jsonObject.getJSONArray("advertisements");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    Advertisment advertisment = new Advertisment();
                    advertisment.setTitle(c.get("title").toString());
                    advertisment.setDiscription(c.get("description").toString());
                    object.setAdvertisements(advertisment);
                }
                fencingService.createGeofence(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
        System.out.println("Data changed in <" + collectionName + "> in document <" + documentID + ">");
        System.out.println("    Updated: " + updatedValuesJson);
        System.out.println("    Removed: " + removedValuesJson);
        try {
            JSONObject jsonObject = new JSONObject(updatedValuesJson);
            if (jsonObject.get("lat").toString() != null) {
                GeoFenceObject object = new GeoFenceObject();
                //id of geofence
                object.setId(documentID);
                object.setLatitude(jsonObject.getDouble("lat"));
                object.setLongitude(jsonObject.getDouble("lng"));
                object.setRadius(Float.valueOf(jsonObject.get("radius").toString()));
                JSONArray jsonArray = jsonObject.getJSONArray("advertisements");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    Advertisment advertisment = new Advertisment();
                    advertisment.setTitle(c.get("title").toString());
                    advertisment.setDiscription(c.get("description").toString());
                    object.setAdvertisements(advertisment);
                }
                fencingService.createGeofence(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }

    public void subscribeForData(Location mLocation) {
        Log.d("subscribe","Called");
        if (mMeteor!=null) {
            String subscriptionId = mMeteor.subscribe("fences.nearest", new Object[]{mLocation.getLatitude(), mLocation.getLongitude()}, new SubscribeListener() {
                @Override
                public void onSuccess() {
                    Log.d("Data", "success");
                }

                @Override
                public void onError(String error, String reason, String details) {
                    Log.d("Data", error + " reason " + reason + "/n details " + details);
                }
            });
        }
    }

    public class MyThread implements Runnable {
        int startId;
        DDPClient ddpClient;
        public Context context;

        public MyThread(int startId, DDPClient ddpClient, Context context) {
            this.startId = startId;
            this.ddpClient = ddpClient;
            this.context = context;
        }

        @Override
        public void run() {
            //initializing Singleton object of class
            DDPClient.ddpClient=ddpClient;
            mMeteor = new Meteor(ddpClient, "ws://geoadvts.herokuapp.com/websocket");
            fencingService = GeoFencingService.getInstance();
            mMeteor.addCallback(ddpClient);
            mMeteor.connect();
        }
    }
}
