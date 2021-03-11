package com.example.ravelocator;

import com.example.ravelocator.ReverseGeocoding.ReverseGeocodingModel;
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
            @Query("latitude")double lat, @Query("longitude")double lon,
            @Query("state")String state,
            @Query("includeElectronicGenreInd") boolean includeElectronicGenreInd,
            @Query("livestreamInd")boolean livestreamInd,
            @Query("includeOtherGenreInd")boolean includeOtherGenreInd,
            @Query("client")String client);


    @GET("/api/events")
    public Call<RaveLocatorModel> getRaveLocations(
            @Query("eventName")String eventName, @Query("client")String client);

    @GET("/api/events")
    public Call<RaveLocatorModel> getRaveLocations(
            @Query("client")String client);

    @Headers("x-rapidapi-key: d2c1842dd0msh0258f661651e3a0p1079aajsnc918df2d5033")
    @GET("/v1/reverse")
    public Call<ReverseGeocodingModel> reverseGeocoding(
            @Query("lat")double lat, @Query("lon")double lon,
            @Query("format") String format,
            @Query("zoom")int zoom);

}
