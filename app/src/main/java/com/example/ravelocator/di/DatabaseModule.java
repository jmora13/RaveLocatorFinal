package com.example.ravelocator.di;

import android.content.Context;

import com.example.ravelocator.data.DatumDao;
import com.example.ravelocator.data.DatumDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public final DatumDatabase provideDB(@ApplicationContext Context context){
        return DatumDatabase.getDatabase(context);
    }

    @Provides
    @Singleton
    public final DatumDao provideDatumDao(DatumDatabase datumDatabase){
        return datumDatabase.datumDao();
    }

}
