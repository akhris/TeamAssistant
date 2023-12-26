package ui.dialogs.entity_picker_dialogs

import com.arkivanov.decompose.ComponentContext
import domain.*
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DI
import org.kodein.di.instance
import ui.ItemRenderer
import ui.SelectMode
import ui.screens.BaseComponent

class ProjectPickerComponent(
    val isMultipleSelection: Boolean,
    override val initialSelection: List<Project>,
    val onProjectsPicked: (List<Project>) -> Unit,
    di: DI,
    componentContext: ComponentContext,
) : IBaseEntityPickerDialogComponent<Project>, BaseComponent(componentContext) {

    private val repo: IRepositoryObservable<Project> by di.instance()

    override val items: Flow<EntitiesList<Project>> = repo.query(listOf(Specification.QueryAll))

    override val selectMode: SelectMode = when (isMultipleSelection) {
        true -> SelectMode.MULTIPLE
        false -> SelectMode.SINGLE
    }

    override val itemRenderer: ItemRenderer<Project> = object : ItemRenderer<Project> {
        override fun getPrimaryText(item: Project): String = item.name
        override fun getIconPath(item: Project): String = item.icon.ifEmpty { "vector/projects/rocket_black_24dp.svg" }
    }
    override val title: String = "выбор проекта"

    override fun onItemsSelected(items: List<Project>) {
        onProjectsPicked(items)
    }
}