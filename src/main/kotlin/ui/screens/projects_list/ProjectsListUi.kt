package ui.screens.projects_list

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import domain.EntitiesList
import domain.Project
import ui.EntitiesListUi
import ui.ItemRenderer
import ui.SelectMode

import ui.screens.master_detail.IMasterComponent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProjectsListUi(component: IMasterComponent<Project>) {
    val projects by remember(component) { component.items }.collectAsState(EntitiesList.empty())

    EntitiesListUi(
        projects,
        selectMode = SelectMode.NONSELECTABLE,
        itemRenderer = object : ItemRenderer<Project> {
            override fun getPrimaryText(item: Project) = item.name

            override fun getSecondaryText(item: Project) = item.creator?.getInitials() ?: ""

            override fun getOverlineText(item: Project) = null

            override fun getIconPath(item: Project): String = "vector/projects/rocket_black_24dp.svg"

        },
        onItemClicked = { component.onItemClicked(it) }
    )


}
