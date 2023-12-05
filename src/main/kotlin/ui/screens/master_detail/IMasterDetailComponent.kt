package ui.screens.master_detail

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import domain.IEntity

interface IMasterDetailComponent<T> {
    val masterStack: Value<ChildStack<*, Master<T>>>

    val detailsStack: Value<ChildStack<*, Details<T>>>

    //    fun onFABClicked()
    sealed class Master<T> {
        class ItemsList<T : IEntity>(val component: IMasterComponent<T>) : Master<T>()
    }

    sealed class Details<T> {
        class ItemDetails<T : IEntity>(val component: IDetailsComponent<T>) : Details<T>()

        class None<T> : Details<T>()
    }
}