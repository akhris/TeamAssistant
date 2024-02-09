package ui.screens.user_create

import com.arkivanov.decompose.ComponentContext
import domain.IRepositoryObservable
import domain.User
import domain.application.SettingsUseCase
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.factory
import settings.DatabaseArguments
import settings.Settings
import ui.screens.BaseComponent
import utils.UserUtils
import utils.log
import java.time.LocalDateTime

class UserCreateComponent(
    private val di: DI,
    componentContext: ComponentContext,
    private val dbPath: String,
    onUserCreated: (User) -> Unit,
) : IUserCreateComponent, BaseComponent(componentContext) {

    private val userID = UserUtils.getUserID()

    private val usersRepoFactory: (DatabaseArguments) -> IRepositoryObservable<User> by di.factory()
    private val settingsUseCaseFactory: (DatabaseArguments) -> SettingsUseCase by di.factory()
    override fun createUser(name: String, surname: String) {
        if (dbPath.isEmpty()) {
            log("Cannot add new user: DBPath is empty", this.toString())
            return
        }
        val usersRepo = usersRepoFactory(DatabaseArguments(path = dbPath))
        val settingsUseCase = settingsUseCaseFactory(DatabaseArguments(path = dbPath))
        scope.launch {
            val isCreator =
                userID == settingsUseCase.getSetting(Settings.DB.SETTING_ID_DB_CREATOR).stringValue

            val newUser = User(
                id = userID,
                name = name,
                surname = surname,
                isDBCreator = isCreator,
                createdAt = LocalDateTime.now()
            )

            usersRepo.insert(
                newUser
            )
        }
    }

}