package me.aleksandarzekovic.labkmaspacex

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache

internal fun IosApolloClient() = ApolloClient.Builder()
    .serverUrl("https://spacex-production.up.railway.app/")
    .normalizedCache(MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024))
    .build()