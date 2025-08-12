package com.ninecraft.booket.core.common.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {
    fun logScreenView(screenName: String, customTags: Map<String, String> = emptyMap()) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)

            customTags.forEach { (key, value) ->
                param(key, value)
            }
        }
    }

    fun logCustomEvent(eventName: String, parameters: Map<String, String> = emptyMap()) {
        firebaseAnalytics.logEvent(eventName) {
            parameters.forEach { (key, value) ->
                param(key, value)
            }
        }
    }
}
