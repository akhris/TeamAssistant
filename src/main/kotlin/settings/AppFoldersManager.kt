package settings

import java.nio.file.Path
import kotlin.io.path.*

object AppFoldersManager {
    // TODO: use it for user login:
    //private val currentUserName = System.getProperty("user.name")       //"domain/user"
    private val currentUserPath = System.getProperty("user.home")       //"/home/user"
    private val appSubPath = ".team_assistant"
    private val appPath = Path(currentUserPath, appSubPath)      //"/home/user/.team_assistant"

    fun getAppPath(): Path {
        if (appPath.notExists())
            appPath.createDirectories()

        return appPath
    }

}