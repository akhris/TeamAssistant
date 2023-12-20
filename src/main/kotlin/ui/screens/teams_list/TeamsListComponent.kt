package ui.screens.teams_list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.*
import domain.application.baseUseCases.GetEntities
import domain.application.baseUseCases.InsertEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DI
import org.kodein.di.instance
import ui.dialogs.IDialogComponent
import ui.dialogs.text_input_dialog.DialogTextInputComponent
import ui.screens.BaseComponent
import utils.UserUtils
import utils.log
import java.time.LocalDateTime


class TeamsListComponent(
    di: DI,
    componentContext: ComponentContext,
    private val onItemSelected: (String) -> Unit,
) : ITeamsListComponent, BaseComponent(componentContext) {

//    private val dialogNavigation = SlotNavigation<DialogConfig>()

    private val userID = UserUtils.getUserID()

    private val repo: IRepositoryObservable<Team> by di.instance()

    override val filterSpecs: Flow<List<FilterSpec>>? = repo.getFilterSpecs()


//        realm
//        .query<RealmTeam>("admins._id == $0 OR members._id == $0 OR creator._id == $0", userID)
//        .find()
//        .asFlow()
//        .map {
//            when (it) {
//                is InitialResults -> {
//                    it.list.map { it.toTeam() }
//                }
//
//                is UpdatedResults -> {
//                    it.list.map { it.toTeam() }
//                }
//            }
//        }


//    override val dialogSlot: Value<ChildSlot<*, IDialogComponent>> =
//        childSlot(
//            source = dialogNavigation,
//            // persistent = false, // Disable navigation state saving, if needed
//            handleBackButton = true, // Close the dialog on back button press
//        ) { config, childComponentContext ->
//            when (config) {
//                DialogConfig.NewTeamDialog ->
//                    DialogTextInputComponent(
//                        componentContext = childComponentContext,
//                        hint = "имя новой команды",
//                        title = "добавить команду",
//                        OKButtonText = "добавить",
//                        onDismissed = dialogNavigation::dismiss
//                    )
//            }
//        }

    override val items: Flow<EntitiesList<Team>> = repo.query(listOf(Specification.GetAllForUserID(userID)))


    override fun onItemClicked(item: Team) {
        onItemSelected(item.id)
    }

    override fun onAddNewItem(item: Team) {
        scope.launch {
            try {
                repo.insert(item)
            } catch (e: Throwable) {
                log("error during saving new team: ")
                log(e.localizedMessage)
            }
        }
    }


    override fun deleteTeam() {
        scope.launch {
            //remove team here
        }
    }




    private sealed class DialogConfig() : Parcelable {

        @Parcelize
        object NewTeamDialog : DialogConfig()
    }


}