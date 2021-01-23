package com.example.ravelocator;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.ravelocator.util.ArtistList;
import com.example.ravelocator.util.Datum;

import java.util.List;

public class DatumWithArtistList {
    @Embedded
    public Datum datum;

    @Relation(
            parentColumn = "id",
            entityColumn = "aId"
    )
    public List<ArtistList> artistList;

    public DatumWithArtistList(Datum datum, List<ArtistList> artistList){
        this.datum = datum;
        this.artistList = artistList;
    }
}
