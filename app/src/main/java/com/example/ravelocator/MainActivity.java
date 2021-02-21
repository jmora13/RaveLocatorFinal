package com.example.ravelocator;

import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumUpdate;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


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
//        if(savedInstanceState != null){
//            dayOfMonth = mPreferences.getInt(DAY_OF_MONTH_KEY,0);
//            if(dayOfMonth == 0){
//                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//            }
//
//        }
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
//        Toolbar myToolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(myToolbar);
        mRaveLocatorViewModel = new ViewModelProvider(this).get(RaveLocatorViewModel.class);
        //getSupportActionBar().setTitle(city);
        //myToolbar.setTitle(city);
        createNotificationChannel();
        DataFragmentAdapter ad = new DataFragmentAdapter(this);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(ad);
        viewPager.setOffscreenPageLimit(2);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            viewPager.setCurrentItem(tab.getPosition(), true);
            if(position == 0) {
                tab.setText("Nearby");
                tab.setIcon(R.drawable.ic_baseline_location_on_24_white);
            }
            if(position == 1){
                tab.setText("Search");
                tab.setIcon(R.drawable.ic_baseline_search_24_white);
            }
            if(position == 2){
                tab.setText("Favorites");
                tab.setIcon(R.drawable.ic_baseline_favorite_24_white);
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
//                View view = viewPager.getChildAt(position);
//                //if (view == null) return;
//                int wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
//                int hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                view.measure(wMeasureSpec,hMeasureSpec);
//
//                if(viewPager.getLayoutParams().height != view.getMeasuredHeight()){
//                    ViewGroup.LayoutParams lp = viewPager.getLayoutParams();
//                    lp.height = view.getMeasuredHeight();
//                }
//            }
//        });

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


//    private void updatePagerHeightForChild(View view, ViewPager2 viewPager){
//        int wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
//        int hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        view.measure(wMeasureSpec,hMeasureSpec);
//
//        if(viewPager.getLayoutParams().height != view.getMeasuredHeight()){
//            viewPager.setLayoutParams(view.getLayoutParams());
//        }
//    }
public void onListItemClick(Datum datum) {
    //Toast.makeText(this, datum., Toast.LENGTH_SHORT).show();
//        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
//        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    DatumUpdate isFavorite;
    if(datum.getFavorite() == false) {
        isFavorite = new DatumUpdate(datum.getId(), true);
    } else {
        isFavorite = new DatumUpdate(datum.getId(), false);
    }
    mRaveLocatorViewModel.updateDatumFavorites(isFavorite);
}
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Get the SearchView and set the searchable configuration
//        SearchManager searchManager = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        }
//        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        // Assumes current activity is the searchable activity
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return true;
    }

//    public void setActionBarTitle(String title) {
//        getSupportActionBar().setTitle(title);
//    }
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch(item.getItemId()){
//            case R.id.action_calendar:
//                DialogFragment newFragment = new DatePickerFragment();
//                newFragment.show(getSupportFragmentManager(),"datePicker");
//        }
//        return super.onOptionsItemSelected(item);
//    }

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