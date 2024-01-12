package di

import domain.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.realm.repository.*

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

val settingsModule = DI.Module("settings module") {
    bindSingleton<IRepositoryObservable<Setting>> { RealmSettingsRepository(instance()) }
}

