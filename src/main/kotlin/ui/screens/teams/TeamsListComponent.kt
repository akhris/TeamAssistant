package ui.screens.teams

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.Team
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import org.kodein.di.DI
import ui.dialogs.IDialogComponent
import ui.dialogs.text_input_dialog.DialogTextInputComponent


class TeamsListComponent(
    private val di: DI,
    componentContext: ComponentContext
) : ITeamsListComponent, ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val dialogNavigation = SlotNavigation<DialogConfig>()

    override val dialog: Value<ChildSlot<*, IDialogComponent>> =
        childSlot(
            source = dialogNavigation,
            // persistent = false, // Disable navigation state saving, if needed
            handleBackButton = true, // Close the dialog on back button press
        ) { config, childComponentContext ->
            when (config) {
                DialogConfig.NewTeamDialog ->
                    DialogTextInputComponent(
                        componentContext = childComponentContext,
                        message = "имя новой команды:",
                        onDismissed = dialogNavigation::dismiss
                    )
            }
        }

    override val teams: Value<List<Team>>
        get() = TODO("Not yet implemented")

    override fun createNewTeam() {
        dialogNavigation.activate(DialogConfig.NewTeamDialog)
    }

    override fun deleteTeam() {
        TODO("Not yet implemented")
    }


    private suspend fun invalidateTeams() {

    }


    init {
        componentContext
            .lifecycle
            .subscribe(onDestroy = {
                scope.coroutineContext.cancelChildren()
            })

    }


    private sealed class DialogConfig() : Parcelable {

        @Parcelize
        object NewTeamDialog : DialogConfig()
    }


}