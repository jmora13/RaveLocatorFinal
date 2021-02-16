package com.example.ravelocator;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"id", "venueName"})
public class DatumVenueCrossRef {
    public int id;
    @NonNull
    public String venueName;

    public DatumVenueCrossRef(int id, String venueName){
        this.id = id;
        this.venueName = venueName;
    }
}
