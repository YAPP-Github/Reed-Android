package com.ninecraft.booket.core.common.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.orhanobut.logger.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
) {

    fun logScreenView(screenName: String) {
        Logger.d("Analytics - Screen View: $screenName")
        firebaseAnalytics.logEvent(screenName) {}
    }

    fun logEvent(eventName: String) {
        Logger.d("Analytics - Event: $eventName")
        firebaseAnalytics.logEvent(eventName) {}
    }
}
