package com.ninecraft.booket

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.util.DebugLogger
import com.ninecraft.booket.di.AppGraph
import dev.zacsweers.metro.createGraphFactory

class BooketApplication : Application(), ImageLoaderFactory {

    val appGraph by lazy { createGraphFactory<AppGraph.Factory>().create(this) }
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(10 * 1024 * 1024)
                    .build()
            }
            .logger(DebugLogger())
            .respectCacheHeaders(false)
            .build()
    }
}
