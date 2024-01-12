package persistence.realm

import domain.IRepositoryObservable
import domain.Setting
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.flow.first
import org.kodein.di.DI
import org.kodein.di.instance

object RealmInit {

    fun createRealm(): Realm {
        val config = RealmConfiguration.create(
            schema = setOf(
                RealmTask::class,
                RealmProject::class,
                RealmUser::class,
                RealmTeam::class,
                RealmTask::class,
                RealmSubTask::class,
                RealmAttachment::class,
                RealmTaskMessage::class,
                RealmSetting::class
            )
        )

        return Realm.open(config)
    }


    /**
     * Checks that database settings table contains creator information
     * @return true if current user is creator, false - otherwise
     */
    suspend fun checkDatabaseCreator(userID: String, di: DI): Boolean {
        val repo: IRepositoryObservable<Setting> by di.instance()
        val dbCreator = repo.getByID(Setting.SETTING_ID_DB_CREATOR).first()
        when (dbCreator.item) {
            is Setting.StringSetting -> {
                return userID == dbCreator.item.value
            }

            else -> {
                //setting not found - db was just created
                repo.insert(
                    Setting.StringSetting(
                        id = Setting.SETTING_ID_DB_CREATOR,
                        name = "database creator",
                        description = "id of user that created database file",
                        value = userID
                    )
                )
                return true
            }
        }
    }
}