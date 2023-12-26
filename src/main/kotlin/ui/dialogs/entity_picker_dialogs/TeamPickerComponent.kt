package ui.dialogs.entity_picker_dialogs

import com.arkivanov.decompose.ComponentContext
import domain.*
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DI
import org.kodein.di.instance
import ui.ItemRenderer
import ui.SelectMode
import ui.screens.BaseComponent

class TeamPickerComponent(
    isMultipleSelection: Boolean,
    override val initialSelection: List<Team>,
    val onTeamsPicked: (List<Team>) -> Unit,
    di: DI,
    componentContext: ComponentContext,
) : IBaseEntityPickerDialogComponent<Team>, BaseComponent(componentContext) {

    private val repo: IRepositoryObservable<Team> by di.instance()

    override val items: Flow<EntitiesList<Team>> = repo.query(listOf(Specification.QueryAll))

    override val selectMode: SelectMode = when (isMultipleSelection) {
        true -> SelectMode.MULTIPLE
        false -> SelectMode.SINGLE
    }

    override val itemRenderer = object : ItemRenderer<Team> {
        override fun getPrimaryText(item: Team): String = item.name
        override fun getIconPath(item: Team): String = "vector/people_black_24dp.svg"
    }
    override val title: String = "выбор команд"

    override fun onItemsSelected(items: List<Team>) {
        onTeamsPicked(items)
    }
}