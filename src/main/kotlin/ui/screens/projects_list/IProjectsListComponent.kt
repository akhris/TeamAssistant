package ui.screens.projects_list

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import domain.EntitiesList
import domain.Project
import domain.User
import kotlinx.coroutines.flow.Flow
import ui.dialogs.IDialogComponent

interface IProjectsListComponent {
    val dialogSlot: Value<ChildSlot<*, IDialogComponent>>

    /**
     * projects available for user
     * (created by user or with user as a member)
     */
    val projects: Flow<EntitiesList<Project>>

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