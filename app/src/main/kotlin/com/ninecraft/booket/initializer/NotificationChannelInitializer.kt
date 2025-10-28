package com.ninecraft.booket.initializer

import android.content.Context
import androidx.startup.Initializer
import com.ninecraft.booket.ReedFirebaseMessagingService.Companion.createNotificationChannel

class NotificationChannelInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        createNotificationChannel(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
