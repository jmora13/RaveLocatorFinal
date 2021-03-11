package com.example.ravelocator;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.ravelocator.util.ArtistList;
import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumFTS;
import com.example.ravelocator.util.DatumUpdate;
import com.example.ravelocator.util.Venue;

@Database(
        entities = {
                Datum.class,
                Venue.class,
                ArtistList.class,
                DatumUpdate.class,
                DatumFTS.class,
        DatumVenueCrossRef.class},
        version = 6)
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

        private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    db.execSQL("INSERT INTO datum_fts(datum_fts) VALUES ('rebuild')");
                }
            };

//    private static DatumDatabase buildDatabase(final Context context) {
//        return Room.databaseBuilder(context,
//                DatumDatabase.class,
//                "datum_database")
//                .addCallback(new Callback() {
//                    @Override
//                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
//                        super.onOpen(db);
//                    }
//
//                    @Override
//                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                        super.onCreate(db);
//
//                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                getDatabase(context).datumDao().insertDatum(Datum.populateData());
//                                Log.e("Done", "done");
//                            }
//                        });
//                    }
//                })
//                .build();
//    }
}
