package di

import domain.IRepositoryObservable
import domain.Project
import domain.Team
import domain.User
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.realm.repository.RealmProjectsRepository
import persistence.realm.repository.RealmTeamsRepository
import persistence.realm.repository.RealmUsersRepository

val usersModule = DI.Module("users module") {
    bindSingleton<IRepositoryObservable<User> > { RealmUsersRepository(instance()) }
}

val teamsModule = DI.Module("teams module"){
    bindSingleton<IRepositoryObservable<Team> > { RealmTeamsRepository(instance()) }
}

val projectsModule = DI.Module("projects module"){
    bindSingleton<IRepositoryObservable<Project> > { RealmProjectsRepository(instance()) }
}