package ui.screens.master_detail.tasks

import com.arkivanov.decompose.ComponentContext
import domain.Task
import org.kodein.di.DI
import ui.screens.master_detail.BaseMasterDetailsComponent
import ui.screens.task_details.TaskDetailsComponent
import ui.screens.tasks_list.TasksListComponent

class TasksMasterDetailsComponent(private val di: DI, componentContext: ComponentContext) :
    BaseMasterDetailsComponent<Task>(componentContext = componentContext,
        createMasterComponent = { componentContext: ComponentContext, onItemSelected: (String) -> Unit ->
            TasksListComponent(di = di, componentContext = componentContext, onTaskSelected = onItemSelected)
        },
        createDetailsComponent = {componentContext, itemID->
            TaskDetailsComponent(di = di, componentContext = componentContext, taskID = itemID)
        }
        ) {
}