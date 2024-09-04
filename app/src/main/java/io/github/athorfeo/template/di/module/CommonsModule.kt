package io.github.athorfeo.template.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.athorfeo.template.util.AppLogger
import io.github.athorfeo.template.util.Logger
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CommonsModule {
    @Provides
    fun providesLogger(): Logger {
        return AppLogger
    }

    @Singleton
    @Provides
    fun providesGson(): Gson {
        return Gson()
    }
}
