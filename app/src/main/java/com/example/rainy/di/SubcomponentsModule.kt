package com.example.rainy.di

import dagger.Module
import di.DataComponent

@Module(subcomponents = [DataComponent::class])
internal interface SubcomponentsModule