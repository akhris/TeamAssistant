package di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import persistence.realm.RealmInit

val di = DI {
    bindSingleton { RealmInit.createRealm(instance(tag = "settings.local")) }
    import(usersModule)
    import(teamsModule)
    import(projectsModule)
    import(tasksModule)
    import(settingsModule)
}

