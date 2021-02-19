package com.example.ravelocator;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.Venue;

import java.util.List;

public class VenueWithDatum {
    @Embedded
    public Venue venue;
    @Relation(
            parentColumn = "venueName",
            entityColumn = "id",
            associateBy = @Junction(DatumVenueCrossRef.class)
    )
    public List<Datum> datum;
    public VenueWithDatum(List<Datum> datum, Venue venue){
        this.datum = datum;
        this.venue = venue;
    }
}
