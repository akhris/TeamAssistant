package ui.screens.master_detail

import kotlinx.coroutines.flow.Flow

interface IDetailsComponent<T> {
    val item: Flow<T>
    fun updateItem(item: T)

    fun removeItem(item: T)
}