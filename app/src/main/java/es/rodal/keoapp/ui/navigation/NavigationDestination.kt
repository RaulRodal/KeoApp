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

    object RecordatorioHomeDestination : NavigationDestination {
        override val route = "recordatorio_home"
        override val titleRes = R.string.recordatorio_home_screen_title
    }

    object RecordatorioEntryDestination : NavigationDestination {
        override val route = "recordatorio_entry"
        override val titleRes = R.string.recordatorio_entry_screen_title
        const val recordatorioIdArg = "recordatorioId"
        val routeWithArgs = "$route/{$recordatorioIdArg}"
    }

    object RecordatorioHistoryDestination : NavigationDestination {
        override val route = "recordatorio_history"
        override val titleRes = R.string.recordatorio_history_screen_title
    }

    object RecordatorioCalendarDestination : NavigationDestination {
        override val route = "recordatorio_calendar"
        override val titleRes = R.string.recordatorio_calendar_screen_title
    }

    object RecordatorioDetailDestination : NavigationDestination {
        override val route = "recordatorio_detail"
        override val titleRes = R.string.recordatorio_detail_screen_title
        const val recordatorioIdArg = "recordatorioId"
        val routeWithArgs = "$route/{$recordatorioIdArg}"
    }

    object RecordatorioHelpDestination : NavigationDestination {
        override val route = "recordatorio_help"
        override val titleRes = R.string.help
    }
}
