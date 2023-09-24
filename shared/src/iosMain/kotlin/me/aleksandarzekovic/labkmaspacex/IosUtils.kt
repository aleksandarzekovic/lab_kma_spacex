package me.aleksandarzekovic.labkmaspacex

import me.aleksandarzekovic.labkmaspacex.shared.ships.ShipsStore

fun ShipsStore.watchState() = observeState().wrap()
fun ShipsStore.watchSideEffect() = observeSideEffect().wrap()