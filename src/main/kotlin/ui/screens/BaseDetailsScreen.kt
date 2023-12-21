package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.FABState
import ui.UiSettings
import utils.applyTextStyle


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BaseDetailsScreen(
    mainTag: @Composable() (() -> Unit)? = null,
    secondaryTag: @Composable() (() -> Unit)? = null,
    attachments: @Composable() (ColumnScope.() -> Unit)? = null,
    title: @Composable() (() -> Unit)? = null,
    description: @Composable() (() -> Unit)? = null,
    rightPanel: @Composable() (ColumnScope.() -> Unit)? = null,
    bottomSheetContent: @Composable (ColumnScope.() -> Unit)? = null,
    bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
) {

    val styledMaintag = applyTextStyle(textStyle = MaterialTheme.typography.overline, text = mainTag)
    val styledSecondaryTag = applyTextStyle(textStyle = MaterialTheme.typography.body2, text = secondaryTag)
    val styledTitle = applyTextStyle(textStyle = MaterialTheme.typography.h5, text = title)
    val styledDescription = applyTextStyle(textStyle = MaterialTheme.typography.caption, text = description)

    BaseDetailsScreenPattern(
        mainContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // main tag
                        styledMaintag?.invoke()
                        // title
                        styledTitle?.invoke()
                        // description
                        styledDescription?.invoke()
                        //secondary tags:
                        styledSecondaryTag?.invoke()
                        //attachments:
                        attachments?.invoke(this)
                    }
                }
            }
        },
        rightPanelContent = rightPanel?.let { rp ->
            {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    rp()
                }
            }
        },
        bottomSheetContent = bottomSheetContent,
        bottomSheetState = bottomSheetState
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BaseDetailsScreenPattern(
    mainContent: @Composable BoxScope.() -> Unit,
    rightPanelContent: @Composable (BoxScope.() -> Unit)? = null,
    bottomSheetContent: @Composable (ColumnScope.() -> Unit)? = null,
    bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
) {
    Row(modifier = Modifier.fillMaxSize()) {
        if (bottomSheetContent != null) {
            ModalBottomSheetLayout(
                modifier = Modifier.weight(1f),
                sheetState = bottomSheetState,
                sheetShape = UiSettings.DetailsScreen.bottomSheetShape,
                sheetContent = {
                    bottomSheetContent()
                }
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colors.surface)
                        .padding(UiSettings.DetailsScreen.mainPanelPadding)
                ) {
                    //main content
                    mainContent()
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colors.surface)
                    .padding(UiSettings.DetailsScreen.mainPanelPadding)
            ) {
                //main content
                mainContent()
            }
        }


        rightPanelContent?.let { ac ->
            Box(
                modifier = Modifier
                    .width(UiSettings.DetailsScreen.rightPanelWidth)
                    .background(MaterialTheme.colors.background)
                    .padding(4.dp)
            ) {
                //additional content
                ac()
            }
        }
    }
}



