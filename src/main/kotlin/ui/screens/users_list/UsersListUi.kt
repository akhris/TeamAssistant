package ui.screens.users_list

import LocalCurrentUser
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import domain.EntitiesList
import domain.User
import ui.EntitiesListUi
import ui.ItemRenderer
import ui.SelectableMode
import ui.screens.master_detail.IMasterComponent

@Composable
fun UsersListUi(component: IMasterComponent<User>) {
    val users by remember(component) { component.items }.collectAsState(EntitiesList.empty())
    val currentUserID = LocalCurrentUser.current?.id
    val currentUserColor = MaterialTheme.colors.primary

    EntitiesListUi(
        users,
        selectableMode = SelectableMode.NonSelectable(onItemClicked = {
            component.onItemClicked(it)
        }),
        itemRenderer = object : ItemRenderer<User> {
            override fun getPrimaryText(item: User) = item.getInitials()

            override fun getSecondaryText(item: User) = ""

            override fun getOverlineText(item: User) = ""

            override fun getIconPath(item: User): String = "vector/users/person_black_24dp.svg"

            override fun getIconTint(item: User): Color? = if (item.id == currentUserID) {
                currentUserColor
            } else null
        }
    )

}
