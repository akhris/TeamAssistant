package ui.screens.master_detail.master

import domain.EntitiesList
import domain.FilterSpec
import domain.IEntity
import domain.Task
import kotlinx.coroutines.flow.Flow

interface IMasterComponent<T> {
    val items: Flow<EntitiesList<T>>

    val filterSpecs: Flow<List<FilterSpec>>?
    fun onItemClicked(item: T)
}