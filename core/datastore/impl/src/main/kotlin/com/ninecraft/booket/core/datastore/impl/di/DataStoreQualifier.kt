package com.ninecraft.booket.core.datastore.impl.di

import dev.zacsweers.metro.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BookRecentSearchDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LibraryRecentSearchDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OnboardingDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NotificationDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoginMethodDataStore
