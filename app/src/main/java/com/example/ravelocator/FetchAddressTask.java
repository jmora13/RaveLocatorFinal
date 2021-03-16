package com.example.ravelocator;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * AsyncTask for reverse geocoding coordinates into a physical address.
 */
class FetchAddressTask extends AsyncTask<Location, Void, String[]> {

    private Context mContext;
    private OnTaskCompleted mListener;


    FetchAddressTask(Context applicationContext, OnTaskCompleted listener) {
        mContext = applicationContext;
        mListener = listener;
    }

    private final String TAG = FetchAddressTask.class.getSimpleName();

    @Override
    protected String[] doInBackground(Location... params) {
        String[] cityAndState = new String[2];
        // Set up the geocoder
        Geocoder geocoder = new Geocoder(mContext,
                Locale.getDefault());

        // Get the passed in location
        Location location = params[0];
        List<Address> addresses = null;
        String resultMessage = "";

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address
                    1);

        } catch (IOException ioException) {
            // Catch network or other I/O problems
            resultMessage = mContext.getString(R.string.service_not_available);
            Log.e(TAG, resultMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values
            resultMessage = mContext.getString(R.string.invalid_lat_long_used);
            Log.e(TAG, resultMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // If no addresses found, print an error message.
        if (addresses == null || addresses.size() == 0) {
            if (resultMessage.isEmpty()) {
                resultMessage = mContext.getString(R.string.no_address_found);
                Log.e(TAG, resultMessage);
            }
        } else {
            String city;
            if(addresses.get(0).getLocality() == null){
                city = "null";
            } else {
                city = addresses.get(0).getLocality();
            }
            String state = addresses.get(0).getAdminArea();
            cityAndState[0] = city;
            cityAndState[1] = state;
        }

        return cityAndState;
    }

    /**
     * Called once the background thread is finished and updates the
     * UI with the result.
     * @param cityAndState The resulting reverse geocoded address, or error
     *                message if the task failed.
     */
    @Override
    protected void onPostExecute(String[] cityAndState) {
        mListener.onTaskCompleted(cityAndState);
        super.onPostExecute(cityAndState);
    }

    interface OnTaskCompleted {
        void onTaskCompleted(String[] result);
    }
}
