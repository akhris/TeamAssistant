package ui.screens.master_detail.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.settings.ISettingDescriptor
import domain.settings.Setting
import domain.settings.SettingType
import kotlinx.coroutines.delay
import ui.UiSettings
import ui.dialogs.file_picker_dialog.fileChooserDialog
import ui.fields.EditableTextField
import ui.screens.master_detail.IDetailsComponent
import utils.FileUtils
import javax.swing.filechooser.FileNameExtensionFilter

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
    Column(modifier = Modifier.fillMaxSize()) {
        settings.forEach { setting ->
            when (val type = settingDescriptor.getType(setting.id)) {
                SettingType.Boolean -> RenderBooleanSetting(setting, settingDescriptor, onSettingChanged)
                SettingType.String -> RenderStringSetting(setting, settingDescriptor, onSettingChanged)
                is SettingType.Path -> RenderPathSetting(setting, type, settingDescriptor, onSettingChanged)
                null -> {

                }
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
    settingType: SettingType.Path,
    settingDescriptor: ISettingDescriptor,
    onSettingChanged: (Setting) -> Unit,
) {
    var tempPath by remember(setting) { mutableStateOf(setting.value) }
    val title = remember(settingDescriptor, setting) { settingDescriptor.getTitle(setting.id) }

    val isError = remember(tempPath) {
        if (tempPath.isEmpty()) {
            true
        } else if (!FileUtils.isPathValid(tempPath, extensions = settingType.extensions)) {
            true
        } else false
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        EditableTextField(
            modifier = Modifier.weight(1f),
            value = tempPath,
            onValueChange = {
                tempPath = it
            },
            isError = isError,
            label = title ?: "",
            singleLine = true
        )
        IconButton(
            modifier = Modifier.padding(8.dp),
            onClick = {
                //open file picker
                val filePicked = fileChooserDialog(
                    title = title ?: "", folderSelection = false, filters = settingType.extensions.map {
                        FileNameExtensionFilter(it, ".$it")
                    }
                )
                if (filePicked.isNotEmpty()) {
                    tempPath = filePicked
                }
            }, content = {
                Icon(
                    painter = painterResource("/vector/folder_black_24dp.svg"),
                    contentDescription = "open file picker"
                )
            })
    }

    // TODO: make showing up button and alert text to save new value
    //  "database file has changed - need a restart to take effect"
/*
    LaunchedEffect(tempPath) {
        if (tempPath == setting.value || isError) {
            return@LaunchedEffect
        }
        delay(UiSettings.Debounce.debounceTime)
        onSettingChanged(setting.copy(value = tempPath))
    }
 */

}