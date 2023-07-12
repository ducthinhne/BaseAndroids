package com.example.baseandroid.resource.injection

import com.example.baseandroid.data.SearchRepository
import com.example.baseandroid.data.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface Modules {

    @Binds
    fun provideSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}