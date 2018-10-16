package com.roombookingapp.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.roombookingapp.data.RoomsServices
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory



@Module
class NetworkModule {

    @Provides
    fun provideNewsApi(retrofit: Retrofit) = retrofit.create(RoomsServices::class.java)

    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient) =
            Retrofit.Builder()
                    .baseUrl("https://challenges.1aim.com/roombooking_app/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()


    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
                .addInterceptor(logging)
                .addNetworkInterceptor(StethoInterceptor())
                .build()
    }
}