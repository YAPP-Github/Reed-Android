package com.ninecraft.booket.feature.settings.osslicenses

import kotlinx.serialization.Serializable

@Serializable
data class OssLicenseInfo(
    val name: String,
    val license: String,
    val url: String,
)
