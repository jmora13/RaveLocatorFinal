package com.example.ravelocator;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.ravelocator.util.ArtistList;
import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumUpdate;
import com.example.ravelocator.util.Venue;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface DatumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRaveLocatorModel(Datum datum);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDatum(Datum datum);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVenue(Venue venue);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArtistList(List<ArtistList> artistList);

    @Query("SELECT * from datum_table")
    LiveData<List<Datum>> getAllDatum();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Datum datum);

    @Delete
    void delete(Datum datum);

    @Update(entity = Datum.class)
    void updateDatumFavorites(DatumUpdate isFavorite);

    @Query("SELECT * FROM datum_table WHERE isFavorite = 1")
    LiveData<List<Datum>> getAllFavorites();

    @Transaction
    @Query("SELECT * FROM datum_table WHERE id = :vId")
    List<DatumAndVenue> getDatumAndVenueWithId(Integer vId);

    @Transaction
    @Query("SELECT * FROM datum_table WHERE id = :aId")
    List<DatumWithArtistList> getDatumWithArtistList(Integer aId);
    @Query("DELETE FROM datum_table")
    void deleteAll();

}