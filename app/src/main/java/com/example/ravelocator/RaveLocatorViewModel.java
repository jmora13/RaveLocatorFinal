package com.example.ravelocator;

import android.Manifest;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumUpdate;
import com.example.ravelocator.util.RaveLocatorModel;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class RaveLocatorViewModel extends AndroidViewModel {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public double lat, lon;
    private final String TAG = getClass().getSimpleName();
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.ravelocator";
    private String day;
    public RaveLocatorViewModel(@NonNull Application application) {
        super(application);
        mRepository = new RaveLocatorRepository(application);
        mAllDatum = mRepository.getAllDatum();
//        SharedPreferences sharedPref = getApplication().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
//        day = Integer.toString(sharedPref.getInt("1", 0));
//        Log.e("Day of month value", day);
    }
    private RaveLocatorRepository mRepository;
    private LiveData<List<Datum>> mAllDatum;

    LiveData<List<Datum>> getAllDatum(){
        return mAllDatum;
    }
    public void insertDatum(Datum datum){ //not used in main activity
        mRepository.insertDatum(datum);
    }
    public void updateDatumFavorites(DatumUpdate isFavorite){mRepository.updateDatumFavorites(isFavorite);}
    public LiveData<List<Datum>> getAllFavorites(){return mRepository.getAllFavorites(); }
    public MutableLiveData<RaveLocatorModel> requestRaveLocations() {
        final MutableLiveData<RaveLocatorModel> mutableLiveData = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://edmtrain.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        final RaveLocatorService service = retrofit.create(RaveLocatorService.class);
        final Call<RaveLocatorModel> callRequest = service.getRaveLocations("2021-01-30" , "cc037aec-dde2-4060-98d9-0d121af42c73");
        callRequest.enqueue(new Callback<RaveLocatorModel>() {
            @Override
            public void onResponse(Call<RaveLocatorModel> call, Response<RaveLocatorModel> response) {
                String data = response.body().toString();
                //mAllDatum = response.body().getData();
                List<Datum> datum = response.body().getData();
                for(int i =0; i <datum.size(); i++){
                    insertDatum(datum.get(i));
                }
                mutableLiveData.setValue(response.body());
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

