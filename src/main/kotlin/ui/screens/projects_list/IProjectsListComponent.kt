package ui.screens.projects_list

import domain.Project
import domain.User
import ui.screens.master_detail.IMasterComponent

interface IProjectsListComponent : IMasterComponent<Project> {
//    val dialogSlot: Value<ChildSlot<*, IDialogComponent>>

    /**
     * delete the project (if user is a team creator)
     */
    fun deleteProject()
}