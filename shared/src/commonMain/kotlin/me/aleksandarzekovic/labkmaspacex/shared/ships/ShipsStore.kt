package me.aleksandarzekovic.labkmaspacex.shared.ships

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

class ShipsStore(
    private val shipsRepository: ShipsRepository
) : Store<ShipsState, ShipsAction, ShipsSideEffect>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(ShipsState(false, emptyList()))
    private val sideEffect = MutableSharedFlow<ShipsSideEffect>()

    override fun observeState(): StateFlow<ShipsState> = state

    override fun observeSideEffect(): Flow<ShipsSideEffect> = sideEffect

    init {
        dispatch(ShipsAction.Init)
    }

    override fun dispatch(action: ShipsAction) {
        Napier.d(tag = "ShipsStore", message = "Action: $action")
        val newState = when (action) {
            ShipsAction.Init -> handleInit()
            is ShipsAction.LoadData -> handleLoadData(action.ships)
            is ShipsAction.Error -> handleError(action.error)
        }

        if (newState != state.value) {
            Napier.d(tag = "ShipsStore", message = "NewState: $newState")
            state.value = newState
        }
    }


    private fun handleInit(): ShipsState =
        state.value.takeIf { !it.progress }
            ?.apply { launch { loadShips() } }
            ?.copy(progress = true) ?: run {
            launch { sideEffect.emit(ShipsSideEffect.Error(Exception("In progress"))) }
            state.value
        }


    private fun handleLoadData(ships: List<ShipsDetail>): ShipsState =
        state.value.takeIf { it.progress }
            ?.copy(progress = false, ships = ships) ?: run {
            launch { sideEffect.emit(ShipsSideEffect.Error(Exception("Unexpected action"))) }
            state.value
        }


    private fun handleError(error: Exception): ShipsState =
        state.value.takeIf { it.progress }
            ?.copy(progress = false).also {
                launch { sideEffect.emit(ShipsSideEffect.Error(error)) }
            } ?: run {
            launch { sideEffect.emit(ShipsSideEffect.Error(Exception("Unexpected action"))) }
            state.value
        }


    @Throws(Exception::class)
    private suspend fun loadShips() {
        try {
            val ships = shipsRepository.getShips()
            dispatch(ShipsAction.LoadData(ships))
        } catch (e: Exception) {
            dispatch(ShipsAction.Error(e))
        }
    }

    companion object
}
