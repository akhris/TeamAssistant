package ui.screens.db_selector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import settings.Settings
import ui.dialogs.file_picker_dialog.fileChooserDialog
import ui.fields.EditableTextField
import utils.FileUtils
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.io.path.Path
import kotlin.io.path.exists

@Composable
fun DBSelectorUi(component: IDBSelectorComponent) {
    val lastOpenedDBPaths by remember(component) { component.lastOpenedDBPaths }.subscribeAsState()
    val currentDBPath by remember(component) { component.currentDBPath }.subscribeAsState()
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        RenderPathSelector(currentPath = currentDBPath, onPathChanged = {
            component.setCurrentDBPath(it)
        })
        Text(modifier = Modifier.padding(16.dp), text = "последние файлы:", style = MaterialTheme.typography.h6)
        lastOpenedDBPaths.forEach {
            Text(modifier = Modifier.padding(8.dp).clickable {
                component.setCurrentDBPath(it)
            }, text = it)
        }

    }

}

@Composable
private fun ColumnScope.RenderPathSelector(
    modifier: Modifier = Modifier,
    currentPath: String,
    onPathChanged: (String) -> Unit,
) {
    var tempPath by remember(currentPath) { mutableStateOf(currentPath.ifEmpty { Settings.DB.DEFAULT_DB_PATH.stringValue }) }


    val isError = remember(tempPath) {
        if (tempPath.isEmpty()) {
            true
        } else if (!FileUtils.isPathValid(tempPath, extensions = listOf("realm"))) {
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
            label = "файл базы данных",
            singleLine = true
        )
        IconButton(
            modifier = Modifier.padding(8.dp),
            onClick = {
                //open file picker
                val filePicked = fileChooserDialog(
                    title = "открыть файл", folderSelection = false, filters = listOf(
                        FileNameExtensionFilter("Realm database", ".realm")
                    )
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
    if (tempPath.isNotEmpty()) {
        Button(onClick = {
            onPathChanged(tempPath)
        },
            enabled = !isError,
            content = {

                Text(
                    text = if (Path(tempPath).exists()) {
                        "открыть"
                    } else "создать"
                )

            })
    }
}