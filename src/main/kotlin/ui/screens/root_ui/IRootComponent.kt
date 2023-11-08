package ui.screens.root_ui

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ui.NavItem

interface IRootComponent {

    val navHostStack: Value<ChildStack<*, NavHost>>
    val dialogStack: Value<ChildStack<*, Dialog>>
//    val toolbarUtilsStack: Value<ChildStack<*, ToolbarUtils>>

    val currentDestination: Value<NavItem>

    fun navigateTo(navItem: NavItem)

    sealed class NavHost {
        class UserDetails : NavHost()
        class Tasks : NavHost()
        class Activity : NavHost()
        class Projects : NavHost()
        class Team : NavHost()
           }

    sealed class Dialog {
               object None : Dialog()
    }

    sealed class ToolbarUtils {
//        class SampleTypesSelector(val component: ISampleTypesSelector) : ToolbarUtils()
    }

    fun dismissDialog()
}