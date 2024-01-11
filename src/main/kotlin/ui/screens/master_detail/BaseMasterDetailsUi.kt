package ui.screens.master_detail

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.UiSettings

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

    Row {
        Box(
            modifier = Modifier
                .fillMaxHeight()
//                .weight(1/3f)
                .requiredWidthIn(min = 64.dp, max = 200.dp)
                .padding(UiSettings.Screen.screenPadding)
        ) {
            renderMaster()
        }
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