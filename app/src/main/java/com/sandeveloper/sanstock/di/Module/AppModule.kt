package com.sandeveloper.sanstock.di.Module

import android.app.Application
import androidx.room.Room
import com.sandeveloper.sanstock.data.local.StockDatabase
import com.sandeveloper.sanstock.data.remote.StockApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStockApi():StockApi{
        return Retrofit.Builder()
            .baseUrl(StockApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun providesStockDatabase(context:Application):StockDatabase{
        return Room.databaseBuilder(
            context,
            StockDatabase::class.java,
            "stockdb.db"
        ).build()
    }
}