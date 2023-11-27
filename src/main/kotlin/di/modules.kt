package di

import domain.*
import domain.application.baseUseCases.GetEntities
import domain.application.baseUseCases.GetEntity
import domain.application.baseUseCases.InsertEntity
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.realm.dao.TeamsDao
import persistence.realm.dao.UsersDao
import persistence.realm.repository.RealmTeamsRepository
import persistence.repositories.Repository

val usersModule = DI.Module("users module") {
    //User persistence bindings:
    bindSingleton<IDao<User>> { UsersDao(instance()) }
    bindSingleton<Repository<User>> { Repository(instance()) }
    bindSingleton<IRepository<User>> { instance<Repository<User>>() }
    bindSingleton<IRepositoryCallback<User>> { instance<Repository<User>>() }
    bindSingleton<GetEntity<User>> { GetEntity(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton<InsertEntity<User>> { InsertEntity(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton<GetEntities<User>> { GetEntities(instance(), ioDispatcher = Dispatchers.IO) }

}

val teamsModule = DI.Module("teams module"){
    bindSingleton<IDao<Team>> { TeamsDao(instance()) }
    bindSingleton<Repository<Team>> { Repository(instance()) }
    bindSingleton<IRepository<Team>> { instance<Repository<Team>>() }
    bindSingleton<IRepositoryCallback<Team>> { instance<Repository<Team>>() }
    bindSingleton<GetEntity<Team>> { GetEntity(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton<InsertEntity<Team>> { InsertEntity(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton<GetEntities<Team>> { GetEntities(instance(), ioDispatcher = Dispatchers.IO) }
    bindSingleton<IRepositoryObservable<Team> > { RealmTeamsRepository(instance()) }

}