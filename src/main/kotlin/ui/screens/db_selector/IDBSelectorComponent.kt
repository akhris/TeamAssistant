package ui.screens.db_selector

import com.arkivanov.decompose.value.Value

interface IDBSelectorComponent {

    val lastOpenedDBPaths: Value<List<String>>

    val currentDBPath: Value<String>

}