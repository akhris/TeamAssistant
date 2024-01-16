package ui.screens.master_detail.settings

import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import domain.settings.Setting
import kotlinx.coroutines.delay
import ui.UiSettings
import ui.fields.EditableTextField
import ui.screens.master_detail.IDetailsComponent

@Composable
fun SettingsDetailsUi(component: IDetailsComponent<SettingsSection>) {
    val settingsDetailsComponent = remember(component) { component as? SettingsDetailsComponent } ?: return
    val settingsType by remember(settingsDetailsComponent) { settingsDetailsComponent.item }.collectAsState(null)
    val settings = remember(settingsType) { settingsType?.settings ?: listOf() }

    RenderSettingsList(settings = settings, onSettingChanged = {
        settingsDetailsComponent.updateSetting(it)
    })
}

@Composable
private fun RenderSettingsList(settings: List<Setting>, onSettingChanged: (Setting) -> Unit) {
    settings.forEach { setting ->
        when (setting) {
            is Setting.BooleanSetting -> RenderBooleanSetting(setting, onSettingChanged)
            is Setting.StringSetting -> RenderStringSetting(setting, onSettingChanged)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderBooleanSetting(setting: Setting.BooleanSetting, onSettingChanged: (Setting.BooleanSetting) -> Unit) {

    ListItem(text = {
        Text(setting.name)
    }, secondaryText = {
        Text(setting.description)
    }, trailing = {
        Checkbox(checked = setting.value, onCheckedChange = { onSettingChanged(setting.copy(value = !setting.value)) })
    })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderStringSetting(setting: Setting.StringSetting, onSettingChanged: (Setting.StringSetting) -> Unit) {
    var tempString by remember(setting) { mutableStateOf(setting.value) }
    ListItem(text = {
        EditableTextField(
            value = tempString,
            onValueChange = { tempString = it },
            label = setting.name
        )
    }, secondaryText = {
        Text(setting.description)
    })

    LaunchedEffect(tempString) {
        if (tempString == setting.value) {
            return@LaunchedEffect
        }
        delay(UiSettings.Debounce.debounceTime)
        onSettingChanged(setting.copy(value = tempString))
    }
}