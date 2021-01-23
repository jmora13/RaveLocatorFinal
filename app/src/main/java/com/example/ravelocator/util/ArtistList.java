package com.example.ravelocator.util;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import static androidx.room.ForeignKey.CASCADE;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "artistName",
        "link",
        "b2bInd"
})
@Entity
public class ArtistList {

    @PrimaryKey
    @JsonProperty("id")
    private Integer artistListId;
    @JsonProperty("name")
    private String artistName;
    @JsonProperty("link")
    private String link;
    @JsonProperty("b2bInd")
    private Boolean b2bInd;
    public Integer aId;



//    public ArtistList(Integer artistListId, String artistName, String link, Boolean b2bInd){
//        this.artistListId = artistListId; this.artistName = artistName; this.link = link; this.b2bInd = b2bInd;
//    }
    public ArtistList(){}
    @JsonProperty("name")
    public String getArtistName() {
        return artistName;
    }

    @JsonProperty("name")
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    @JsonProperty("b2bInd")
    public Boolean getB2bInd() {
        return b2bInd;
    }

    @JsonProperty("b2bInd")
    public void setB2bInd(Boolean b2bInd) {
        this.b2bInd = b2bInd;
    }

    @JsonProperty("id")
    public Integer getArtistListId() {
        return artistListId;
    }
    @JsonProperty("id")
    public void setArtistListId(Integer artistListId) {
        this.artistListId = artistListId;
    }
}