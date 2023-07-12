package com.example.baseandroid.resource.injection

import android.content.Context
import androidx.room.Room
import com.example.baseandroid.BuildConfig
import com.example.baseandroid.data.local.AppDatabase
import com.example.baseandroid.data.local.search.SearchHistoryDao
import com.example.baseandroid.data.network.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object Provider {

    @Provides
    fun provideRetrofit(
        @AuthInterceptorClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "gurume-go"
        ).build()
    }

    @Provides
    fun provideHistoryDao(@ApplicationContext context: Context): SearchHistoryDao {
        return provideAppDataBase(context).userDao()
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .generateNonExecutableJson()
            .create()
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}