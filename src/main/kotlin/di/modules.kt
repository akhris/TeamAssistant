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
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.multiplatform_settings.MultiplatformSettingsRepository
import persistence.realm.repository.*
import settings.SettingDescriptor
import java.util.prefs.Preferences

val usersModule = DI.Module("users module") {
    bindSingleton<IRepositoryObservable<User>> { RealmUsersRepository(instance()) }
}

val teamsModule = DI.Module("teams module") {
    bindSingleton<IRepositoryObservable<Team>> { RealmTeamsRepository(instance()) }
}

val projectsModule = DI.Module("projects module") {
    bindSingleton<IRepositoryObservable<Project>> { RealmProjectsRepository(instance()) }
}

val tasksModule = DI.Module("tasks module") {
    bindSingleton<IRepositoryObservable<Task>> { RealmTasksRepository(instance()) }
}

@OptIn(ExperimentalSettingsApi::class)
val settingsModule = DI.Module("settings module") {
    //database settings:
    bindSingleton<ISettingsRepository>(tag = "settings.db") { RealmSettingsRepository(instance()) }

    //key-value settings:
    bindSingleton<Preferences> { Preferences.userRoot() }
    bindSingleton<Settings> { PreferencesSettings(instance()) }
    bindSingleton<SuspendSettings> { instance<Settings>().toSuspendSettings() }
    bindSingleton<ISettingsRepository>(tag = "settings.DB") { RealmSettingsRepository(instance()) }
    bindSingleton<ISettingsRepository>(tag = "settings.local") { MultiplatformSettingsRepository(instance()) }
    bindSingleton<ISettingDescriptor> { SettingDescriptor() }
    bindSingleton<SettingsUseCase> {
        SettingsUseCase(
            dbSettingsRepo = instance(tag = "settings.DB"),
            appSettingsRepo = instance(tag = "settings.local")
        )
    }
}

