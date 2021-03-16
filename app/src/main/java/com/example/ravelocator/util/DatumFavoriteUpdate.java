package com.example.ravelocator.util;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Update;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class DatumFavoriteUpdate {
    @PrimaryKey
    @JsonProperty("id")
    private Integer id;
    @ColumnInfo(name = "isFavorite")
    public boolean isFavorite;

    public DatumFavoriteUpdate(int id, boolean isFavorite){
        this.id = id;
        this.isFavorite = isFavorite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
