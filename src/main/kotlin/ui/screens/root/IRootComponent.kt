package ui.screens.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import domain.User
import kotlinx.coroutines.flow.Flow
import ui.screens.db_selector.IDBSelectorComponent
import ui.screens.logged_in_root.ILoggedInRootComponent
import ui.screens.user_create.IUserCreateComponent
import java.nio.file.Path

interface IRootComponent {

    val rootStack: Value<ChildStack<*, Screen>>

    val currentDBPath: Value<String>

    fun loadDatabase()

    sealed class Screen {
        object NONE: Screen()
        class DBSelector(val component: IDBSelectorComponent) : Screen()
        class UserCreate(val component: IUserCreateComponent) : Screen()
        class LoggedIn(val component: ILoggedInRootComponent) : Screen()

    }
}

data class UserLoggingInfo(
    val userID: String = "",
    val user: User? = null,
)