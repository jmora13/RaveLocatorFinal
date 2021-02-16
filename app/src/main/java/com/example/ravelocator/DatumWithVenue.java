package com.example.ravelocator;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.Venue;

public class DatumWithVenue {
    @Embedded
    public Datum datum;
    @Relation(
            parentColumn = "id",
            entityColumn = "venueName",
            associateBy = @Junction(DatumVenueCrossRef.class)
    )
    public Venue venue;
    public DatumWithVenue(Datum datum, Venue venue){
        this.datum = datum;
        this.venue = venue;
    }
}
