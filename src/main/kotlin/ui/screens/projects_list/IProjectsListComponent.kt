package ui.screens.projects_list

import domain.Project
import domain.User
import ui.screens.master_detail.IMasterComponent

interface IProjectsListComponent : IMasterComponent<Project> {
//    val dialogSlot: Value<ChildSlot<*, IDialogComponent>>

    /**
     * Show a dialog for creating new project
     */
    fun createNewProjectRequest()

    /**
     * Create new project and store it in database
     */
    fun createNewProject(name: String, creator: User?)

    /**
     * delete the project (if user is a team creator)
     */
    fun deleteProject()
}