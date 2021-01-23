package com.example.ravelocator;

import com.example.ravelocator.util.RaveLocatorModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface RaveLocatorService {
    @GET("/api/events")
    public Call<RaveLocatorModel> getRaveLocations(
            @Query("endDate")String endDate, @Query("client")String client);

}
