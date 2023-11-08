import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.di
import ui.screens.root_ui.IRootComponent
import ui.screens.root_ui.RootComponent
import ui.screens.root_ui.RootUi
import ui.theme.AppTheme

@Composable
fun App(rootComponent: IRootComponent) {

    var isDark by remember { mutableStateOf(false) }
    AppTheme(darkTheme = isDark) {
//        VerticalReorderList()
        RootUi(component = rootComponent, isDarkTheme = isDark, onThemeChanged = { isDark = it })
    }
}


fun main() {
    // Create the root component before starting Compose
    val lifecycle = LifecycleRegistry()
    val root = RootComponent(componentContext = DefaultComponentContext(lifecycle), di = di)
    application {
        Window(
            title = "TeamAssistant",
            onCloseRequest = ::exitApplication
        ) {
            App(root)
        }
    }
}
