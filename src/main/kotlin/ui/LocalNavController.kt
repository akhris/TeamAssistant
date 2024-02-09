import androidx.compose.runtime.compositionLocalOf
import ui.screens.logged_in_root.INavController

/**
 * Provider of NavController
 *
 * Every UI in root scaffold's content can access current NavController by calling [LocalNavController.current]
 */
val LocalNavController = compositionLocalOf<INavController?> { null }