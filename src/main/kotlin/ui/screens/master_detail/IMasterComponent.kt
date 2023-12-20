package ui.screens.master_detail

import domain.EntitiesList
import domain.FilterSpec
import domain.User
import kotlinx.coroutines.flow.Flow

interface IMasterComponent<T> {
    val items: Flow<EntitiesList<T>>

    val filterSpecs: Flow<List<FilterSpec>>?
    fun onItemClicked(item: T)

    fun onAddNewItem(item: T)
}