package di

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import domain.*
import domain.application.SettingsUseCase
import domain.settings.ISettingDescriptor
import domain.settings.ISettingMapper
import domain.settings.SettingMapper
import org.kodein.di.DI
import org.kodein.di.bindMultiton
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.multiplatform_settings.MultiplatformSettingsRepository
import persistence.realm.repository.*
import settings.DatabaseArguments
import settings.SettingDescriptor
import utils.log
import java.util.prefs.Preferences

val usersModule = DI.Module("users module") {
    bindMultiton<DatabaseArguments, IRepositoryObservable<User>> { RealmUsersRepository(instance(arg = it)) }
}

val teamsModule = DI.Module("teams module") {
    bindMultiton<DatabaseArguments, IRepositoryObservable<Team>> { RealmTeamsRepository(instance(arg = it)) }
}

val projectsModule = DI.Module("projects module") {
    bindMultiton<DatabaseArguments, IRepositoryObservable<Project>> { RealmProjectsRepository(instance(arg = it)) }
}

val tasksModule = DI.Module("tasks module") {
    bindMultiton<DatabaseArguments, IRepositoryObservable<Task>> { RealmTasksRepository(instance(arg = it)) }
}

@OptIn(ExperimentalSettingsApi::class)
val settingsModule = DI.Module("settings module") {
    bindSingleton<ISettingMapper> { SettingMapper }

    //database settings:
    bindMultiton<DatabaseArguments, ISettingsRepository>(tag = "settings.db") { RealmSettingsRepository(instance(arg = it)) }

    //key-value settings:
    bindSingleton<Preferences> {
        val prefs = Preferences.userRoot()
        log(prefs.toString(), "local preferences:")
        prefs
    }
    bindSingleton<Settings> { PreferencesSettings(instance()) }
    bindSingleton<SuspendSettings> { instance<Settings>().toSuspendSettings() }

    bindSingleton<ISettingsRepository>(tag = "settings.local") {
        MultiplatformSettingsRepository(
            instance(),
            instance()
        )
    }
    bindSingleton<ISettingDescriptor> { SettingDescriptor() }
    bindMultiton<DatabaseArguments, SettingsUseCase> {
        SettingsUseCase(
            dbSettingsRepo = instance(tag = "settings.DB", arg = it),
            appSettingsRepo = instance(tag = "settings.local")
        )
    }
}

