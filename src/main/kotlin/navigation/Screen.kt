package navigation


import strings.Strings

/**
 * Class for navigation representing single destination Screen that can be hosted in NavHost.
 * Each Screen is characterized by route String.
 */
sealed class Screen(val route: String) {

    object Warehouse : Screen("screen_components")

    object Types : Screen("screen_types")

    object Income : Screen("screen_income")

    object Outcome : Screen("screen_outcome")

    object Projects : Screen("screen_projects")

    object Settings : Screen("screen_settings")

    object Empty : Screen("")
    companion object {
        val homeScreen = Screen.Warehouse
    }
}

/**
 * Class representing navigation items for using in navigation component (such as Navigation Rail).
 * Each Item has title, icon and route String.
 */
sealed class NavItem(val pathToIcon: String, val title: Strings, val route: String) {

    object Warehouse :
        NavItem(
            pathToIcon = "vector/warehouse_black_24dp.svg",
            title = Strings.navitem_warehouse_title,
            Screen.Warehouse.route
        )

    object DataTypes : NavItem(
        pathToIcon = "vector/category_black_24dp.svg",
        title = Strings.navitem_datatypes_title,
        Screen.Types.route
    )

    object Income :
        NavItem(
            pathToIcon = "vector/drive_file_move_black_24dp.svg",
            title = Strings.navitem_income_title,
            Screen.Income.route
        )

    object Outcome :
        NavItem(
            pathToIcon = "vector/drive_file_move_rtl_black_24dp.svg",
            title = Strings.navitem_outcome_title,
            Screen.Outcome.route
        )

    object Projects : NavItem(
        pathToIcon = "vector/list_alt_black_24dp.svg",
        title = Strings.navitem_projects_title,
        Screen.Projects.route
    )

    object Settings : NavItem(
        pathToIcon = "vector/settings_black_24dp.svg",
        title = Strings.navitem_settings_title,
        Screen.Settings.route
    )

    companion object {
        fun getItems() = listOf<NavItem>(Warehouse, Income, Outcome, DataTypes, Settings)
        fun getDefaultHome() = DataTypes
    }
}