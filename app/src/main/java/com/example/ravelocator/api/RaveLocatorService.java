package com.example.ravelocator.api;


import com.example.ravelocator.model.Datum;
import com.example.ravelocator.model.LocationDataModel;
import com.example.ravelocator.model.RaveLocatorModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface RaveLocatorService {


    //A ^ B ^ C == TRUE
    @GET("/api/events")
    Call<RaveLocatorModel> getRaveLocations(
            @Query("festivalInd") boolean festivalInd,
            @Query("createdStartDate") String createdStartDate,
            @Query("createdEndDate") String createdEndDate,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("locationIds") int locationids,
            @Query("includeElectronicGenreInd") boolean includeElectronicGenreInd,
            @Query("livestreamInd") boolean livestreamInd,
            @Query("includeOtherGenreInd") boolean includeOtherGenreInd,
            @Query("client") String client);


    @GET("/api/locations")
    Call<LocationDataModel> getLocationId(
            @Query("state") String state, @Query("city") String city,
            @Query("client") String client);

    @GET("/api/locations")
    Call<LocationDataModel> getLocationId(
            @Query("state") String state,
            @Query("client") String client);

    @GET("/api/events")
    Call<RaveLocatorModel> getRaveLocations(
            @Query("client") String client);


}
