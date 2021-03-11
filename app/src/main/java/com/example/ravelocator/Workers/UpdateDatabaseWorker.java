package com.example.ravelocator.Workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ravelocator.DatumDatabase;
import com.example.ravelocator.DatumVenueCrossRef;
import com.example.ravelocator.MainActivity;
import com.example.ravelocator.NearbyFragment;
import com.example.ravelocator.RaveLocatorRepository;
import com.example.ravelocator.RaveLocatorService;
import com.example.ravelocator.RaveLocatorViewModel;
import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.RaveLocatorModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class UpdateDatabaseWorker extends Worker {
    RaveLocatorViewModel mRaveLocatorViewModel;
    public UpdateDatabaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        DatumDatabase database = DatumDatabase.getDatabase(getApplicationContext());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(System.currentTimeMillis());
        database.datumDao().deletePastDates(date);
        requestRaveLocations(database);

        return Result.success();
    }

    public MutableLiveData<RaveLocatorModel> requestRaveLocations(DatumDatabase database) {
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
                String data = response.body().toString();
                List<Datum> datum = response.body().getData();
                for(int i = 0; i <datum.size(); i++){
                    database.datumDao().insertDatum(datum.get(i));
                    database.datumDao().insertVenue(datum.get(i).getVenue());
                    database.datumDao().insertDatumVenueCrossRef(new DatumVenueCrossRef(datum.get(i).getId(), datum.get(i).getVenue().getVenueName()));
                }
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
