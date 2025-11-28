package com.ninecraft.booket.core.common.utils

fun shouldSyncNotification(effectiveEnabled: Boolean, lastSynced: Boolean?): Boolean =
    lastSynced == null || lastSynced != effectiveEnabled
