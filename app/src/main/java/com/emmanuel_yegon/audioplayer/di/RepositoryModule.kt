package com.emmanuel_yegon.audioplayer.di

import com.emmanuel_yegon.audioplayer.data.repository.AudioPlayerRepositoryImpl
import com.emmanuel_yegon.audioplayer.domain.repository.AudioPlayerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAudioPlayerRepository(
        repositoryImpl: AudioPlayerRepositoryImpl
    ): AudioPlayerRepository
}