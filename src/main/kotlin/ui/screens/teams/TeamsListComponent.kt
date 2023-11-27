package ui.screens.teams

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.EntitiesList
import domain.Specification
import domain.Team
import domain.User
import domain.application.Result
import domain.application.baseUseCases.GetEntities
import domain.application.baseUseCases.InsertEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import org.kodein.di.DI
import org.kodein.di.instance
import persistence.realm.RealmTeam
import persistence.realm.toTeam
import ui.dialogs.IDialogComponent
import ui.dialogs.text_input_dialog.DialogTextInputComponent
import utils.UserUtils
import utils.log
import java.time.LocalDateTime


class TeamsListComponent(
    private val di: DI,
    componentContext: ComponentContext
) : ITeamsListComponent, ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val dialogNavigation = SlotNavigation<DialogConfig>()

    private val userID = UserUtils.getUserID()
    private val realm by di.instance<Realm>()

    val realmTeams = realm
        .query<RealmTeam>("admins._id == $0 OR members._id == $0 OR creator._id == $0", userID)
        .find()
        .asFlow()
        .map {
            when (it) {
                is InitialResults -> {
                    it.list.map { it.toTeam() }
                }

                is UpdatedResults -> {
                    it.list.map { it.toTeam() }
                }
            }
        }


    private val getTeams: GetEntities<Team> by di.instance()
    private val insertTeam: InsertEntity<Team> by di.instance()

    private val _teams = MutableValue<List<Team>>(listOf())

    override val dialogSlot: Value<ChildSlot<*, IDialogComponent>> =
        childSlot(
            source = dialogNavigation,
            // persistent = false, // Disable navigation state saving, if needed
            handleBackButton = true, // Close the dialog on back button press
        ) { config, childComponentContext ->
            when (config) {
                DialogConfig.NewTeamDialog ->
                    DialogTextInputComponent(
                        componentContext = childComponentContext,
                        hint = "имя новой команды",
                        title = "добавить команду",
                        OKButtonText = "добавить",
                        onDismissed = dialogNavigation::dismiss
                    )
            }
        }

    override val teams: Value<List<Team>> = _teams


    override fun createNewTeamRequest() {
        dialogNavigation.activate(DialogConfig.NewTeamDialog)
    }

    override fun createNewTeam(name: String, creator: User?) {
        scope.launch {
            val newTeam = Team(
                name = name,
                createdAt = LocalDateTime.now(),
                creator = creator
            )
            try {
                insertTeam(InsertEntity.Insert(newTeam))
            } catch (e: Throwable) {
                log("error during saving new team: ")
                log(e.localizedMessage)
            }
        }
    }

    override fun deleteTeam() {
        TODO("Not yet implemented")
    }


    private suspend fun invalidateTeams() {
        val allTeamsForUser = getTeams(GetEntities.Params.GetWithSpecification(Specification.GetAllForUserID(userID)))
        when (allTeamsForUser) {
            is Result.Failure -> {

            }

            is Result.Success -> {
                when (val list = allTeamsForUser.value) {
                    is EntitiesList.Grouped -> {

                    }

                    is EntitiesList.NotGrouped -> {
                        _teams.value = list.items
                    }
                }
            }
        }
    }


    init {
        componentContext
            .lifecycle
            .subscribe(onDestroy = {
                scope.coroutineContext.cancelChildren()
            })

        scope.launch {
            invalidateTeams()
        }
    }


    private sealed class DialogConfig() : Parcelable {

        @Parcelize
        object NewTeamDialog : DialogConfig()
    }


}