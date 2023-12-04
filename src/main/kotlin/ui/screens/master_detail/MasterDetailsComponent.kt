package ui.screens.master_detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.IEntity
import domain.IRepositoryObservable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ui.FABState
import ui.dialogs.IDialogComponent
import ui.dialogs.text_input_dialog.DialogTextInputComponent
import ui.screens.BaseComponent
import ui.screens.master_detail.details.DetailsComponent
import ui.screens.master_detail.master.MasterComponent
import ui.screens.tasks_list.TasksListComponent
import utils.log

class MasterDetailsComponent<T : IEntity>(
    private val repo: IRepositoryObservable<T>,
    componentContext: ComponentContext,

    ) : IMasterDetailComponent<T>, BaseComponent(componentContext) {

    private val masterNavigation = StackNavigation<MasterConfig>()
    private val detailsNavigation = StackNavigation<DetailsConfig>()

    private val dialogNavigation = SlotNavigation<DialogConfig>()

    override val dialogSlot: Value<ChildSlot<*, IDialogComponent>> =
        childSlot(
            source = dialogNavigation,
            // persistent = false, // Disable navigation state saving, if needed
            handleBackButton = true, // Close the dialog on back button press
        ) { config, childComponentContext ->
            when (config) {
                DialogConfig.NewItemDialog ->
                    DialogTextInputComponent(
                        componentContext = childComponentContext,
                        hint = "имя новой задачи",
                        title = "добавить задачу",
                        OKButtonText = "добавить",
                        onDismissed = dialogNavigation::dismiss
                    )
            }
        }

    private val _fabState = MutableValue(
        FABState.VISIBLE(
            iconPath = "vector/add_black_24dp.svg",
            text = "добавить",
            description = "добавить новый элемент в список"
        )
    )

    override val fabState: Value<FABState> = _fabState

    override val masterStack: Value<ChildStack<*, IMasterDetailComponent.Master<T>>> = childStack(
        source = masterNavigation,
        initialConfiguration = MasterConfig.ItemsList,
//            handleBackButton = true,
        childFactory = ::createChild,
        key = "master stack"
    )

    override val detailsStack: Value<ChildStack<*, IMasterDetailComponent.Details<T>>> = childStack(
        source = detailsNavigation,
        initialConfiguration = DetailsConfig.None,
        childFactory = ::createChild,
        key = "details stack"
    )

    override fun onFABClicked() {
        dialogNavigation.activate(DialogConfig.NewItemDialog)
    }

    override fun createNewItem(item: T) {
        scope.launch {
            repo.insert(item)
        }
    }

    private fun createChild(
        config: MasterConfig,
        componentContext: ComponentContext,
    ): IMasterDetailComponent.Master<T> {
        return when (config) {
            MasterConfig.ItemsList -> IMasterDetailComponent.Master.ItemsList(
                MasterComponent(
                    repo,
                    componentContext,
                    onItemSelected = {
                        log("onItemSelected: $it")
                        //navigate to item details
                        detailsNavigation.replaceCurrent(DetailsConfig.ItemDetails(it))
                    }
                )
            )
        }
    }

    private fun createChild(
        config: DetailsConfig,
        componentContext: ComponentContext,
    ): IMasterDetailComponent.Details<T> {
        return when (config) {
            is DetailsConfig.ItemDetails -> IMasterDetailComponent.Details.ItemDetails(
                DetailsComponent(
                    repo = repo,
                    entityID = config.itemID,
                    componentContext = componentContext
                )
            )

            DetailsConfig.None -> IMasterDetailComponent.Details.None()
        }
    }

    @Parcelize
    private sealed class MasterConfig : Parcelable {
        object ItemsList : MasterConfig()
    }


    @Parcelize
    private sealed class DetailsConfig : Parcelable {
        object None : DetailsConfig()
        class ItemDetails(val itemID: String) : DetailsConfig()
    }

    private sealed class DialogConfig() : Parcelable {

        @Parcelize
        object NewItemDialog : DialogConfig()
    }

}