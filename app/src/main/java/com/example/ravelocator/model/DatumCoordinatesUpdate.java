package com.example.ravelocator.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;
@Entity
public class DatumCoordinatesUpdate {
    @PrimaryKey
    @JsonProperty("id")
    private Integer id;
    @ColumnInfo(name = "coordinates")
    public String coordinates;

    public DatumCoordinatesUpdate(int id, String coordinates){
        this.id = id;
        this.coordinates = coordinates;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
