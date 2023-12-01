package ui.screens.master_detail.details

import domain.IEntity
import domain.Task
import kotlinx.coroutines.flow.Flow

interface IDetailsComponent<T> {
    val item: Flow<T>
}