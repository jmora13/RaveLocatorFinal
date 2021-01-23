package com.example.ravelocator;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.ravelocator.util.ArtistList;
import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumUpdate;
import com.example.ravelocator.util.Venue;

@Database(
        entities = {
                Datum.class,
                Venue.class,
                ArtistList.class,
                DatumUpdate.class},
        version = 3)
@TypeConverters({Converters.class})
public abstract class DatumDatabase extends RoomDatabase {
//    public abstract DatumDao datumDao();
//
//    private static DatumDatabase INSTANCE;
//
//    public static DatumDatabase getDatabase(Context context) {
//
//        if (INSTANCE == null) {
//            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatumDatabase.class, "datum_database")
//                    .allowMainThreadQueries()
//                    .build();
//
//        }
//        return INSTANCE;
//    }
//}
//    private static DatumDatabase INSTANCE;
//    public static DatumDatabase getDatabase(final Context context){
//        if(INSTANCE == null){
//            synchronized (DatumDatabase.class){
//                if(INSTANCE == null){
//                    //create database here
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                            DatumDatabase.class, "Datum_database")
//                            .fallbackToDestructiveMigration().addCallback(sRoomDatabaseCallback).build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//    public abstract DatumDao datumDao();
//
//    private static RoomDatabase.Callback sRoomDatabaseCallback =
//            new RoomDatabase.Callback(){
//
//                @Override
//                public void onOpen(@NonNull SupportSQLiteDatabase db) {
//                    super.onOpen(db);
//                    new PopulateDbAsync(INSTANCE).execute();
//                }
//            };
//
//    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
//        private final DatumDao mDao;
//        //String[] Datums = {"dolphin", "crocodile","cobra"};
//        //Datum datumTest = new Datum(123, "456");
//        public PopulateDbAsync(DatumDatabase db) {
//            mDao = db.datumDao();
//        }
//        @Override
//        protected Void doInBackground(final Void... params) {
//             Start the Datum with a clean database every time.
//             Not needed if you only populate on creation.
  //          mDao.deleteAll();

                //Datum datum = new Datum(155432);
//                mDao.insertDatum(datum);
//                mDao.insertArtistList(datum.getArtistList());
//                mDao.insertVenue(datum.getVenue());
//            fillWithStartingData(activity,mDao);
//            JSONArray datum = loadJSONArray(activity);
//            try{
//                for(int i = 0; i < datum.length(); i++){
//                    JSONObject data = datum.getJSONObject(i);
//                    Integer datumId = data.getInt("id");
//                    String datumName = data.getString("name");
//                    Log.d("datum name", datumName);
//                    Datum datumTest = new Datum(123, "456");
//                    mDao.insertDatum(new Datum(datumId,datumName));
//                    mDao.insertDatum(new Datum(23233,"rawr"));
//                    mDao.insertDatum(datumTest);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            mDao.insertDatum(Datum.populateData());
//           // mDao.insertDatum(datumTest);
//            mDao.getAllDatum();
//            return null;
//        }
//    }

//    private static void fillWithStartingData(Context context, DatumDao mDao) {
//         //DatumDao dao = getDatabase(context).datumDao();
//
//         JSONArray datum = loadJSONArray(context);
//         try{
//             for(int i = 0; i < datum.length(); i++){
//                 JSONObject data = datum.getJSONObject(i);
//                 Integer datumId = data.getInt("id");
//                 String datumName = data.getString("name");
//                 Log.d("datum name", datumName);
//                 mDao.insertDatum(new Datum(datumId,datumName));
//             }
//         } catch (JSONException e) {
//             e.printStackTrace();
//         }
//
//    }
//
//    private static JSONArray loadJSONArray(Context context){
//        StringBuilder builder = new StringBuilder();
//        InputStream in = context.getResources().openRawResource(R.raw.response);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//
//        String line;
//
//        try{
//            while ((line = reader.readLine()) != null){
//                builder.Datumend(line);
//            }
//            JSONObject json = new JSONObject(builder.toString());
//            return json.getJSONArray("data");
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
private static DatumDatabase INSTANCE;

    public abstract DatumDao datumDao();

    public synchronized static DatumDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), DatumDatabase.class, "datum_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
            INSTANCE.populateInitialData();
        }
        return INSTANCE;
    }

    private void populateInitialData() {
            runInTransaction(new Runnable() {
                @Override
                public void run() {
                    Datum datum = new Datum();
                    //datumDao().insertDatum(Datum.populateData());
                }
            });
    }

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
