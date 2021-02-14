package com.example.ravelocator;

import com.example.ravelocator.util.RaveLocatorModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface RaveLocatorService {
    @GET("/api/events")
    public Call<RaveLocatorModel> getRaveLocations(
            @Query("latitude")double lat, @Query("longitude")double lon, @Query("state")String state,@Query("client")String client);

    @GET("/api/events")
    public Call<RaveLocatorModel> getRaveLocations(
            @Query("eventName")String eventName, @Query("client")String client);

    @GET("/api/events")
    public Call<RaveLocatorModel> getRaveLocations(
            @Query("client")String client);

}
