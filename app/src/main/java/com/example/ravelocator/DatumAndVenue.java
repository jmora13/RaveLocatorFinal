package com.example.ravelocator;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.Venue;

public class DatumAndVenue {
    @Embedded
    public Datum datum;
    @Relation(
            parentColumn = "id",
            entityColumn = "vId"
    )
    public Venue venue;
    public DatumAndVenue(Datum datum, Venue venue){
        this. datum = datum;
        this.venue = venue;
    }
}
