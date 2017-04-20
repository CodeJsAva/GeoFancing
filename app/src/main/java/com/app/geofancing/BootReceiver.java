package com.app.geofancing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by beyond on 20-Apr-17.
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, GeoFencingService.class);
        context.startService(myIntent);
        Intent Intent = new Intent(context, DDPClient.class);
        context.startService(Intent);

    }
}