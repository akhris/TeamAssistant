package ui.screens.master_detail

import domain.User
import kotlinx.coroutines.flow.Flow

interface IDetailsComponent<T> {
    val currentUser: User

    val item: Flow<T>
    fun updateItem(item: T)

    fun removeItem(item: T)
}