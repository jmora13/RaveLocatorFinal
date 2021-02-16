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
        "venueName",
        "location",
        "address",
        "state",
        "latitude",
        "longitude"
})
@Entity
public class Venue {
    @PrimaryKey
    @JsonProperty("id")
    private Integer venueId;
    @JsonProperty("name")
    private String venueName;
    @JsonProperty("location")
    private String location;
    @JsonProperty("address")
    private String address;
    @JsonProperty("state")
    private String state;
    @JsonProperty("latitude")
    private Double latitude;
    @JsonProperty("longitude")
    private Double longitude;


    @ForeignKey
            (entity = Datum.class,
                    parentColumns = "id",
                    childColumns = "vId",
                    onDelete = CASCADE
            )

//    public Venue(Integer venueId, String venueName, String location, String address, String state, Double latitude, Double longitude){
//        this.venueId = venueId; this.venueName = venueName; this.location = location; this.address = address; this.state = state; this. latitude = latitude; this.longitude = longitude;
//    }
    public  Venue(){}
    public String getVenueName() {
        return venueName;
    }

    @JsonProperty("name")
    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("latitude")
    public Double getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public Double getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("id")
    public Integer getVenueId() {
        return venueId;
    }
    @JsonProperty("id")
    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }

    public String getNameFromId(int venueId){
        return venueName;
    }
}