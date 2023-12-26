package ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import utils.applyTextStyle

@Composable
fun BaseDialogWindow(
    state: DialogState = rememberDialogState(),
    onCloseRequest: () -> Unit,
    title: @Composable (() -> Unit)? = null,
    buttons: @Composable (RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val styledTitle = applyTextStyle(
        textStyle = MaterialTheme.typography.h5,
        contentAlpha = ContentAlpha.high,
        text = title
    )


    DialogWindow(
        state = state,
        onCloseRequest = onCloseRequest,
        undecorated = true,
        resizable = false,
        transparent = true,
        content = {
            Surface(shape = RoundedCornerShape(8.dp)) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    styledTitle?.let { t ->
                        WindowDraggableArea {
                            //title
                            Box(
                                modifier = Modifier.height(48.dp).padding(vertical = 8.dp),
                                contentAlignment = Alignment.BottomStart
                            ) {
                                t()
                            }
                        }
                    }
                    content()
                    buttons?.let { b ->
                        Row(modifier = Modifier.fillMaxWidth().padding(4.dp), horizontalArrangement = Arrangement.End) {
                            b()
                        }
                    }
                }
            }
        }
    )
}