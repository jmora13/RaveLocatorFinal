package com.example.ravelocator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data",
        "success"
})

public class RaveLocatorModel {

    @JsonProperty("data")
    private List<Datum> data = null;
    @JsonProperty("success")
    private Boolean success;



//    public RaveLocatorModel(@NonNull boolean success, List<GetLocationId> data){
//        this.success = success;
//        this.data = data;
//    }


    @JsonProperty("data")
    public List<Datum> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<Datum> data) {
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

}