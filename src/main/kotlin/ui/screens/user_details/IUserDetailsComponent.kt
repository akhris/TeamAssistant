package ui.screens.user_details

import domain.User
import kotlinx.coroutines.flow.Flow
import ui.screens.master_detail.IDetailsComponent

interface IUserDetailsComponent : IDetailsComponent<User> {
    fun updateUser(user: User)

}