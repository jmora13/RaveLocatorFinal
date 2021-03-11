package com.example.ravelocator.ReverseGeocoding;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "country",
        "state",
        "country_code"
})

public class Address {

        @JsonProperty("country")
        private String country;
        @JsonProperty("state")
        private String state;
        @JsonProperty("country_code")
        private String countryCode;

        @JsonProperty("country")
        public String getCountry() {
            return country;
        }

        @JsonProperty("country")
        public void setCountry(String country) {
            this.country = country;
        }

        @JsonProperty("state")
        public String getState() {
            return state;
        }

        @JsonProperty("state")
        public void setState(String state) {
            this.state = state;
        }

        @JsonProperty("country_code")
        public String getCountryCode() {
            return countryCode;
        }

        @JsonProperty("country_code")
        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }


}
