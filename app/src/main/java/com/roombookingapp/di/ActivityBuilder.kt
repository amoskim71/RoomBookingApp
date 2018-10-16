package com.roombookingapp.di

import com.roombookingapp.ui.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
internal abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun contributeHomeInjector(): HomeActivity


}