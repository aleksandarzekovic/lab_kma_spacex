package me.aleksandarzekovic.labkmaspacex.android.navigation

sealed class Destination(val route: String) {
    data object Ships : Destination(SHIPS)
    data object ShipDetails : Destination(SHIP_DETAILS)

    companion object {
        const val SHIPS = "ships"
        const val SHIP_DETAILS = "ship_details"
    }
}