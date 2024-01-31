package ui.screens.master_detail.settings

import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import domain.settings.ISettingDescriptor
import domain.settings.Setting
import domain.settings.SettingType
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
        when (settingDescriptor.getType(setting.id)) {
            SettingType.Boolean -> RenderBooleanSetting(setting, settingDescriptor, onSettingChanged)
            SettingType.String -> RenderStringSetting(setting, settingDescriptor, onSettingChanged)
            SettingType.Path -> RenderPathSetting(setting, settingDescriptor, onSettingChanged)
            null -> {

            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderBooleanSetting(
    setting: Setting,
    settingDescriptor: ISettingDescriptor,
    onSettingChanged: (Setting) -> Unit,
) {
    val title = remember(settingDescriptor, setting) { settingDescriptor.getTitle(setting.id) }
    val description = remember(settingDescriptor, setting) { settingDescriptor.getDescription(setting.id) }

    ListItem(text = {
        title?.let { Text(it) }
    }, secondaryText = {
        description?.let { Text(it) }
    }, trailing = {
        Checkbox(
            checked = setting.value.toBoolean(),
            onCheckedChange = { onSettingChanged(setting.copy(value = (!setting.value.toBoolean()).toString())) })
    })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderStringSetting(
    setting: Setting,
    settingDescriptor: ISettingDescriptor,
    onSettingChanged: (Setting) -> Unit,
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
    setting: Setting,
    settingDescriptor: ISettingDescriptor,
    onSettingChanged: (Setting) -> Unit,
) {

}