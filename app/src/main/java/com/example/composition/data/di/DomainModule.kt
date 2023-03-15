package com.example.composition.data.di

import com.example.composition.data.repository_impl.GameRepositoryImpl
import com.example.composition.domain.repository.GameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGameRepository(): GameRepository {
        return GameRepositoryImpl
    }
}