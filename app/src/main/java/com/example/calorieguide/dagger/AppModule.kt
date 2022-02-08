package com.example.calorieguide.dagger

import android.content.Context
import com.example.calorieguide.BuildConfig
import com.example.core.di.CoreProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRepository(@ApplicationContext context: Context) =
        CoreProvider.getRepository(context, BuildConfig.CALORIE_GUIDE_API_HOST)
}