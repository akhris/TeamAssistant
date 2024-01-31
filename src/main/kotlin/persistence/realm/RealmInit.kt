package persistence.realm

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import settings.AppFoldersManager
import settings.Settings
import utils.UserUtils
import utils.log
import kotlin.io.path.pathString

object RealmInit {

    fun createRealm(): Realm {
        val conf = RealmConfiguration.Builder(
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
            .name(AppFoldersManager.defaultDBFileName)
            .directory(AppFoldersManager.getAppPath().pathString)
            .initialData {
                copyToRealm(RealmSetting().apply {
                    _id = Settings.DB.SETTING_ID_DB_CREATOR
                    value = UserUtils.getUserID()
                })
            }
            .build()

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
        log("directory: ${AppFoldersManager.getAppPath().pathString} file: ${AppFoldersManager.defaultDBFileName}", "creating realm: ")
        return Realm.open(config)
    }


}