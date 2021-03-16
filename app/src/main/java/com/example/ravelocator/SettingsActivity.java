package com.example.ravelocator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class SettingsActivity extends AppCompatActivity {

    RaveLocatorViewModel mRaveLocatorViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getApplication().getApplicationContext());

        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                SharedPreferences sharedPref = PreferenceManager
                        .getDefaultSharedPreferences(getApplication().getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("settings_changed", true);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            SharedPreferences sharedPref = PreferenceManager
                    .getDefaultSharedPreferences(getActivity().getApplication().getApplicationContext());

            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference sync = findPreference("sync");
            sync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getContext(), SplashActivity.class);
                    getActivity().finish();
                    startActivity(i);
                    Toast.makeText(getActivity().getApplicationContext(), "Settings Updated", Toast.LENGTH_LONG).show();
                    return false;
                }
            });

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.clear();
            CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
            constraintBuilder.setValidator(DateValidatorPointForward.now());
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            builder.setCalendarConstraints(constraintBuilder.build());
            builder.setTitleText("SELECT A DATE RANGE");
            builder.setTheme(R.style.MaterialCalendarTheme);
            final MaterialDatePicker materialDatePicker = builder.build();


            Preference filterDates = findPreference("filter_dates");
            String dates_selected = sharedPref.getString("startDate", "") + " - " + sharedPref.getString("endDate", "");
            filterDates.setSummary(dates_selected);
            filterDates.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

                    materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {


                        public void onPositiveButtonClick(Pair<Long, Long> selection) {

                            Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                            utc.setTimeInMillis(selection.first);

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String startDate = format.format(utc.getTime());
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("startDate", startDate);
                            utc.setTimeInMillis(selection.second);
                            String endDate = format.format(utc.getTime());
                            editor.putString("endDate", endDate);
                            editor.apply();
                        }
                    });

                    materialDatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("startDate", "");
                            editor.putString("endDate", "");
                            editor.apply();
                        }
                    });
                    return false;
                }
            });

            Preference manualLocation = findPreference("manual_location");
            String state = sharedPref.getString("state", "");
            manualLocation.setSummary(state);
            manualLocation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), MapsMarkerActivity.class);
                    startActivity(i);
                    return false;
                }
            });
        }
    }

}