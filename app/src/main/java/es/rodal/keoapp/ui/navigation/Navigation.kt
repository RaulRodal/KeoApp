/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.rodal.keoapp.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import es.rodal.keoapp.ui.screens.calendar.RecordatorioCalendarScreen
import es.rodal.keoapp.ui.screens.detail.RecordatorioDetailScreen
import es.rodal.keoapp.ui.screens.entry.RecordatorioEntryScreen
import es.rodal.keoapp.ui.screens.help.RecordatorioHelpScreen
import es.rodal.keoapp.ui.screens.history.RecordatorioHistoryScreen
import es.rodal.keoapp.ui.screens.home.RecordatorioHomeScreen
import es.rodal.keoapp.ui.utils.scheduleAlarmPermissionGranted


@Composable
fun MainNavigation(
    context: Context
) {
    val navController = rememberNavController()
    val askPermission = scheduleAlarmPermissionGranted(context)
    NavHost(
        navController = navController,
        startDestination = NavigationDestinations.RecordatorioHomeDestination.route
    ) {
        //HOME
        composable(route = NavigationDestinations.RecordatorioHomeDestination.route) {
            RecordatorioHomeScreen(
                navController = navController,
                askNotificationPermission = askPermission,
                askAlarmPermission = askPermission
            )
        }

        //HISTORY
        composable(route = NavigationDestinations.RecordatorioHistoryDestination.route) {
            RecordatorioHistoryScreen(
                navController = navController,
                navigateToRecordatorioEntry = { navController.navigate("${NavigationDestinations.RecordatorioEntryDestination.route}/0") },
                navigateToRecordatorioDetail = { navController.navigate("${NavigationDestinations.RecordatorioDetailDestination.route}/$it") }
            )
        }

        //ENTRY
        composable(
            route = NavigationDestinations.RecordatorioEntryDestination.routeWithArgs,
            arguments = listOf(navArgument(NavigationDestinations.RecordatorioEntryDestination.recordatorioIdArg) {//argumentos que recibe de la ruta
                type = NavType.IntType
            })
        ) {
            RecordatorioEntryScreen(
                navController = navController
            )
        }

        //CALENDAR
        composable(route = NavigationDestinations.RecordatorioCalendarDestination.route) {
            RecordatorioCalendarScreen(
                navController = navController
            )
        }

        //DETAIL
        composable(
                route = NavigationDestinations.RecordatorioDetailDestination.routeWithArgs,
                arguments = listOf(navArgument(NavigationDestinations.RecordatorioDetailDestination.recordatorioIdArg) {//argumentos que recibe de la ruta
                type = NavType.IntType
            })
        ) {
            RecordatorioDetailScreen(
                navController = navController,
                navigateToEditRecordatorio = { navController.navigate("${NavigationDestinations.RecordatorioEntryDestination.route}/$it") }
            )
        }

        //HELP
        composable(route = NavigationDestinations.RecordatorioHelpDestination.route) {
            RecordatorioHelpScreen(
                navController = navController
            )
        }

    }
}
