package com.ninecraft.booket.core.di

/**
 * Scope for data layer dependencies including:
 * - Network (Retrofit, OkHttp, Interceptors)
 * - DataStore and DataSources
 * - Repository implementations
 */
abstract class DataScope private constructor()