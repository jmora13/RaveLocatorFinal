package com.example.ravelocator.workers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ravelocator.data.DatumDatabase;
import com.example.ravelocator.api.RaveLocatorService;
import com.example.ravelocator.model.Datum;
import com.example.ravelocator.model.DatumCoordinatesUpdate;
import com.example.ravelocator.model.DatumLocationUpdate;
import com.example.ravelocator.model.DatumVenueUpdate;
import com.example.ravelocator.model.RaveLocatorModel;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class UpdateDatabaseWorker extends Worker {

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
        return null;
    }

    public void requestRaveLocations(DatumDatabase database) {
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
                List<Datum> datum = response.body().getData();
                for(int i = 0; i <datum.size(); i++){
                    database.datumDao().insertDatum(datum.get(i));
                    database.datumDao().insertVenue(datum.get(i).getVenue());
                    database.datumDao().updateDatumVenueName(new DatumVenueUpdate(datum.get(i).getId(),datum.get(i).getVenue().getVenueName()));
                    database.datumDao().updateDatumLocation(new DatumLocationUpdate(datum.get(i).getId(), datum.get(i).getVenue().getLocation()));
                    if(!datum.get(i).getLivestreamInd()) {
                        database.datumDao().updateDatumCoordinates(new DatumCoordinatesUpdate(datum.get(i).getId(),
                                datum.get(i).getVenue().getLatitude() + ", "
                                        + datum.get(i).getVenue().getLongitude() ));
                    }
                }
                mutableLiveData.postValue(response.body());
                Log.e("BACON AND", "potatoes");

            }

            @Override
            public void onFailure(Call<RaveLocatorModel> call, Throwable t) {
                Log.d("Database not updated", t.getMessage());
                Log.d("YES", call.toString());
            }
        });
    }


}
