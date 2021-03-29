package com.example.ravelocator;

import android.Manifest;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;


import com.example.ravelocator.data.RaveLocatorRepository;
import com.example.ravelocator.model.LocationDataModel;
import com.example.ravelocator.workers.UpdateDatabaseWorker;
import com.example.ravelocator.api.RaveLocatorService;
import com.example.ravelocator.data.DatumDao;
import com.example.ravelocator.model.Datum;
import com.example.ravelocator.model.DatumFavoriteUpdate;
import com.example.ravelocator.model.RaveLocatorModel;
import com.example.ravelocator.model.Venue;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@HiltViewModel
public class RaveLocatorViewModel extends AndroidViewModel {


    private final WorkManager mWorkManager;
    private final SharedPreferences sharedPref;
    private final boolean includeElectronicGenreInd;
    private final boolean includeOtherGenreInd;
    private final boolean livestreamInd;
    private final String state;
    private final String city;
    private final String lat;
    private final String lon;
    private final String startDate;
    private final String endDate;
    private int locationId;
    private final boolean festivalInd;
    private String today;
    private MutableLiveData<RaveLocatorModel> nearbyEventsList;
    @Inject
    public RaveLocatorViewModel(@NonNull Application application) {
        super(application);
        mRepository = new RaveLocatorRepository(application);
        mAllDatum = mRepository.getAllDatum();
        mWorkManager = WorkManager.getInstance(application);
        sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getApplication().getApplicationContext());
        lat = sharedPref.getString("lat", "");
        lon = sharedPref.getString("lon", "");
        includeElectronicGenreInd = sharedPref.getBoolean("include_electronic_shows", true);
        livestreamInd = sharedPref.getBoolean("livestreams_only", false);
        includeOtherGenreInd = sharedPref.getBoolean("include_other_genres", false);
        state = sharedPref.getString("state", "California");
        city = sharedPref.getString("city", "Los Angeles");
        startDate = sharedPref.getString("startDate", "");
        endDate = sharedPref.getString("endDate", "");
        festivalInd = sharedPref.getBoolean("festivals_only", false);

    }
    private final RaveLocatorRepository mRepository;
    private final LiveData<List<Datum>> mAllDatum;
    private Datum getCoordinates(int eventId){
        return mRepository.getDatum(eventId);
    }
    public LiveData<List<Datum>> getAllDatum(){
        return mAllDatum;
    }
    MutableLiveData<RaveLocatorModel> getNearbyEventList(){ return nearbyEventsList;}
    public void insertDatum(Datum datum){ //not used in main activity
        mRepository.insertDatum(datum);
    }
    public void insertVenue(Venue venue) { mRepository.insertVenue(venue);}
    public void updateDatumFavorites(DatumFavoriteUpdate isFavorite){mRepository.updateDatumFavorites(isFavorite);}
    public LiveData<List<Datum>> getAllFavorites(){return mRepository.getAllFavorites(); }
    public List<Datum> search(String query){
        return mRepository.search(query); }

    public MutableLiveData<RaveLocatorModel> getNearbyEvents() {
        locationId = sharedPref.getInt("locationId", 73);
        boolean justAdded = sharedPref.getBoolean("just_added", false);
        if(justAdded){
            Date todayDate = Calendar.getInstance().getTime();
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            today = formatter.format(todayDate);
        } else {
            today = "";
        }
        final MutableLiveData<RaveLocatorModel> mutableLiveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://edmtrain.com")
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        final RaveLocatorService service = retrofit.create(RaveLocatorService.class);
        final Call<RaveLocatorModel> callRequest = service.getRaveLocations(festivalInd,today, today, startDate, endDate, locationId, includeElectronicGenreInd,livestreamInd,includeOtherGenreInd,"cc037aec-dde2-4060-98d9-0d121af42c73");
        callRequest.enqueue(new Callback<RaveLocatorModel>() {
            @Override
            public void onResponse(Call<RaveLocatorModel> call, Response<RaveLocatorModel> response) {
                mutableLiveData.postValue(response.body());
                Log.e("BACON AND", mutableLiveData.toString());

            }

            @Override
            public void onFailure(Call<RaveLocatorModel> call, Throwable t) {
                Log.d("NO", t.getMessage());
                Log.d("YES", call.toString());
            }
        });
        nearbyEventsList = mutableLiveData;
        return mutableLiveData;
    }

    public MutableLiveData<RaveLocatorModel> getAllEvents() {
        final MutableLiveData<RaveLocatorModel> mutableLiveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://edmtrain.com")
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        final RaveLocatorService service = retrofit.create(RaveLocatorService.class);
        final Call<RaveLocatorModel> callRequest = service.getRaveLocations("cc037aec-dde2-4060-98d9-0d121af42c73");
        callRequest.enqueue(new Callback<RaveLocatorModel>() {
            @Override
            public void onResponse(Call<RaveLocatorModel> call, Response<RaveLocatorModel> response) {
                mutableLiveData.postValue(response.body());
                Log.e("BACON AND", mutableLiveData.toString());

            }

            @Override
            public void onFailure(Call<RaveLocatorModel> call, Throwable t) {
                Log.d("NO", t.getMessage());
                Log.d("YES", call.toString());
            }
        });
        nearbyEventsList = mutableLiveData;
        return mutableLiveData;
    }

    public void getLocationId() {
        //Toast.makeText(getApplication().getApplicationContext(),lon, Toast.LENGTH_LONG);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://edmtrain.com")
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        final RaveLocatorService service = retrofit.create(RaveLocatorService.class);
        final Call<LocationDataModel> callRequest;
        callRequest = service.getLocationId(state, city, "cc037aec-dde2-4060-98d9-0d121af42c73");
        callRequest.enqueue(new Callback<LocationDataModel>() {
            @Override
            public void onResponse(Call<LocationDataModel> call, Response<LocationDataModel> response) {
                SharedPreferences.Editor editor = sharedPref.edit();
                if (response.body().getSuccess() == false) {
                    getStateWideLocationId();
                    return;
                } else {
                    editor.putInt("locationId", response.body().getData().get(0).getId());
                    editor.apply();
                    Log.d("GOT LOCATION", response.body().getData().get(0).getState());
                }
            }

            @Override
            public void onFailure(Call<LocationDataModel> call, Throwable t) {
                Log.d("Get Location Failed", t.getMessage());
                Log.d("Get Location Failed", call.toString());
            }
        });
    }
    public void getStateWideLocationId() {

        //Toast.makeText(getApplication().getApplicationContext(),lon, Toast.LENGTH_LONG);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://edmtrain.com")
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        final RaveLocatorService service = retrofit.create(RaveLocatorService.class);
        final Call<LocationDataModel> callRequest;
        callRequest = service.getLocationId(state,  "cc037aec-dde2-4060-98d9-0d121af42c73");
        callRequest.enqueue(new Callback<LocationDataModel>() {
            @Override
            public void onResponse(Call<LocationDataModel> call, Response<LocationDataModel> response) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("locationId", response.body().getData().get(0).getId());
                editor.apply();
                Log.d("GOT LOCATION", response.body().getData().get(0).getState());
            }

            @Override
            public void onFailure(Call<LocationDataModel> call, Throwable t) {
                Log.d("Get Location Failed", t.getMessage());
                Log.d("Get Location Failed", call.toString());
            }
        });
    }





    void updateDatabase(){
        if(getAllDatum() == null) {
            mWorkManager.enqueue(OneTimeWorkRequest.from(UpdateDatabaseWorker.class));
        } else {
            PeriodicWorkRequest updateDatabase = new PeriodicWorkRequest.Builder(UpdateDatabaseWorker.class, 24, TimeUnit.HOURS)
                    .build();
            mWorkManager.enqueue(updateDatabase);
            Toast.makeText(getApplication(), "Updating Database", Toast.LENGTH_SHORT).show();
        }
    }




}

