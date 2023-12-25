import androidx.compose.runtime.compositionLocalOf
import domain.User

/**
 * Provider of User
 * if null - no user is logged in -> make UI show message to create one.
 *
 * It changes when a value in types selector is changed.
 *
 * Every UI in root scaffold's content can access current User by calling [LocalCurrentUser.current]
 */
val LocalCurrentUser = compositionLocalOf<User?> { null }