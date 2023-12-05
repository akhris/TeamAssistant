package ui.screens.master_detail.users

import com.arkivanov.decompose.ComponentContext
import domain.User
import org.kodein.di.DI
import ui.screens.master_detail.BaseMasterDetailsComponent
import ui.screens.user_details.UserDetailsComponent
import ui.screens.users_list.UsersListComponent

class UsersMasterDetailsComponent(private val di: DI, componentContext: ComponentContext) :
    BaseMasterDetailsComponent<User>(componentContext = componentContext,
        createMasterComponent = { componentContext: ComponentContext, onItemSelected: (String) -> Unit ->
            UsersListComponent(di = di, componentContext = componentContext, onItemSelected = onItemSelected)
        },
        createDetailsComponent = { componentContext, itemID ->
            UserDetailsComponent(di = di, componentContext = componentContext, userID = itemID)
        }) {
}