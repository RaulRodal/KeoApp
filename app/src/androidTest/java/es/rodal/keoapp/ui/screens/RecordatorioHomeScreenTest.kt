package es.rodal.keoapp.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import es.rodal.keoapp.R
import es.rodal.keoapp.ui.screens.home.RecordatorioHomeScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RecordatorioHomeScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)


    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

//    @Inject
//    lateinit var navController: NavController

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.setContent {
            RecordatorioHomeScreen(
                navController = rememberNavController(),
                askNotificationPermission = false,
                askAlarmPermission = false
            )
        }
    }

    @Test
    fun recordatorioHomeScreen_displaysCorrectData() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.recordatorio_home_screen_subtitle))
            .assertExists()
    }

    @Test
    fun recordatorioHomeScreen_navigateToCalendar() {
        var navigateCalled = false
        composeTestRule.setContent {
            RecordatorioHomeScreen(
                navController = rememberNavController(),
                askNotificationPermission = false,
                askAlarmPermission = false
            )
        }
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.recordatorio_show_calendar))
            .performClick()
        assert(navigateCalled)
    }

    @Test
    fun recordatorioHomeScreen_navigateToHistory() {
        var navigateCalled = false
        composeTestRule.setContent {
            RecordatorioHomeScreen(
                navController = rememberNavController(),
                askNotificationPermission = false,
                askAlarmPermission = false
            )
        }
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.recordatorio_show_reminders))
            .performClick()
        assert(navigateCalled)
    }

    @Test
    fun recordatorioHomeScreen_navigateToAddReminder() {
        var navigateCalled = false
        composeTestRule.setContent {
            RecordatorioHomeScreen(
                navController = rememberNavController(),
                askNotificationPermission = false,
                askAlarmPermission = false
            )
        }
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.recordatorio_add_reminder))
            .performClick()
        assert(navigateCalled)
    }
}