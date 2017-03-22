package com.app.geofancing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.json.JSONException;

/**
 * Created by beyond on 21-Mar-17.
 */

public class MyReceiver extends BroadcastReceiver {
   final String ACTION_BEACON_TRIGGER="101";
    private static final int REQ_PERMISSION = 1;
    MainActivity mainActivity;
    MyReceiver(MainActivity mainActivity) {
this.mainActivity=mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");

        try {
            switch (intent.getAction()){
                case ACTION_BEACON_TRIGGER:
                    Log.d("TAG", "askPermission()");
                    ActivityCompat.requestPermissions(
                            mainActivity,
                            new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                            REQ_PERMISSION
                    );
                    break;

                default:
                    break;

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


}
