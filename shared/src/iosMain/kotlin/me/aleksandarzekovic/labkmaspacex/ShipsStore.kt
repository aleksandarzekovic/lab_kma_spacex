package me.aleksandarzekovic.labkmaspacex

import me.aleksandarzekovic.labkmaspacex.shared.data.repository.ShipsRepositoryImpl
import me.aleksandarzekovic.labkmaspacex.shared.ships.ShipsStore


fun ShipsStore.Companion.create() = ShipsStore(
    ShipsRepositoryImpl(
        IosApolloClient()
    )
)