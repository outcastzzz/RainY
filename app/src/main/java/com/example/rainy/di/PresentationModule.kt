package com.example.rainy.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dagger.Module
import dagger.Provides

@Module
interface PresentationModule {

    companion object {

        @Provides
        @ApplicationScope
        fun provideStoreFactory(): StoreFactory = DefaultStoreFactory()

    }
}