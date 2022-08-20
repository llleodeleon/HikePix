package com.leodeleon.hikepix.di

import android.content.Context
import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.leodeleon.hikepix.BuildConfig
import com.leodeleon.hikepix.service.LocationManager
import com.leodeleon.hikepix.data.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFlickr() = Flickr(BuildConfig.flickrApiKey, BuildConfig.flickrApiSecret, REST())

    @Provides
    @Singleton
    fun provideLocationClient(@ApplicationContext context: Context) = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideLocationManager(client: FusedLocationProviderClient) = LocationManager(client)

    @Provides
    @Singleton
    fun providePhotoRepository(flickr: Flickr) = PhotoRepository(flickr)
}