package ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase

@Composable
fun SideNavigationPanel(
    isExpandable: Boolean = false,
    withLabels: Boolean = false,
    currentSelection: NavItem? = null,
    onNavigationItemSelected: (NavItem) -> Unit,
) {

    var isExpanded by remember { mutableStateOf(false) }

    val panelWidth by animateDpAsState(
        when (isExpanded) {
            true -> UiSettings.NavigationPanel.widthExpanded
            false -> UiSettings.NavigationPanel.widthCollapsed
        }
    )

    val alwaysShowLabel = remember(panelWidth) {
        panelWidth == UiSettings.NavigationPanel.widthExpanded
    }


    NavigationRail(
        elevation = UiSettings.NavigationPanel.elevation,
        modifier = Modifier.width(panelWidth),
        header = if (isExpandable) {
            {
                IconButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = {
                        isExpanded = !isExpanded
                    },
                    content = {
                        Icon(
                            imageVector = when (isExpanded) {
                                true -> Icons.Rounded.KeyboardArrowLeft
                                false -> Icons.Rounded.KeyboardArrowRight
                            },
                            contentDescription = "expand or collapse icon"
                        )
                    }
                )
            }
        } else null
    ) {
        NavItem
            .getMainNavigationItems()
            .forEach { navItem ->
                RenderNavigationItem(
                    navItem = navItem,
                    isSelected = navItem == currentSelection,
                    alwaysShowLabel = alwaysShowLabel,
                    withLabels = withLabels,
                    onClick = { onNavigationItemSelected(navItem) }
                )
            }
        NavItem.bottomNavigationItem?.let { bni ->
            Spacer(modifier = Modifier.weight(1f))
            RenderNavigationItem(
                navItem = bni,
                isSelected = bni == currentSelection,
                alwaysShowLabel = alwaysShowLabel,
                withLabels = withLabels,
                onClick = { onNavigationItemSelected(bni) }
            )
        }
    }
}

@Composable
private fun RenderNavigationItem(
    navItem: NavItem,
    isSelected: Boolean,
    alwaysShowLabel: Boolean,
    withLabels: Boolean,
    onClick: () -> Unit,
) {
    NavigationRailItem(
        selected = isSelected,
        alwaysShowLabel = alwaysShowLabel,
        icon = {
            Icon(
                painter = painterResource(navItem.pathToIcon),
                contentDescription = navItem.title,
                modifier = Modifier.size(UiSettings.NavigationPanel.iconSize)
            )
        },
        label = if (withLabels) {
            { Text(maxLines = 1, overflow = TextOverflow.Ellipsis, text = navItem.title.lowercase()) }
        } else null,
        onClick = onClick
    )
}