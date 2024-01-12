package di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import persistence.realm.RealmInit

val di = DI {
    bindSingleton { RealmInit.createRealm() }
    import(usersModule)
    import(teamsModule)
    import(projectsModule)
    import(tasksModule)
    import(settingsModule)
}

