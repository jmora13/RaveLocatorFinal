package com.example.ravelocator.di;

import com.example.ravelocator.api.RaveLocatorService;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@InstallIn(ActivityComponent.class)
@Module
public class NetworkModule {

    @Provides
    @Singleton
    public final RaveLocatorService provideRaveLocatorService(){
        return new Retrofit.Builder()
                .baseUrl("https://edmtrain.com")
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(RaveLocatorService.class);

    }
}
