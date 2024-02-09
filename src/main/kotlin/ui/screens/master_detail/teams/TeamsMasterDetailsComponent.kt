package ui.screens.master_detail.teams

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.IRepositoryObservable
import domain.Team
import domain.User
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ui.dialogs.DialogProperties
import ui.screens.master_detail.BaseMasterDetailsComponent
import ui.screens.team_details.TeamDetailsComponent
import ui.screens.teams_list.TeamsListComponent
import utils.log
import java.time.LocalDateTime

class TeamsMasterDetailsComponent(
    private val di: DI, componentContext: ComponentContext, dbPath: String,
    override val currentUser: User,
) :
    BaseMasterDetailsComponent<Team>(componentContext,
        createMasterComponent = { context: ComponentContext, onItemSelected: (String) -> Unit ->
            TeamsListComponent(
                di = di,
                componentContext = context,
                onItemSelected = onItemSelected,
                dpPath = dbPath,
                currentUser = currentUser
            )
        },
        createDetailsComponent = { context, itemID ->
            TeamDetailsComponent(
                di = di,
                componentContext = context,
                teamID = itemID,
                dpPath = dbPath,
                currentUser = currentUser
            )
        }
    ) {


    private val repo: IRepositoryObservable<Team> by di.instance()

    override fun getAddNewEntityDialogProperties(): DialogProperties =
        DialogProperties(
            title = "добавить команду",
            hint = "имя команды",
            OKButtonText = "добавить"
        )

    override fun onDialogOKClicked(text: String, user: User) {
        createNewTeam(text, user)
    }

    private fun createNewTeam(name: String, creator: User?) {
        scope.launch {
            val newTeam = Team(
                name = name,
                createdAt = LocalDateTime.now(),
                creator = creator
            )
            try {
                repo.insert(newTeam)
            } catch (e: Throwable) {
                log("error during saving new team: ")
                log(e.localizedMessage)
            }
        }
    }

    @Parcelize
    private sealed class DialogConfig : Parcelable {
        @Parcelize
        object NONE : DialogConfig()

        @Parcelize
        class AddNewTeamDialog : DialogConfig()
    }

}