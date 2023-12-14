package ui.fields

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CircleIconButton(
    iconRes: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    IconButton(
        modifier = Modifier
            .border(
                width = 3.dp,
                shape = CircleShape,
                color = if (isSelected) MaterialTheme.colors.primary else Color.Unspecified
            ),
        onClick = onClick,
        content = {
            Icon(
//                modifier = Modifier.size(36.dp),
                painter = painterResource(iconRes),
                contentDescription = null
            )
        }
    )
}