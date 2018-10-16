package com.roombookingapp.di

import android.content.Context
import com.roombookingapp.data.RoomsServices
import com.roombookingapp.data.repository.RoomsRepository
import com.roombookingapp.data.repository.RoomsRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideNewsRepo(roomsServices: RoomsServices,
                        context: Context): RoomsRepository = RoomsRepositoryImpl(roomsServices,  context)
}