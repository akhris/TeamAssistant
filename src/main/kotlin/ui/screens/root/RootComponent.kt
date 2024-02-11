package ui.screens.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.IRepositoryObservable
import domain.ISettingsRepository
import domain.User
import domain.settings.Setting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.DI
import org.kodein.di.factory
import org.kodein.di.instance
import settings.DatabaseArguments
import settings.Settings
import ui.screens.BaseComponent
import ui.screens.db_selector.DBSelectorComponent
import ui.screens.logged_in_root.LoggedInRootComponent
import ui.screens.user_create.UserCreateComponent
import utils.UserUtils
import kotlin.io.path.Path
import kotlin.io.path.notExists

class RootComponent(
    private val di: DI,
    componentContext: ComponentContext,
) : IRootComponent, BaseComponent(componentContext) {

    private val userID = UserUtils.getUserID()

    private val navHostNav = StackNavigation<RootDestination>()

    override val rootStack: Value<ChildStack<*, IRootComponent.Screen>> = childStack(
        source = navHostNav,
        initialConfiguration = RootDestination.None,
//            handleBackButton = true,
        childFactory = ::createChild,
        key = "root host stack"
    )

    private val appSettingsRepo: ISettingsRepository by di.instance(tag = "settings.local")

    private val dbSettingsRepoFactory: (DatabaseArguments) -> ISettingsRepository by di.factory(tag = "settings.db")
    private val userRepoFactory: (DatabaseArguments) -> IRepositoryObservable<User> by di.factory()

    private val _currentDBPath: MutableValue<String> = MutableValue("")
    override val currentDBPath: Value<String> = _currentDBPath

    override fun loadDatabase() {
        navHostNav.replaceCurrent(RootDestination.DBSelector)
    }

    private suspend fun invalidateCurrentDBPath() {
        //1. load dbpath from settings:
        val dbPathSetting = appSettingsRepo.getSetting(Settings.DB.SETTING_ID_DB_PATH)
            ?: Settings.DB.DEFAULT_DB_PATH
        // loaded path - non-empty string, now check its validity:
        onDBSet(dbPathSetting.stringValue)
    }
    private fun onDBSet(dbPath: String) {
        // 1.check path existence
        val path = Path(dbPath)
        _currentDBPath.value = dbPath
        if (path.notExists()) {
            //database is not exist - show DBSelector screen - to create or choose another one
            navHostNav.replaceCurrent(RootDestination.DBSelector)
        } else {
            //database exists - check user:
            val userRepo = userRepoFactory(DatabaseArguments(path = dbPath))
            scope.launch {
                val user = userRepo.getByIDBlocking(userID).item
                withContext(Dispatchers.Main) {
                    if (user == null) {
                        //user doesn't exist in given database - go create it
                        navHostNav.replaceCurrent(RootDestination.UserCreate(dbPath = dbPath))
                    } else {
                        //user exists - go to logged screen
                        navHostNav.replaceCurrent(RootDestination.LoggedIn(dbPath = dbPath, user = user))
                    }
                }
            }
        }
    }

    @Parcelize
    sealed class RootDestination : Parcelable {

        @Parcelize
        object None : RootDestination()

        @Parcelize
        object DBSelector : RootDestination()

        @Parcelize
        class UserCreate(val dbPath: String) : RootDestination()

        @Parcelize
        class LoggedIn(val dbPath: String, val user: User) : RootDestination()

    }

    private fun createChild(config: RootDestination, componentContext: ComponentContext): IRootComponent.Screen {
        return when (config) {
            RootDestination.DBSelector -> IRootComponent.Screen.DBSelector(
                component = DBSelectorComponent(
                    di = di,
                    componentContext = componentContext,
                    onPathSelected = ::onDBSet
                )
            )

            is RootDestination.UserCreate -> IRootComponent.Screen.UserCreate(
                component = UserCreateComponent(
                    di = di,
                    componentContext = componentContext,
                    dbPath = config.dbPath,
                    onUserCreated = {
                        //user created in given database path -> show logged in screen
                        navHostNav.replaceCurrent(RootDestination.LoggedIn(dbPath = config.dbPath, user = it))
                    })
            )

            is RootDestination.LoggedIn -> IRootComponent.Screen.LoggedIn(
                component = LoggedInRootComponent(
                    di = di,
                    componentContext = componentContext,
                    dbPath = config.dbPath,
                    loggedUser = config.user
                )
            )

            RootDestination.None -> IRootComponent.Screen.NONE
        }
    }

    init {
        //decide here what screen to show:
        scope.launch {
            invalidateCurrentDBPath()
        }
    }
}