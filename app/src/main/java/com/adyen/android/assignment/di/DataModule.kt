package com.adyen.android.assignment.di

import com.adyen.android.assignment.data.api.PlacesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun bindPlacesService(): PlacesService {
        return PlacesService.instance
    }
}
