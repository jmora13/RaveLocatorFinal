package com.example.ravelocator;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.ravelocator.adapters.DataFragmentAdapter;
import com.example.ravelocator.databinding.ActivityMainBinding;
import com.example.ravelocator.model.Datum;
import com.example.ravelocator.model.DatumFavoriteUpdate;
import com.google.android.material.tabs.TabLayoutMediator;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;
    private ActivityMainBinding binding;
    private RaveLocatorViewModel mRaveLocatorViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        createNotificationChannel();
        DataFragmentAdapter ad = new DataFragmentAdapter(this);
        binding.pager.setAdapter(ad);
        binding.pager.setOffscreenPageLimit(1);
        new TabLayoutMediator(binding.tabLayout, binding.pager, (tab, position) -> {
            binding.pager.setCurrentItem(tab.getPosition(), true);
            if (position == 0) {
                tab.setText("Nearby");
                tab.setIcon(R.drawable.ic_baseline_location_on_24_white);
            }
            if (position == 1) {
                tab.setText("Favorites");
                tab.setIcon(R.drawable.ic_baseline_favorite_24_white);
            }
        }).attach();
        setSupportActionBar(binding.toolbar);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    item.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Enable location for best experience", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Tap update to save changes", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this, SettingsActivity.class);
                    startActivity(i);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



public void onListItemClick(Datum datum) {
    //Toast.makeText(this, datum., Toast.LENGTH_SHORT).show();
//        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
//        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    DatumFavoriteUpdate isFavorite;
    if(datum.getFavorite() == false) {
        isFavorite = new DatumFavoriteUpdate(datum.getId(), true);
    } else {
        isFavorite = new DatumFavoriteUpdate(datum.getId(), false);
    }
    mRaveLocatorViewModel.updateDatumFavorites(isFavorite);
}
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    public void createNotificationChannel(){
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Bacon and Cheese");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Event Clicked")
                .setContentText("Artist + date")
                .setSmallIcon(R.drawable.ic_baseline_date_range_24)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        return notifyBuilder;

    }
}