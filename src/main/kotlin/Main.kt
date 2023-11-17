import androidx.compose.runtime.*
import androidx.compose.ui.window.*
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.di
import ui.screens.root_ui.IRootComponent
import ui.screens.root_ui.RootComponent
import ui.screens.root_ui.RootUi
import ui.theme.AppTheme
import utils.UserUtils
import utils.log

@Composable
fun FrameWindowScope.App(rootComponent: IRootComponent, windowState: WindowState) {

    var isDark by remember { mutableStateOf(false) }
    AppTheme(darkTheme = isDark) {
//        VerticalReorderList()
        RootUi(
            component = rootComponent,
            isDarkTheme = isDark,
            onThemeChanged = { isDark = it },
            windowState = windowState
        )
    }
}


fun main() {
    // Create the root component before starting Compose
    val lifecycle = LifecycleRegistry()
    val root = RootComponent(componentContext = DefaultComponentContext(lifecycle), di = di)
    val userName = UserUtils.getUserName()
    log(userName, "userName: ")
    application {
        val windowState = rememberWindowState()
        Window(
            state = windowState,
            title = "TeamAssistant",
            onCloseRequest = ::exitApplication,
            undecorated = true
        ) {
            App(root, windowState)
        }
    }
}
