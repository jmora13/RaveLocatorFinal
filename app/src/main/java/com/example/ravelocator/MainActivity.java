package com.example.ravelocator;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumFavoriteUpdate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private RaveLocatorViewModel mRaveLocatorViewModel;
    private Object Menu;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;
    private SharedPreferences sharedPref;
    private String sharedPrefFile = "sharedPrefFile";
    Calendar calendar = Calendar.getInstance();
    private int dayOfMonth = 0;
    private final String DAY_OF_MONTH_KEY = "1";
    ViewPager2 viewPager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    // Initializing other items
    // from layout file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(this);
        createNotificationChannel();
        DataFragmentAdapter ad = new DataFragmentAdapter(this);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(ad);
        viewPager.setOffscreenPageLimit(1);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            viewPager.setCurrentItem(tab.getPosition(), true);
            if (position == 0) {
                tab.setText("Nearby");
                tab.setIcon(R.drawable.ic_baseline_location_on_24_white);
            }
            if (position == 1) {
                tab.setText("Favorites");
                tab.setIcon(R.drawable.ic_baseline_favorite_24_white);
            }
        }).attach();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                    ad.notifyDataSetChanged();
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Tap update to save changes", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
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

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string +
                "/" + day_string + "/" + year_string);
        Toast.makeText(this, "Date: " + dateMessage,
                Toast.LENGTH_SHORT).show();
        dayOfMonth = day;
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

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

//    @Override
//    protected void onPause() {
//        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
//        super.onPause();
//        preferencesEditor.putInt(DAY_OF_MONTH_KEY, dayOfMonth);
//        preferencesEditor.apply();
//    }

//    public void reset(View view){
//        //reset day to current day
//        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//        //clear preferences
//        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
//        preferencesEditor.clear();
//        preferencesEditor.apply();
//    }
}