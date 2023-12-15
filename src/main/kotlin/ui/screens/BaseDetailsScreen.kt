package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.UiSettings
import utils.applyTextStyle


@Composable
fun BaseDetailsScreen(
    mainTag: @Composable (() -> Unit)? = null,
    secondaryTag: @Composable (() -> Unit)? = null,
    attachments: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    description: @Composable (() -> Unit)? = null,
    comments: @Composable (BoxScope.() -> Unit)? = null,
    rightPanel: @Composable (ColumnScope.() -> Unit)? = null,
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
                        attachments?.invoke()
                    }
                }
                comments?.let { com ->
                    Box(modifier = Modifier.weight(1f)) {
                        // comments
                        com()
                    }
                }
            }
        },
        additionalContent = rightPanel?.let { rp ->
            {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    rp()
                }
            }
        }
    )
}

@Composable
private fun BaseDetailsScreenPattern(
    mainContent: @Composable BoxScope.() -> Unit,
    additionalContent: @Composable (BoxScope.() -> Unit)? = null,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colors.surface)
                .padding(UiSettings.DetailsScreen.mainPanelPadding)
        ) {
            //main content
            mainContent()
        }
        additionalContent?.let { ac ->
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



