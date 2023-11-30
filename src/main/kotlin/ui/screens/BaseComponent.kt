package ui.screens

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.subscribe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

abstract class BaseComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    protected val scope =
        CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        componentContext
            .lifecycle
            .subscribe(onDestroy = {
                scope.coroutineContext.cancelChildren()
            })
    }

}