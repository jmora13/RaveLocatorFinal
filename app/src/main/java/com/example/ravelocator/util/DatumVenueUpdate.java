package com.example.ravelocator.util;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;
@Entity
public class DatumVenueUpdate {
    @PrimaryKey
    @JsonProperty("id")
    private Integer id;
    @ColumnInfo(name = "venueName")
    public String venueName;

    public DatumVenueUpdate(int id, String venueName){
        this.id = id;
        this.venueName = venueName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
