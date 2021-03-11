package com.example.ravelocator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.ravelocator.util.Datum;

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
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getApplication().getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
//            final RaveLocatorAdapter adapter = new RaveLocatorAdapter(getActivity(), this::onListItemClick);
//            setPreferencesFromResource(R.xml.root_preferences, rootKey);
//            EditTextPreference sync = findPreference("sync");
//            sync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    adapter.notifyDataSetChanged();
//                    return false;
//                }
//            });
//        }
//
//
//        private void onListItemClick(Datum datum) {
//        }
    }
}