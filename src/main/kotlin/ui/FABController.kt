package ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow

class FABController : IFABController {

    private val _clicks = MutableSharedFlow<Unit>()
    override val clicks: Flow<Unit> = _clicks

    val a = callbackFlow<Unit> {

    }
    private val _state = mutableStateOf<FABState>(FABState.HIDDEN)
    val state: State<FABState> = _state

    override fun setFABState(state: FABState) {
        _state.value = state
    }

    suspend fun onClick() {
        _clicks.emit(Unit)
    }

}

interface IFABController {

    val clicks: Flow<Unit>
    fun setFABState(state: FABState)
//    fun onFABClick(): () -> Unit
}

sealed class FABState {
    data class VISIBLE(val iconPath: String?, val text: String = "", val description: String = "") : FABState()
    object HIDDEN : FABState()
}