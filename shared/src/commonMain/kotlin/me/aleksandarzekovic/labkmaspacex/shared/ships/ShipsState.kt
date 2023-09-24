package me.aleksandarzekovic.labkmaspacex.shared.ships

import me.aleksandarzekovic.labkmaspacex.shared.core.State
import me.aleksandarzekovic.labkmaspacex.fragment.ShipsDetail

data class ShipsState(
    val progress: Boolean,
    val ships: List<ShipsDetail>
) : State