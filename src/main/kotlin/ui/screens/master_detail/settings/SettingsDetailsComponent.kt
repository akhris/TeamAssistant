package ui.screens.master_detail.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ui.screens.BaseComponent
import ui.screens.master_detail.IDetailsComponent

class SettingsDetailsComponent(
    settingsNavItem: SettingsNavItem,
    di: DI,
    componentContext: ComponentContext,
) : IDetailsComponent<SettingsNavItem>, BaseComponent(componentContext) {

    private val repo: IRepositoryObservable<Setting> by di.instance()

    override val item: Flow<SettingsNavItem> =
        flowOf(settingsNavItem)

    val _settings: MutableValue<EntitiesList<Setting>> = MutableValue(EntitiesList.empty())

    val settings: Value<EntitiesList<Setting>> = _settings


    override fun removeItem(item: SettingsNavItem) {

    }

    override fun updateItem(item: SettingsNavItem) {

    }

    init {
        //get settings from settings repository for given [settingsNavItem]
        scope.launch {
            when (settingsNavItem) {
                SettingsNavItem.DBSettings -> {
                    repo.query(listOf(Specification.QueryAll)).collect { s ->
                        _settings.value = s
                    }
                }
            }
        }
    }
}