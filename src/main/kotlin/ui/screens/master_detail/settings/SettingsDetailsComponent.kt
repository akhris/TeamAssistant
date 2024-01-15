package ui.screens.master_detail.settings

import androidx.compose.runtime.MutableState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ui.screens.BaseComponent
import ui.screens.master_detail.IDetailsComponent
import utils.log

class SettingsDetailsComponent(
    settingsType: SettingsType,
    di: DI,
    componentContext: ComponentContext,
) : IDetailsComponent<SettingsType>, BaseComponent(componentContext) {

    private val repo: ISettingsRepository by di.instance()

    private val _item: MutableStateFlow<SettingsType> = MutableStateFlow(settingsType)

    override val item: Flow<SettingsType> = _item


    fun updateSetting(setting: Setting){

    }

    override fun removeItem(item: SettingsType) {

    }

    override fun updateItem(item: SettingsType) {

    }

    init {
        //get settings from settings repository for given [settingsNavItem]
        scope.launch {
            when (settingsType.id) {
                SettingsType.DBSettingsID -> {
                    repo.query(listOf(Specification.QueryAll)).collect { s ->
                        _item.value = settingsType.copy(settings = s)
                    }
                }

                SettingsType.APPSettingsID -> {

                }
            }
        }
    }
}