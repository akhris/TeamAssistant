package persistence.realm

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object RealmInit {

    fun createRealm(): Realm {
        val config = RealmConfiguration.create(
            schema = setOf(
                RealmTask::class,
                RealmProject::class,
                RealmUser::class,
                RealmTeam::class,
                RealmTask::class,
                RealmSubTask::class
            )
        )
        return Realm.open(config)
    }

}