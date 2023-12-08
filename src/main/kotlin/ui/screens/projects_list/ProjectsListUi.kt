package ui.screens.projects_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.EntitiesList
import domain.Project
import domain.Team
import ui.EntitiesListUi
import ui.ItemRenderer
import ui.SelectableMode
import ui.screens.master_detail.IMasterComponent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProjectsListUi(component: IMasterComponent<Project>) {
    val projects by remember(component) { component.items }.collectAsState(EntitiesList.empty())

    EntitiesListUi(
        projects,
        selectableMode = SelectableMode.NonSelectable(onItemClicked = {
            component.onItemClicked(it)
        }),
        itemRenderer = object : ItemRenderer<Project> {
            override fun getPrimaryText(item: Project) = item.name

            override fun getSecondaryText(item: Project) = item.creator?.getInitials() ?: ""

            override fun getOverlineText(item: Project) = null

            override fun getIconPath(item: Project): String = "vector/rocket_black_24dp.svg"

            override fun getIconTint(item: Project): Color? = item.color?.let { Color(it) }

        }
    )
}
