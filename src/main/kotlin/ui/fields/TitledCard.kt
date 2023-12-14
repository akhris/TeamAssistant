package ui.fields

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import utils.applyTextStyle


@Composable
fun TitledCard(title: @Composable (() -> Unit)? = null, content: @Composable () -> Unit) {

    val styledTitle = applyTextStyle(MaterialTheme.typography.overline, ContentAlpha.high, title)

    Card {
        Column(modifier = Modifier.padding(4.dp)) {
            styledTitle?.let { t ->
                Box(modifier = Modifier.fillMaxWidth().height(24.dp).padding(4.dp)) {
                    t()
                }
            }
            Box(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                content()
            }
        }
    }
}

