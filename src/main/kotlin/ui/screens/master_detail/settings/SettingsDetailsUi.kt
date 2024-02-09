package ui.screens.master_detail.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import domain.settings.ISettingDescriptor
import domain.settings.Setting
import domain.settings.SettingType
import domain.settings.extensions
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

    val loadedSettings by remember(component) { settingsDetailsComponent.settings }.subscribeAsState()

    var tempSettings by remember(loadedSettings) { mutableStateOf(loadedSettings) }

    val descriptor = remember(settingsDetailsComponent) { settingsDetailsComponent.settingDescriptor }
    Box(modifier = Modifier.fillMaxSize()) {
        RenderSettingsList(
            settings = tempSettings,
            settingDescriptor = descriptor,
            onSettingChanged = { changedSetting ->
                tempSettings = tempSettings.map {
                    if (it.id == changedSetting.id) {
                        changedSetting
                    } else it
                }
            }
        )


        if (tempSettings != loadedSettings) {
            //show panel
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Настройки были изменены.")
                    TextButton(onClick = {
                        tempSettings.forEach {
                            settingsDetailsComponent.updateSetting(it)
                        }
                    }, content = { Text("Сохранить") })
                    TextButton(onClick = {
                        tempSettings = loadedSettings
                    }, content = { Text("Отмена") })
                }
            }
        }

    }


}

@Composable
private fun RenderSettingsList(
    settings: List<Setting>,
    settingDescriptor: ISettingDescriptor,
    onSettingChanged: (Setting) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        settings.forEach { setting ->
            when (setting) {
                is Setting.BooleanSetting -> RenderBooleanSetting(setting, settingDescriptor, onSettingChanged)
                is Setting.PathSetting -> RenderPathSetting(
                    setting = setting,
                    settingDescriptor = settingDescriptor,
                    onSettingChanged = onSettingChanged
                )
                else -> Text("rendering of ${setting::class.java.simpleName}not yet implemented")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderBooleanSetting(
    setting: Setting.BooleanSetting,
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
            checked = setting.stringValue.toBoolean(),
            onCheckedChange = { onSettingChanged(setting.copy(stringValue = (!setting.stringValue.toBoolean()).toString())) })
    })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderStringSetting(
    setting: Setting.StringSetting,
    settingDescriptor: ISettingDescriptor,
    onSettingChanged: (Setting) -> Unit,
) {
    var tempString by remember(setting) { mutableStateOf(setting.stringValue) }
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
        if (tempString == setting.stringValue) {
            return@LaunchedEffect
        }
        delay(UiSettings.Debounce.debounceTime)
        onSettingChanged(setting.copy(stringValue = tempString))
    }
}

@Composable
fun RenderPathSetting(
    modifier: Modifier = Modifier,
    setting: Setting.PathSetting,
//    settingType: SettingType.Path,
    settingDescriptor: ISettingDescriptor,
    onSettingChanged: (Setting) -> Unit,
) {
    var tempPath by remember(setting) { mutableStateOf(setting.stringValue) }
    val title = remember(settingDescriptor, setting) { settingDescriptor.getTitle(setting.id) }

    val isError = remember(tempPath) {
        if (tempPath.isEmpty()) {
            true
        } else if (!FileUtils.isPathValid(tempPath, extensions = setting.extensions())) {
            true
        } else false
    }


    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
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
                    title = title ?: "", folderSelection = false, filters = setting.extensions().map {
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


    LaunchedEffect(tempPath) {
        if (tempPath == setting.stringValue || isError) {
            return@LaunchedEffect
        }
        delay(UiSettings.Debounce.debounceTime)

        onSettingChanged(setting.copy(stringValue = tempPath))

    }


}