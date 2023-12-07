package ui.screens.users_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.EntitiesList
import domain.Team
import domain.User
import ui.EntitiesListUi
import ui.ItemRenderer
import ui.SelectableMode
import ui.screens.master_detail.IMasterComponent

@Composable
fun UsersListUi(component: IMasterComponent<User>) {
    val users by remember(component) { component.items }.collectAsState(EntitiesList.empty())

    EntitiesListUi(
        users,
        selectableMode = SelectableMode.NonSelectable,
        itemRenderer = object : ItemRenderer<User> {
            override fun getPrimaryText(item: User) = item.getInitials()

            override fun getSecondaryText(item: User) = ""

            override fun getOverlineText(item: User) = ""

        }
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderUserItem(user: User, onTeamClick: () -> Unit) {
    Card(modifier = Modifier.clickable { onTeamClick() }) {
        ListItem(modifier = Modifier.padding(4.dp), text = {
            Text(text = user.name)
        })
    }
}