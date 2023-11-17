package settings

import java.nio.file.Path
import kotlin.io.path.*

object AppFoldersManager {
    private val currentUserPath = System.getProperty("user.home")       //"/home/user"
    private val appSubPath = ".team_assistant"
    private val appPath = Path(currentUserPath, appSubPath)      //"/home/user/.team_assistant"

    fun getAppPath(): Path {
        if (appPath.notExists())
            appPath.createDirectories()

        return appPath
    }

}