package com.example.ravelocator;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumUpdate;

import java.util.List;

public class RaveLocatorRepository {
    private DatumDao datumDao;
    private LiveData<List<Datum>> datum;
    private LiveData<List<Datum>> favorites;

    RaveLocatorRepository(Application application){
        DatumDatabase db = DatumDatabase.getDatabase(application);
        datumDao = db.datumDao();
        datum = datumDao.getAllDatum();
        favorites = datumDao.getAllFavorites();
    }

    LiveData<List<Datum>> getAllDatum(){
        return datum;
    }
    
    public void insertDatum(Datum datum){
        new insertAsyncTask(datumDao).execute(datum);
    }
    public void updateDatumFavorites(DatumUpdate isFavorite){
        new insertAsyncTask(datumDao).execute(isFavorite);
    }
    public LiveData<List<Datum>> getAllFavorites(){
        return favorites;
    }

    private class insertAsyncTask extends AsyncTask<Datum, Void, Void> {
        private DatumDao mAsyncTaskDao;
        public insertAsyncTask(DatumDao datumDao) {
            mAsyncTaskDao = datumDao;
        }
        @Override
        protected Void doInBackground(final Datum... data) {
            mAsyncTaskDao.insertDatum(data[0]);
            return null;
        }

        public void execute(DatumUpdate isFavorite) {
            mAsyncTaskDao.updateDatumFavorites(isFavorite);
        }

    }

}
