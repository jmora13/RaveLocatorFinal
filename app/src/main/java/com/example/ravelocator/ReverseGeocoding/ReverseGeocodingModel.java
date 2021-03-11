package com.example.ravelocator.ReverseGeocoding;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "licence",
        "osm_id",
        "address",
        "osm_type",
        "boundingbox",
        "place_id",
        "lat",
        "lon",
        "display_name"
})

public class ReverseGeocodingModel {

        @JsonProperty("licence")
        private String licence;
        @JsonProperty("osm_id")
        private Integer osmId;
        @JsonProperty("address")
        private Address address;
        @JsonProperty("osm_type")
        private String osmType;
        @JsonProperty("boundingbox")
        private List<String> boundingbox = null;
        @JsonProperty("place_id")
        private Integer placeId;
        @JsonProperty("lat")
        private String lat;
        @JsonProperty("lon")
        private String lon;
        @JsonProperty("display_name")
        private String displayName;


        @JsonProperty("licence")
        public String getLicence() {
            return licence;
        }

        @JsonProperty("licence")
        public void setLicence(String licence) {
            this.licence = licence;
        }

        @JsonProperty("osm_id")
        public Integer getOsmId() {
            return osmId;
        }

        @JsonProperty("osm_id")
        public void setOsmId(Integer osmId) {
            this.osmId = osmId;
        }

        @JsonProperty("address")
        public Address getAddress() {
            return address;
        }

        @JsonProperty("address")
        public void setAddress(Address address) {
            this.address = address;
        }

        @JsonProperty("osm_type")
        public String getOsmType() {
            return osmType;
        }

        @JsonProperty("osm_type")
        public void setOsmType(String osmType) {
            this.osmType = osmType;
        }

        @JsonProperty("boundingbox")
        public List<String> getBoundingbox() {
            return boundingbox;
        }

        @JsonProperty("boundingbox")
        public void setBoundingbox(List<String> boundingbox) {
            this.boundingbox = boundingbox;
        }

        @JsonProperty("place_id")
        public Integer getPlaceId() {
            return placeId;
        }

        @JsonProperty("place_id")
        public void setPlaceId(Integer placeId) {
            this.placeId = placeId;
        }

        @JsonProperty("lat")
        public String getLat() {
            return lat;
        }

        @JsonProperty("lat")
        public void setLat(String lat) {
            this.lat = lat;
        }

        @JsonProperty("lon")
        public String getLon() {
            return lon;
        }

        @JsonProperty("lon")
        public void setLon(String lon) {
            this.lon = lon;
        }

        @JsonProperty("display_name")
        public String getDisplayName() {
            return displayName;
        }

        @JsonProperty("display_name")
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

    }
