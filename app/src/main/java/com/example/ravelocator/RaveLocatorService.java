package com.example.ravelocator;


import com.example.ravelocator.GetLocationId.GetLocationId;
import com.example.ravelocator.GetLocationId.LocationDataModel;
import com.example.ravelocator.util.RaveLocatorModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
public interface RaveLocatorService {


    //A ^ B ^ C == TRUE
    @GET("/api/events")
    public Call<RaveLocatorModel> getRaveLocations(
            @Query("createdStartDate") String createdStartDate,
            @Query("createdEndDate") String createdEndDate,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("locationIds")int locationids,
            @Query("includeElectronicGenreInd") boolean includeElectronicGenreInd,
            @Query("livestreamInd")boolean livestreamInd,
            @Query("includeOtherGenreInd")boolean includeOtherGenreInd,
            @Query("client")String client);


    @GET("/api/locations")
    public Call<LocationDataModel> getLocationId(
            @Query("state")String state, @Query("city")String city,
            @Query("client")String client);

    @GET("/api/locations")
    public Call<LocationDataModel> getLocationId(
            @Query("state")String state,
            @Query("client")String client);

    @GET("/api/events")
    public Call<RaveLocatorModel> getRaveLocations(
            @Query("client")String client);


}
