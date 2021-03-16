package com.example.ravelocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class SplashActivity extends AppCompatActivity implements FetchAddressTask.OnTaskCompleted {

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Location mLastLocation;
    private RaveLocatorViewModel mRaveLocatorViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        ImageView imageView = findViewById(R.id.icon_inner);
        Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotating_disc_splash );
        imageView.startAnimation(rotate);



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // method to get the location
        mRaveLocatorViewModel = new ViewModelProvider(this).get(RaveLocatorViewModel.class);
        //mRaveLocatorViewModel.getLocation();
        mRaveLocatorViewModel.updateDatabase();
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getApplication().getApplicationContext());
        boolean settingsChanged = sharedPref.getBoolean("settings_changed", false);
        if(settingsChanged){
            updateManualLocation();
        } else {
            getLocation();
        }
    }



    private void updateManualLocation(){
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getApplication().getApplicationContext());
        String lat = sharedPref.getString("lat", "");
        String lon = sharedPref.getString("lon", "");
        Location newLocation = new Location("");
        newLocation.setLatitude(Double.valueOf(lat));
        newLocation.setLongitude(Double.valueOf(lon));
        new FetchAddressTask(SplashActivity.this,
                SplashActivity.this).execute(newLocation);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("settings_changed", false);
        editor.apply();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLastLocation = location;
                                SharedPreferences sharedPref = PreferenceManager
                                        .getDefaultSharedPreferences(getApplication().getApplicationContext());
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("lat", String.valueOf(mLastLocation.getLatitude()));

                                editor.putString("lon", String.valueOf(mLastLocation.getLongitude()));
                                editor.putBoolean("settings_changed", false);
                                editor.apply();

                                new FetchAddressTask(SplashActivity.this,
                                        SplashActivity.this).execute(location);
                            } else {
                                Toast.makeText(getApplicationContext(), "No Location", Toast.LENGTH_LONG).show();
                                Log.d("Location", "No Location");
                                startActivity(new Intent(getApplication(), MainActivity.class));
                            }
                        }
                    });
        }
    }

    @Override
    public void onTaskCompleted(String[] result) {
        // Update the UI
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getApplication().getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("city", result[0]);
        editor.putString("state", result[1]);
        editor.apply();
        startActivity(new Intent(getApplication(), MainActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "permission denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}