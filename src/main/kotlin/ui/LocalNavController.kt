import androidx.compose.runtime.compositionLocalOf
import domain.User
import ui.screens.root_ui.INavController

/**
 * Provider of NavController
 *
 * Every UI in root scaffold's content can access current NavController by calling [LocalNavController.current]
 */
val LocalNavController = compositionLocalOf<INavController?> { null }