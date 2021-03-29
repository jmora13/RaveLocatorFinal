package com.example.ravelocator.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.ravelocator.model.ArtistList;
import com.example.ravelocator.model.Datum;
import com.example.ravelocator.model.DatumCoordinatesUpdate;
import com.example.ravelocator.model.DatumFTS;
import com.example.ravelocator.model.DatumFavoriteUpdate;
import com.example.ravelocator.model.DatumLocationUpdate;
import com.example.ravelocator.model.DatumVenueUpdate;
import com.example.ravelocator.model.Venue;

@Database(
        entities = {
                Datum.class,
                Venue.class,
                ArtistList.class,
                DatumFavoriteUpdate.class,
                DatumFTS.class,
                DatumVenueUpdate.class,
                DatumLocationUpdate.class,
                DatumCoordinatesUpdate.class},
        version = 13)
@TypeConverters({Converters.class})
public abstract class DatumDatabase extends RoomDatabase {

private static DatumDatabase INSTANCE;

    public abstract DatumDao datumDao();

    public synchronized static DatumDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), DatumDatabase.class, "datum_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(sRoomDatabaseCallback)
                    .allowMainThreadQueries()
                    .build();
            INSTANCE.populateInitialData();
        }
        return INSTANCE;
    }

    private void populateInitialData() {
            runInTransaction(() -> {
                new Datum();
            });
    }

        private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    db.execSQL("INSERT INTO datum_fts(datum_fts) VALUES ('rebuild')");
                }
            };
}
