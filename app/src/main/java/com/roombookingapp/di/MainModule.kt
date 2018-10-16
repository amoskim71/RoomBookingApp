package com.roombookingapp.di

import com.roombookingapp.ui.roombook.BookRoomFragment
import com.roombookingapp.ui.rooms.RoomsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class MainModule {
    @ContributesAndroidInjector
    internal abstract fun contributeHomeFragmentInjector(): RoomsListFragment

    @ContributesAndroidInjector
    internal abstract fun contributeSelectionFragmentInjector(): BookRoomFragment


}