package ui.screens.master_detail.projects

import com.arkivanov.decompose.ComponentContext
import domain.Project
import org.kodein.di.DI
import ui.screens.master_detail.BaseMasterDetailsComponent
import ui.screens.project_details.ProjectDetailsComponent
import ui.screens.projects_list.ProjectsListComponent

class ProjectsMasterDetailsComponent(private val di: DI, componentContext: ComponentContext) :
    BaseMasterDetailsComponent<Project>(componentContext = componentContext,
        createMasterComponent = { componentContext: ComponentContext, onItemSelected: (String) -> Unit ->
            ProjectsListComponent(di = di, componentContext = componentContext, onItemSelected = onItemSelected)
        },
        createDetailsComponent = {componentContext, itemID->
            ProjectDetailsComponent(di = di, componentContext = componentContext, projectID = itemID)
        }) {
}