package me.aleksandarzekovic.labkmaspacex.shared.shipdetails

import me.aleksandarzekovic.labkmaspacex.shared.core.Effect

sealed class ShipDetailsSideEffect : Effect {
    data class Error(val error: Exception) : ShipDetailsSideEffect()
}