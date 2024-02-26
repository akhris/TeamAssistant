package ui.screens.master_detail

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import ui.UiSettings
import java.awt.Cursor

/**
 * Base implementation of Master Details Ui pattern.
 * Ui-only (no components)
 * Used by [MasterDetailsUi]
 */
@Composable
internal fun BaseMasterDetailsUi(
    renderMaster: @Composable() (BoxScope.() -> Unit),
    renderItemDetails: @Composable() (BoxScope.() -> Unit),
) {

    var masterWidth by remember { mutableStateOf(UiSettings.MasterDetailsScreen.masterPanelWidth) }

    Row {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(masterWidth)
                .padding(UiSettings.Screen.screenPadding)
        ) {
            renderMaster()
        }
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .pointerHoverIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR)))
                .draggable(
                    state = rememberDraggableState { onDelta ->
                        masterWidth += onDelta.dp
                    },
                    orientation = Orientation.Horizontal
                )

        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(UiSettings.Screen.screenPadding)
        ) {
            //Details screen
            renderItemDetails()
        }

    }
}