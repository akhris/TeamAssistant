package di

import io.realm.kotlin.Realm
import org.kodein.di.*
import persistence.realm.RealmInit
import settings.DatabaseArguments

val di = DI {
    bindMultiton<DatabaseArguments, Realm> { dbArguments -> RealmInit.createRealm(dbArguments.path) }
    import(usersModule)
    import(teamsModule)
    import(projectsModule)
    import(tasksModule)
    import(settingsModule)

}

