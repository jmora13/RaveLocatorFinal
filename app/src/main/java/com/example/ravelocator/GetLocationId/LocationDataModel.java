package com.example.ravelocator.GetLocationId;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LocationDataModel {
    @JsonProperty("data")
    private List<GetLocationId> data = null;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    public List<GetLocationId> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<GetLocationId> data) {
        this.data = data;
    }

    @JsonProperty("success")
    public Boolean getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
