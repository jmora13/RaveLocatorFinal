package com.example.ravelocator.data;

import androidx.room.TypeConverter;

import com.example.ravelocator.model.ArtistList;
import com.example.ravelocator.model.Venue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Converters {
    @TypeConverter
    public static String fromVenue(Venue venue) {
        return venue == null ? null : "";
    }

    @TypeConverter
    public static Venue venueFromString(String s) {
        return s == null ? null : new Venue();
    }

    @TypeConverter
    public static List<ArtistList> restoreList(String artistList) {
        return new Gson().fromJson(artistList, new TypeToken<List<ArtistList>>() {
        }.getType());
    }

    @TypeConverter
    public static String saveListOfString(List<ArtistList> artistList) {
        return new Gson().toJson(artistList);
    }
}
