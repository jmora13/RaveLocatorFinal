
package com.example.ravelocator.util;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "link",
        "name",
        "ages",
        "festivalInd",
        "livestreamInd",
        "electronicGenreInd",
        "otherGenreInd",
        "date",
        "startTime",
        "endTime",
        "createdDate",
        "venue",
        "artistList"
})
@Entity(tableName = "datum_fts")
@Fts4(contentEntity = Datum.class)
public class DatumFTS {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("link")
    private String link;
    @JsonProperty("name")
    private String name;
    @JsonProperty("ages")
    private String ages;
    @JsonProperty("festivalInd")
    private Boolean festivalInd;
    @JsonProperty("livestreamInd")
    private Boolean livestreamInd;
    @JsonProperty("electronicGenreInd")
    private Boolean electronicGenreInd;
    @JsonProperty("otherGenreInd")
    private Boolean otherGenreInd;
    @JsonProperty("date")
    private String date;
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("endTime")
    private String endTime;
    @JsonProperty("createdDate")
    private String createdDate;
    @JsonProperty("venue")
    private Venue venue;
    @JsonProperty("artistList")
    private List<ArtistList> artistList = null;
    @ColumnInfo(defaultValue = "false")
    private Boolean isFavorite = false;

//    public Datum(Integer id, String link, String name,String ages,Boolean festivalInd,
//                 Boolean livestreamInd,Boolean electronicGenreInd,Boolean otherGenreInd,
//                 String date, String startTime, String endTime, String createdDate, Venue venue, List<ArtistList> artistList){
//        this.id = id; this.link = link; this.name = name; this.ages = ages; this.festivalInd = festivalInd;
//        this.livestreamInd = livestreamInd; this.electronicGenreInd = electronicGenreInd; this.otherGenreInd = otherGenreInd;
//        this.date = date; this.startTime = startTime; this.endTime = endTime; this.createdDate = createdDate; this.venue = venue; this.artistList = artistList;
//    }

    //    public Datum(Integer id, String name){
//        this.id = id;
//        this.name = name;
//    }
//    public Datum(){
//    }

    public static Datum populateData() {
        return new Datum(123, "456","test", "all", true, true,
                true, true,"yesterday","now", "later", "yesterday", null, null);
    }


    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("ages")
    public String getAges() {
        return ages;
    }

    @JsonProperty("ages")
    public void setAges(String ages) {
        this.ages = ages;
    }

    @JsonProperty("festivalInd")
    public Boolean getFestivalInd() {
        return festivalInd;
    }

    @JsonProperty("festivalInd")
    public void setFestivalInd(Boolean festivalInd) {
        this.festivalInd = festivalInd;
    }

    @JsonProperty("livestreamInd")
    public Boolean getLivestreamInd() {
        return livestreamInd;
    }

    @JsonProperty("livestreamInd")
    public void setLivestreamInd(Boolean livestreamInd) {
        this.livestreamInd = livestreamInd;
    }

    @JsonProperty("electronicGenreInd")
    public Boolean getElectronicGenreInd() {
        return electronicGenreInd;
    }

    @JsonProperty("electronicGenreInd")
    public void setElectronicGenreInd(Boolean electronicGenreInd) {
        this.electronicGenreInd = electronicGenreInd;
    }

    @JsonProperty("otherGenreInd")
    public Boolean getOtherGenreInd() {
        return otherGenreInd;
    }

    @JsonProperty("otherGenreInd")
    public void setOtherGenreInd(Boolean otherGenreInd) {
        this.otherGenreInd = otherGenreInd;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("startTime")
    public String getStartTime() {
        return startTime;
    }

    @JsonProperty("startTime")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @JsonProperty("endTime")
    public String getEndTime() {
        return endTime;
    }

    @JsonProperty("endTime")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @JsonProperty("createdDate")
    public String getCreatedDate() {
        return createdDate;
    }

    @JsonProperty("createdDate")
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @JsonProperty("venue")
    public Venue getVenue() {
        return venue;
    }

    @JsonProperty("venue")
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    @JsonProperty("artistList")
    public List<ArtistList> getArtistList() {
        return artistList;
    }

    @JsonProperty("artistList")
    public void setArtistList(List<ArtistList> artistList) {
        this.artistList = artistList;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }
}