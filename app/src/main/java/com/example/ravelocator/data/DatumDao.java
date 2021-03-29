package com.example.ravelocator.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.ravelocator.model.ArtistList;
import com.example.ravelocator.model.Datum;
import com.example.ravelocator.model.DatumCoordinatesUpdate;
import com.example.ravelocator.model.DatumFavoriteUpdate;
import com.example.ravelocator.model.DatumLocationUpdate;
import com.example.ravelocator.model.DatumVenueUpdate;
import com.example.ravelocator.model.RaveLocatorModel;
import com.example.ravelocator.model.Venue;

import java.util.List;

@Dao
public interface DatumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRaveLocatorModel(Datum datum);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertDatum(Datum datum);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVenue(Venue venue);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArtistList(List<ArtistList> artistList);

    @Query("SELECT * FROM datum_table ORDER BY date")
    LiveData<List<Datum>> getAllDatum();

    @Query("SELECT * FROM datum_table JOIN datum_fts ON datum_table.id = datum_fts.id WHERE datum_fts MATCH :query ORDER BY date")
    List<Datum>search(String query);

    @Query("SELECT * FROM datum_table WHERE id =:eventId")
    Datum getDatum(int eventId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Datum datum);

    @Delete
    void delete(Datum datum);

    @Query("DELETE FROM datum_table WHERE date < :currentDate")
    void deletePastDates(String currentDate);

    @Update(entity = Datum.class)
    void updateDatumFavorites(DatumFavoriteUpdate isFavorite);

    @Update(entity = Datum.class)
    void updateDatumVenueName(DatumVenueUpdate venueName);

    @Update(entity = Datum.class)
    void updateDatumLocation(DatumLocationUpdate location);

    @Update(entity = Datum.class)
    void updateDatumCoordinates(DatumCoordinatesUpdate coordinates);

    @Query("SELECT * FROM datum_table WHERE isFavorite = 1 ORDER BY date")
    LiveData<List<Datum>> getAllFavorites();

    @Query("DELETE FROM datum_table")
    void deleteAll();



}
