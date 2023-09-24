package me.aleksandarzekovic.labkmaspacex.shared.shipdetails

import me.aleksandarzekovic.labkmaspacex.fragment.ShipsDetail
import me.aleksandarzekovic.labkmaspacex.shared.core.Action

sealed class ShipDetailsAction : Action {
    data class Init(val id: String) : ShipDetailsAction()
    data class LoadData(val ship: ShipsDetail?) : ShipDetailsAction()
    data class Error(val error: Exception) : ShipDetailsAction()
}