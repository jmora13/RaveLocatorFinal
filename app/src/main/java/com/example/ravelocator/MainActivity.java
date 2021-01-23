package com.example.ravelocator;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity  {
    private RaveLocatorViewModel mRaveLocatorViewModel;
    private Object Menu;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.ravelocator";
    Calendar calendar = Calendar.getInstance();
    private int dayOfMonth = 0;
    private final String DAY_OF_MONTH_KEY = "1";
    ViewPager2 viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(savedInstanceState != null){
            dayOfMonth = mPreferences.getInt(DAY_OF_MONTH_KEY,0);
            if(dayOfMonth == 0){
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            }

        }
//        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open,
//                R.string.navigation_drawer_close);
//        if (drawer != null) {
//            drawer.addDrawerListener(toggle);
//        }
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        if (navigationView != null) {
//            navigationView.setNavigationItemSelectedListener(this::onOptionsItemSelected);
//        }
        //Toolbar myToolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(myToolbar);
        createNotificationChannel();
        DataFragmentAdapter ad = new DataFragmentAdapter(this);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(ad);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            viewPager.setCurrentItem(tab.getPosition(), true);
            if(position == 0) {
                tab.setText("Nearby");
                tab.setIcon(R.drawable.ic_outline_location_on_24);
            }
            if(position == 1){
                tab.setText("Favorites");
                tab.setIcon(R.drawable.ic_baseline_favorite_24);
            }
        }).attach();


//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                tabLayout.selectTab(tabLayout.getTabAt(position));
//            }
//        });
//        public boolean onOptionsItemSelected(MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.action_sign_out: {
//                    // do your sign-out stuff
//                    break;
//                }
//                default:
//                    // If we got here, the user's action was not recognized.
//                    // Invoke the superclass to handle it.
//                    return super.onOptionsItemSelected(item);
//            }
//            return true;
//        }

    }

//    /**
//     * Handles the Back button: closes the nav drawer.
//     */
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer != null) {
//            if (drawer.isDrawerOpen(GravityCompat.START)) {
//                drawer.closeDrawer(GravityCompat.START);
//            } else {
//                super.onBackPressed();
//            }
//        }
//    }



    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_calendar:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(),"datePicker");
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onPause() {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        super.onPause();
        preferencesEditor.putInt(DAY_OF_MONTH_KEY, dayOfMonth);
        preferencesEditor.apply();
    }

    public void reset(View view){
        //reset day to current day
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        //clear preferences
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }
}