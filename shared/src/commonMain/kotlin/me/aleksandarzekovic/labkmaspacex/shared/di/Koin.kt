package me.aleksandarzekovic.labkmaspacex.shared.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import me.aleksandarzekovic.labkmaspacex.shared.data.repository.ShipsRepository
import me.aleksandarzekovic.labkmaspacex.shared.data.repository.ShipsRepositoryImpl
import me.aleksandarzekovic.labkmaspacex.shared.shipdetails.ShipDetailsStore
import me.aleksandarzekovic.labkmaspacex.shared.ships.ShipsStore
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(commonModule())
    }

fun commonModule() = module {
    fun provideApolloClient() : ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://spacex-production.up.railway.app/")
            .normalizedCache(MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024))
            .build()
    }

    single { provideApolloClient() }

    single<ShipsRepository> { ShipsRepositoryImpl(apolloClient = get()) }

    single { ShipsStore(get()) }
    single { ShipDetailsStore(get()) }
}
