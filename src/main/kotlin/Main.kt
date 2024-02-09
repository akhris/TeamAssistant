import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.window.*
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.di
import ui.fields.EditableTextField
import ui.screens.root.RootComponent
import ui.screens.root.IRootComponent
import ui.screens.root.RootUi
import ui.theme.AppTheme
import utils.FileUtils
import java.awt.Dimension
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.notExists
import kotlin.io.path.pathString




fun main() {
    // Create the root component before starting Compose
    val lifecycle = LifecycleRegistry()

    val rootComponent = RootComponent(componentContext = DefaultComponentContext(lifecycle), di = di)

    application {

//        LoginScreen(component = rootComponent)

        val windowState = rememberWindowState()
        var isRunning by remember { mutableStateOf(true) }
        if (isRunning) {
            Window(
                state = windowState,
                title = "TeamAssistant",
                onCloseRequest = ::exitApplication,
                undecorated = true
            ) {
                //restrict minimum window size
                window.minimumSize = Dimension(800, 600)
                App(remember { rootComponent }, windowState, onCloseRequest = { isRunning = false })
            }
        }
    }
}

@Composable
fun FrameWindowScope.App(rootComponent: IRootComponent, windowState: WindowState, onCloseRequest: () -> Unit) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    var isDark by remember { mutableStateOf(isSystemInDarkTheme) }
    AppTheme(darkTheme = isDark) {
        RootUi(
            component = rootComponent,
            isDarkTheme = isDark,
            onThemeChanged = { isDark = it },
            windowState = windowState,
            onCloseRequest = onCloseRequest
        )
    }
}
