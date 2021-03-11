package com.example.ravelocator;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumFTS;
import com.example.ravelocator.util.DatumUpdate;
import com.example.ravelocator.util.Venue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RaveLocatorRepository {
    private DatumDao datumDao;
    private LiveData<List<Datum>> datum;
    private LiveData<List<Datum>> favorites;
    private LiveData<List<Venue>> venue;
    private DatumWithVenue datumWithVenue;

    public RaveLocatorRepository(Application application){
        DatumDatabase db = DatumDatabase.getDatabase(application);
        datumDao = db.datumDao();
        datum = datumDao.getAllDatum();
        favorites = datumDao.getAllFavorites();
    }

    LiveData<List<Datum>> getAllDatum(){
        return datum;
    }
    DatumWithVenue getVenueOfDatum(int id) {return datumDao.getVenueOfDatum(id);}
    List<VenueWithDatum> getDatumOfVenue(String venueId){ return datumDao.getDatumOfVenue(venueId);}
    public void insertDatum(Datum datum){
        new insertAsyncTask(datumDao).execute(datum);
    }
    public void insertVenue(Venue venue){
        new insertAsyncTask(datumDao).execute(venue);
    }
    public void insertDatumVenueCrossRef(DatumVenueCrossRef crossRef){ new insertAsyncTask(datumDao).execute(crossRef);}
    public void updateDatumFavorites(DatumUpdate isFavorite){
        new insertAsyncTask(datumDao).execute(isFavorite);
    }
    public LiveData<List<Datum>> getAllFavorites(){
        return favorites;
    }
    public List<Datum> search(String query){
        return datumDao.search(query);}


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

        public void execute(Venue venue) {
            mAsyncTaskDao.insertVenue(venue);
        }

        public void execute(DatumVenueCrossRef crossRef) { mAsyncTaskDao.insertDatumVenueCrossRef(crossRef);
        }

    }
}
