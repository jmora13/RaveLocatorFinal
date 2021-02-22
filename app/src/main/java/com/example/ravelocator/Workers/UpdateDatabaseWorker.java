package com.example.ravelocator.Workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ravelocator.DatumDatabase;
import com.example.ravelocator.NearbyFragment;
import com.example.ravelocator.RaveLocatorRepository;
import com.example.ravelocator.RaveLocatorViewModel;

public class UpdateDatabaseWorker extends Worker {
    RaveLocatorViewModel mRaveLocatorViewModel;
    public UpdateDatabaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        DatumDatabase database = DatumDatabase.getDatabase(getApplicationContext());
       // RaveLocatorRepository repository = new RaveLocatorRepository(database);

        return null;
    }
}
