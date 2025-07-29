package com.ninecraft.booket.core.datastore.impl.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RecentSearchDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OnboardingDataStore
