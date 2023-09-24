package me.aleksandarzekovic.labkmaspacex.shared.ships

import me.aleksandarzekovic.labkmaspacex.shared.core.Effect

sealed class ShipsSideEffect : Effect {
    data class Error(val error: Exception) : ShipsSideEffect()
}