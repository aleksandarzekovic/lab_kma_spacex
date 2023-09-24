package me.aleksandarzekovic.labkmaspacex.shared.shipdetails

import me.aleksandarzekovic.labkmaspacex.fragment.ShipsDetail
import me.aleksandarzekovic.labkmaspacex.shared.core.State

data class ShipDetailsState(
    val progress: Boolean,
    val ship: ShipsDetail?
) : State