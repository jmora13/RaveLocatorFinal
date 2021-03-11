package com.example.ravelocator.Workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ravelocator.DatumDatabase;
import com.example.ravelocator.DatumVenueCrossRef;
import com.example.ravelocator.RaveLocatorService;
import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.RaveLocatorModel;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class GetNearbyEventsWorker extends Worker {
    boolean includeElectronicGenreInd;
    boolean includeOtherGenreInd;
    boolean livestreamInd;
    public double lat, lon;
    public GetNearbyEventsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        DatumDatabase database = DatumDatabase.getDatabase(getApplicationContext());
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        String lat = sharedPref.getString("lat", "");
        String lon = sharedPref.getString("lon", "");
        includeElectronicGenreInd = sharedPref.getBoolean("include_electronic_shows", true);
        livestreamInd = sharedPref.getBoolean("livestreams_only", false);
        includeOtherGenreInd = sharedPref.getBoolean("include_other_genres", false);
        requestRaveLocations(database);

        return null;
    }

    public MutableLiveData<RaveLocatorModel> requestRaveLocations(DatumDatabase database) {
        final MutableLiveData<RaveLocatorModel> mutableLiveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://edmtrain.com")
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        final RaveLocatorService service = retrofit.create(RaveLocatorService.class);
        final Call<RaveLocatorModel> callRequest = service.getRaveLocations(Double.valueOf(lat), Double.valueOf(lon),"California",includeElectronicGenreInd,livestreamInd,includeOtherGenreInd,"cc037aec-dde2-4060-98d9-0d121af42c73");
        callRequest.enqueue(new Callback<RaveLocatorModel>() {
            @Override
            public void onResponse(Call<RaveLocatorModel> call, Response<RaveLocatorModel> response) {
                String data = response.body().toString();
                List<Datum> datum = response.body().getData();
                mutableLiveData.postValue(response.body());
                Log.e("BACON AND", mutableLiveData.toString());

            }

            @Override
            public void onFailure(Call<RaveLocatorModel> call, Throwable t) {
                Log.d("NO", t.getMessage());
                Log.d("YES", call.toString());
            }
        });
        return mutableLiveData;
    }


}
