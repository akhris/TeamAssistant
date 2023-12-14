package ui


/**
 * Class representing navigation items for using in NavHost navigation.
 * Each Item has title, icon and route String.
 */
sealed class NavItem(val pathToIcon: String, val title: String) {

    object UserDetails : NavItem(
        pathToIcon = "vector/users/person_black_24dp.svg",
        title = "Пользователь"
    )

    object Tasks : NavItem(
        pathToIcon = "vector/task_black_24dp.svg",
        title = "Задачи"
    )

    object Activity : NavItem(
        pathToIcon = "vector/view_timeline_black_24dp.svg",
        title = "Активность"
    )

    object Projects : NavItem(
        pathToIcon = "vector/view_list_black_24dp.svg",
        title = "Проекты"
    )

    object Team : NavItem(
        pathToIcon = "vector/people_black_24dp.svg",
        title = "Команда"
    )

    companion object {
        fun getMainNavigationItems() =
            listOf<NavItem>(Tasks, Projects, Team, UserDetails)

        val homeItem = Tasks
    }
}
