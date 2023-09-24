package me.aleksandarzekovic.labkmaspacex.shared.ships

import me.aleksandarzekovic.labkmaspacex.fragment.ShipsDetail
import me.aleksandarzekovic.labkmaspacex.shared.core.Action

sealed class ShipsAction : Action {
    data object Init : ShipsAction()
    data class LoadData(val ships: List<ShipsDetail>) : ShipsAction()
    data class Error(val error: Exception) : ShipsAction()
}