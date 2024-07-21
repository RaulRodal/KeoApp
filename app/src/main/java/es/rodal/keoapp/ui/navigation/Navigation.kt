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
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.rodal.keoapp.ui.screens.calendar.RecordatorioCalendarScreen
import es.rodal.keoapp.ui.screens.entry.RecordatorioEntryScreen
import es.rodal.keoapp.ui.screens.home.RecordatorioScreen
import es.rodal.keoapp.ui.utils.scheduleAlarmPermissionGranted


//const val ASK_NOTIFICATION_PERMISSION = "notification_permission"
//const val ASK_ALARM_PERMISSION = "alarm_permission"

@Composable
fun MainNavigation(
    context: Context
) {
    val navController = rememberNavController()
//  val askNotificationPermission = navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>(ASK_NOTIFICATION_PERMISSION) ?: false
//  val askAlarmPermission = navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>(ASK_ALARM_PERMISSION) ?: false
    val askPermission = scheduleAlarmPermissionGranted(context)
    NavHost(
        navController = navController,
        startDestination = NavigationDestinations.RecordatorioHomeScreen.route
    ) {
        //HOME
        composable(route = NavigationDestinations.RecordatorioHomeScreen.route) {
            RecordatorioScreen(
                navController = navController,
                askNotificationPermission = askPermission,
                askAlarmPermission = askPermission,
                navigateToRecordatorioEntry = { navController.navigate(NavigationDestinations.RecordatorioEntryScreen.route) }
            )
        }

        //ENTRY
        composable(route = NavigationDestinations.RecordatorioEntryScreen.route) {
            RecordatorioEntryScreen(
                navController = navController
            )
        }

        //CALENDAR
        composable(route = NavigationDestinations.RecordatorioCalendarScreen.route) {
            RecordatorioCalendarScreen(
                navController = navController
            )
        }

    }
}
