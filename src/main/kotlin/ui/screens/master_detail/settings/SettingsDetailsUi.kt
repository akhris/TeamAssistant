package ui.screens.master_detail.settings

import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import domain.settings.ISettingDescriptor
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
    val descriptor = remember(settingsDetailsComponent) { settingsDetailsComponent.settingDescriptor }
    RenderSettingsList(
        settings = settings,
        settingDescriptor = descriptor,
        onSettingChanged = {
            settingsDetailsComponent.updateSetting(it)
        }
    )
}

@Composable
private fun RenderSettingsList(
    settings: List<Setting>,
    settingDescriptor: ISettingDescriptor,
    onSettingChanged: (Setting) -> Unit,
) {
    settings.forEach { setting ->
        when (setting) {
            is Setting.BooleanSetting -> RenderBooleanSetting(setting, settingDescriptor, onSettingChanged)
            is Setting.StringSetting -> RenderStringSetting(setting, settingDescriptor, onSettingChanged)
            is Setting.PathSetting -> RenderPathSetting(setting, settingDescriptor, onSettingChanged)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderBooleanSetting(
    setting: Setting.BooleanSetting,
    settingDescriptor: ISettingDescriptor,
    onSettingChanged: (Setting.BooleanSetting) -> Unit,
) {
    val title = remember(settingDescriptor, setting) { settingDescriptor.getTitle(setting.id) }
    val description = remember(settingDescriptor, setting) { settingDescriptor.getDescription(setting.id) }

    ListItem(text = {
        title?.let { Text(it) }
    }, secondaryText = {
        description?.let { Text(it) }
    }, trailing = {
        Checkbox(checked = setting.value, onCheckedChange = { onSettingChanged(setting.copy(value = !setting.value)) })
    })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderStringSetting(
    setting: Setting.StringSetting,
    settingDescriptor: ISettingDescriptor,
    onSettingChanged: (Setting.StringSetting) -> Unit,
) {
    var tempString by remember(setting) { mutableStateOf(setting.value) }
    val title = remember(settingDescriptor, setting) { settingDescriptor.getTitle(setting.id) }
    val description = remember(settingDescriptor, setting) { settingDescriptor.getDescription(setting.id) }
    ListItem(text = {

        EditableTextField(
            value = tempString,
            onValueChange = { tempString = it },
            label = title ?: ""
        )


    }, secondaryText = description?.let { d ->
        {
            Text(d)
        }
    })

    LaunchedEffect(tempString) {
        if (tempString == setting.value) {
            return@LaunchedEffect
        }
        delay(UiSettings.Debounce.debounceTime)
        onSettingChanged(setting.copy(value = tempString))
    }
}

@Composable
private fun RenderPathSetting(
    setting: Setting.PathSetting,
    settingDescriptor: ISettingDescriptor,
    onSettingChanged: (Setting.PathSetting) -> Unit,
) {

}