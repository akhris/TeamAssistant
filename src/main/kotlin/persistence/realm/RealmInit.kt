package persistence.realm

import domain.ISettingsRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.runBlocking
import settings.Settings
import utils.UserUtils
import utils.log
import kotlin.io.path.Path

object RealmInit {

    fun createRealm(dbPath: String): Realm {
//        val realmPathSetting = runBlocking {
//            appSettingsRepo.getSetting(Settings.DB.SETTING_ID_DB_PATH)
//        } ?: Settings.DB.defaults.find { it.id == Settings.DB.SETTING_ID_DB_PATH }
//        ?: throw IllegalStateException("setting with id: ${Settings.DB.SETTING_ID_DB_PATH} was not found in defaults")

        val realmPath = Path(dbPath).toFile()


        val config = RealmConfiguration.Builder(
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
            .name(realmPath.name)
            .directory(realmPath.parent)
            .initialData {
                copyToRealm(RealmSetting().apply {
                    _id = Settings.DB.SETTING_ID_DB_CREATOR
                    value = UserUtils.getUserID()
                })
            }
            .build()


        log(
            "directory: ${realmPath.parent} file: ${realmPath.name}",
            "creating realm: "
        )

        return Realm.open(config)
    }


}