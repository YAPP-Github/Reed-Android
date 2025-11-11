package com.ninecraft.booket.core.common.analytics.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
interface AnalyticsGraph {

    @Provides
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics
}
