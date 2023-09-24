package me.aleksandarzekovic.labkmaspacex.shared.shipdetails

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.aleksandarzekovic.labkmaspacex.fragment.ShipsDetail
import me.aleksandarzekovic.labkmaspacex.shared.core.Store
import me.aleksandarzekovic.labkmaspacex.shared.data.repository.ShipsRepository

class ShipDetailsStore(
    private val shipsRepository: ShipsRepository
) : Store<ShipDetailsState, ShipDetailsAction, ShipDetailsSideEffect>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(ShipDetailsState(false, null))
    private val sideEffect = MutableSharedFlow<ShipDetailsSideEffect>()

    override fun observeState(): StateFlow<ShipDetailsState> = state

    override fun observeSideEffect(): Flow<ShipDetailsSideEffect> = sideEffect

    override fun dispatch(action: ShipDetailsAction) {
        Napier.d(tag = "ShipsStore", message = "Action: $action")
        val newState = when (action) {
            is ShipDetailsAction.Init -> handleInit(action.id)
            is ShipDetailsAction.LoadData -> handleLoadData(action.ship)
            is ShipDetailsAction.Error -> handleError(action.error)
        }

        if (newState != state.value) {
            Napier.d(tag = "ShipsStore", message = "NewState: $newState")
            state.value = newState
        }
    }


    private fun handleInit(id: String): ShipDetailsState =
        state.value.takeIf { !it.progress }
            ?.apply { launch { loadShipById(id) } }
            ?.copy(progress = true) ?: run {
            launch { sideEffect.emit(ShipDetailsSideEffect.Error(Exception("In progress"))) }
            state.value
        }


    private fun handleLoadData(ship: ShipsDetail?): ShipDetailsState =
        state.value.takeIf { it.progress }
            ?.copy(progress = false, ship = ship) ?: run {
            launch { sideEffect.emit(ShipDetailsSideEffect.Error(Exception("Unexpected action"))) }
            state.value
        }


    private fun handleError(error: Exception): ShipDetailsState =
        state.value.takeIf { it.progress }
            ?.copy(progress = false).also {
                launch { sideEffect.emit(ShipDetailsSideEffect.Error(error)) }
            } ?: run {
            launch { sideEffect.emit(ShipDetailsSideEffect.Error(Exception("Unexpected action"))) }
            state.value
        }


    private suspend fun loadShipById(id: String) {
        try {
            val ship = shipsRepository.getShip(id)
            dispatch(ShipDetailsAction.LoadData(ship))
        } catch (e: Exception) {
            dispatch(ShipDetailsAction.Error(e))
        }
    }
}
