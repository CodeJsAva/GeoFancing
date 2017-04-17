package com.app.geofancing;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONException;
import org.json.JSONObject;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

import static im.delight.android.ddp.Meteor.log;


public class MainActivity extends AppCompatActivity {
    TextView taxtLat, textLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taxtLat = (TextView) findViewById(R.id.lat);
        textLong = (TextView) findViewById(R.id.longi);
        //Fencing Service
        Intent i = new Intent(this, GeoFencingService.class);
        startService(i);
        //DDp Client Service
        Intent j = new Intent(getApplicationContext(), DDPClient.class);
        startService(j);
    }
}
