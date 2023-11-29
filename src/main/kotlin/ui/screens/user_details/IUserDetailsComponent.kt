package ui.screens.user_details

import domain.User
import kotlinx.coroutines.flow.Flow

interface IUserDetailsComponent {
    fun updateUser(user: User)

    val user: Flow<User>
}