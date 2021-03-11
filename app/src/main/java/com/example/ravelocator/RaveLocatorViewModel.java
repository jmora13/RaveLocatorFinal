package com.example.ravelocator;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.ravelocator.ReverseGeocoding.ReverseGeocodingModel;
import com.example.ravelocator.Workers.UpdateDatabaseWorker;
import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumUpdate;
import com.example.ravelocator.util.RaveLocatorModel;
import com.example.ravelocator.util.Venue;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class RaveLocatorViewModel extends AndroidViewModel {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private DatumDao datumDao;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final String TAG = getClass().getSimpleName();
    private String sharedPrefFile = "sharedPrefFile";
    private String day;
    private List<Datum> searchResults;
    private DatumVenueCrossRef crossRef;
    private LiveData<DatumWithVenue> datumWithVenue;
    private MutableLiveData<RaveLocatorModel> mutableLiveData = new MutableLiveData<>();
    private WorkManager mWorkManager;
    private FusedLocationProviderClient fusedLocationClient;
    private String latitude;
    private String longitude;
    private SharedPreferences sharedPref;
    private boolean includeElectronicGenreInd;
    private boolean includeOtherGenreInd;
    private boolean livestreamInd;
    private  String state;
    private String lat;
    private String lon;
    private MutableLiveData<RaveLocatorModel> nearbyEventsList;
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
//        SharedPreferences sharedPref = getApplication().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
//        day = Integer.toString(sharedPref.getInt("1", 0));
//        Log.e("Day of month value", day);
    }
    private RaveLocatorRepository mRepository;
    private LiveData<List<Datum>> mAllDatum;
    private LiveData<List<Datum>> searchQuery;
    LiveData<List<Datum>> getAllDatum(){
        return mAllDatum;
    }
    MutableLiveData<RaveLocatorModel> getNearbyEventList(){ return nearbyEventsList;}
    public void insertDatum(Datum datum){ //not used in main activity
        mRepository.insertDatum(datum);
    }
    public void insertVenue(Venue venue) { mRepository.insertVenue(venue);}
    public void insertDatumVenueCrossRef(DatumVenueCrossRef crossRef){mRepository.insertDatumVenueCrossRef(crossRef);}
    DatumWithVenue getVenueOfDatum(int id) {return mRepository.getVenueOfDatum(id);}
    List<VenueWithDatum> getDatumOfVenue(String venueName){return mRepository.getDatumOfVenue(venueName);}
    public void updateDatumFavorites(DatumUpdate isFavorite){mRepository.updateDatumFavorites(isFavorite);}
    public LiveData<List<Datum>> getAllFavorites(){return mRepository.getAllFavorites(); }
    public List<Datum> search(String query){
        return mRepository.search(query); }

    public MutableLiveData<RaveLocatorModel> getNearbyEvents() {

        //Toast.makeText(getApplication().getApplicationContext(),lon, Toast.LENGTH_LONG);
        final MutableLiveData<RaveLocatorModel> mutableLiveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://edmtrain.com")
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        final RaveLocatorService service = retrofit.create(RaveLocatorService.class);
        final Call<RaveLocatorModel> callRequest = service.getRaveLocations(Double.valueOf(lat), Double.valueOf(lon),state,includeElectronicGenreInd,livestreamInd,includeOtherGenreInd,"cc037aec-dde2-4060-98d9-0d121af42c73");
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
        nearbyEventsList = mutableLiveData;
        return mutableLiveData;
    }


    public void reverseGeocoding() {

        sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getApplication().getApplicationContext());
        lat = sharedPref.getString("lat", "");
        lon = sharedPref.getString("lon", "");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://forward-reverse-geocoding.p.rapidapi.com")
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        final RaveLocatorService service = retrofit.create(RaveLocatorService.class);
        final Call<ReverseGeocodingModel> callRequest = service.reverseGeocoding(Double.valueOf(lat), Double.valueOf(lon), "json", 5);
        callRequest.enqueue(new Callback<ReverseGeocodingModel>() {
            @Override
            public void onResponse(Call<ReverseGeocodingModel> call, Response<ReverseGeocodingModel> response) {
                String data = response.body().toString();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("state", response.body().getAddress().getState());
                editor.apply();
                //                List<Datum> datum = response.body().getData();
               // mutableLiveData.postValue(response.body());
                Log.e("HAM AND", mutableLiveData.toString());

            }

            @Override
            public void onFailure(Call<ReverseGeocodingModel> call, Throwable t) {
                Log.d("NO", t.getMessage());
                Log.d("YES", call.toString());
            }
        });
    }
    void updateDatabase(){
        if(mAllDatum == null) {
            mWorkManager.enqueue(OneTimeWorkRequest.from(UpdateDatabaseWorker.class));
        } else {
            PeriodicWorkRequest updateDatabase = new PeriodicWorkRequest.Builder(UpdateDatabaseWorker.class, 24, TimeUnit.HOURS)
                    .build();
            mWorkManager.enqueue(updateDatabase);
            Toast.makeText(getApplication(), "Updating Database", Toast.LENGTH_SHORT).show();
        }
    }




}

