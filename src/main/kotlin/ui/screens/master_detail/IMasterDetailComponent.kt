package ui.screens.master_detail

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import domain.IEntity
import domain.User
import ui.FABState
import ui.dialogs.IDialogComponent

interface IMasterDetailComponent<T> {


    val masterStack: Value<ChildStack<*, Master<T>>>

    val detailsStack: Value<ChildStack<*, Details<T>>>

    val dialogSlot: Value<ChildSlot<*, IDialogComponent>>?

    val fabState: Value<FABState>

    fun onDialogOKClicked(text: String, user: User)
    fun onFABClicked()
    sealed class Master<T> {
        class ItemsList<T : IEntity>(val component: IMasterComponent<T>) : Master<T>()
    }

    sealed class Details<T> {
        class ItemDetails<T : IEntity>(val component: IDetailsComponent<T>) : Details<T>()

        class None<T> : Details<T>()
    }
}