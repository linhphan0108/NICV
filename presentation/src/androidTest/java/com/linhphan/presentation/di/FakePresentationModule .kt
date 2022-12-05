package com.linhphan.presentation.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PresentationModule::class]
)
class FakePresentationModule {
    @Provides
    @Singleton
    fun provideApplication(): Context {
        return ApplicationProvider.getApplicationContext()
    }
}