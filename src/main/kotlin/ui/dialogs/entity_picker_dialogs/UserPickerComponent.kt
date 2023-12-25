package ui.dialogs.entity_picker_dialogs

import com.arkivanov.decompose.ComponentContext
import domain.EntitiesList
import domain.IRepositoryObservable
import domain.Specification
import domain.User
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DI
import org.kodein.di.instance
import ui.ItemRenderer
import ui.dialogs.entity_picker_dialogs.IBaseEntityPickerDialogComponent
import ui.dialogs.entity_picker_dialogs.SelectMode
import ui.screens.BaseComponent

class UserPickerComponent(
    val isMultipleSelection: Boolean,
    override val initialSelection: List<User>,
    val onUsersPicked: (List<User>) -> Unit,
    di: DI,
    componentContext: ComponentContext,
) : IBaseEntityPickerDialogComponent<User>, BaseComponent(componentContext) {

    private val repo: IRepositoryObservable<User> by di.instance()

    override val items: Flow<EntitiesList<User>> = repo.query(listOf(Specification.QueryAll))

    override val selectMode: SelectMode = when (isMultipleSelection) {
        true -> SelectMode.MULTIPLE
        false -> SelectMode.SINGLE
    }

    override val itemRenderer: ItemRenderer<User> = object : ItemRenderer<User> {
        override fun getPrimaryText(item: User): String = item.getInitials()
        override fun getIconPath(item: User): String = "vector/users/person_black_24dp.svg"
    }
    override val title: String = "выбор пользователей"

    override fun onItemsSelected(items: List<User>) {
        onUsersPicked(items)
    }
}