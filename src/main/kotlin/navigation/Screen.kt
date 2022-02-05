package navigation

/**
 * Class for navigation representing single Screen in main host placeholder
 */
sealed class Screen(val route: String) {
    /**
     * Database screen.
     * Managing database files: select file or create new.
     * Showing selected file's properties: size, items count, e.t.c.
     * Export/import database.
     */
    object Database : Screen("screen_database")

    /**
     * Current state of the storage
     */
    object CurrentState : Screen("screen_components")

    object Income : Screen("screen_income")
    object Outcome : Screen("screen_income")
}

sealed class NavItem(val pathToIcon: String, val title: String, val route: String) {
    object Database : NavItem(pathToIcon = "vector/source_black_24dp.svg", title = "data source", Screen.Database.route)
    object CurrentState :
        NavItem(pathToIcon = "vector/warehouse_black_24dp.svg", title = "warehouse", Screen.Database.route)

    object Types : NavItem(pathToIcon = "vector/category_black_24dp.svg", title = "types", Screen.Database.route)
    object Income :
        NavItem(pathToIcon = "vector/drive_file_move_black_24dp.svg", title = "income", Screen.Database.route)

    object Outcome :
        NavItem(pathToIcon = "vector/drive_file_move_rtl_black_24dp.svg", title = "outcome", Screen.Database.route)

    object Projects : NavItem(pathToIcon = "vector/list_alt_black_24dp.svg", title = "projects", Screen.Database.route)


    companion object {
        val items = listOf<NavItem>(Database, CurrentState, Types, Income, Outcome, Projects)
    }
}