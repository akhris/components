package settings

import java.nio.file.Path
import kotlin.io.path.*

object AppFoldersManager {
    private val currentUserPath = System.getProperty("user.home")       //"/home/user"
    private val componentsSupPath = ".components_app"
    private val appPath = Path(currentUserPath, componentsSupPath)      //"/home/user/.components_app"

    fun getAppPath(): Path {
        if(appPath.notExists())
            appPath.createDirectories()

        return appPath
    }

}