package es.rodal.keoapp.ui.navigation

import es.rodal.keoapp.R

/**
 * Interface to describe the navigation destinations for the app
 */
interface NavigationDestination {
    /**
     * Unique name to define the path for a composable
     */
    val route: String

    /**
     * String resource id to that contains title to be displayed for the screen.
     */
    val titleRes: Int
}

object NavigationDestinations {


    object RecordatorioHomeScreen : NavigationDestination {
        override val route = "recordatorio_home"
        override val titleRes = R.string.recordatorio_home_screen_title
    }

    object RecordatorioEntryScreen : NavigationDestination {
        override val route = "recordatorio_entry"
        override val titleRes = R.string.recordatorio_entry_screen_title
    }

    object RecordatorioHistoryScreen : NavigationDestination {
        override val route = "recordatorio_history"
        override val titleRes = R.string.recordatorio_history_screen_title
    }
}