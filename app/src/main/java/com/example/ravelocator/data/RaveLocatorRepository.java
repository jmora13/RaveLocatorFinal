package com.example.ravelocator.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ravelocator.model.Datum;
import com.example.ravelocator.model.DatumFavoriteUpdate;
import com.example.ravelocator.model.DatumVenueUpdate;
import com.example.ravelocator.model.RaveLocatorModel;
import com.example.ravelocator.model.Venue;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RaveLocatorRepository {
    private final DatumDao datumDao;
    private final LiveData<List<Datum>> datum;
    private final LiveData<List<Datum>> favorites;

    @Inject
    public RaveLocatorRepository(Application application){
        DatumDatabase db = DatumDatabase.getDatabase(application);
        datumDao = db.datumDao();
        datum = datumDao.getAllDatum();
        favorites = datumDao.getAllFavorites();
    }

    public LiveData<List<Datum>> getAllDatum(){
        return datumDao.getAllDatum();
    }
    public void insertDatum(Datum datum){
        new insertAsyncTask(datumDao).execute(datum);
    }
    public void insertVenue(Venue venue){
        new insertAsyncTask(datumDao).execute(venue);
    }
    public void updateDatumFavorites(DatumFavoriteUpdate isFavorite){
        new insertAsyncTask(datumDao).execute(isFavorite);
    }

    public Datum getDatum(int eventId){
       return datumDao.getDatum(eventId);
    }

    public void updateDatumVenueName(DatumVenueUpdate venueName){
        new insertAsyncTask(datumDao).execute(venueName);
    }

    public LiveData<List<Datum>> getAllFavorites(){
        return favorites;
    }
    public List<Datum> search(String query){
        return datumDao.search(query);}


    private class insertAsyncTask extends AsyncTask<Datum, Void, Void> {
        private final DatumDao mAsyncTaskDao;
        public insertAsyncTask(DatumDao datumDao) {
            mAsyncTaskDao = datumDao;
        }
        @Override
        protected Void doInBackground(final Datum... data) {
            mAsyncTaskDao.insertDatum(data[0]);
            return null;
        }

        public void execute(DatumFavoriteUpdate isFavorite) {
            mAsyncTaskDao.updateDatumFavorites(isFavorite);
        }

        public void execute(Venue venue) {
            mAsyncTaskDao.insertVenue(venue);
        }

        public void execute(int eventId) { mAsyncTaskDao.getDatum(eventId); }


        public void execute(DatumVenueUpdate venueName) {
            mAsyncTaskDao.updateDatumVenueName(venueName);
        }

    }
}
