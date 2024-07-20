package es.rodal.keoapp.ui.screens.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.ui.navigation.NavigationDestinations
import es.rodal.keoapp.ui.utils.KeoBottomAppBar
import es.rodal.keoapp.ui.utils.KeoTopAppBar
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioCalendarScreen(navController: NavController) {
    val currentMonth = remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
    val reminders = remember { mutableStateOf(emptyList<Recordatorio>()) }
    val remindersFiltered = remember { mutableStateOf(emptyList<Recordatorio>()) }

    Scaffold(
        topBar = {
            KeoTopAppBar(
                title = stringResource(id = NavigationDestinations.RecordatorioCalendarScreen.titleRes),
                canNavigateBack = true,
                navigateUp = navController::navigateUp
            )
        },
        bottomBar = {
            KeoBottomAppBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CalendarView(
                month = currentMonth.value,
                onMonthChange = { month ->
                    currentMonth.value = month
                    // Actualizar los recordatorios según el mes seleccionado
                    remindersFiltered.value = getRemindersForMonth(reminders.value, month)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReminderList(remindersFiltered.value)
        }
    }
}

@Composable
fun CalendarView(month: Int, onMonthChange: (Int) -> Unit) {
    val context = LocalContext.current

    AndroidView(
        factory = { android.widget.CalendarView(context) },
        update = { view ->
            view.setOnDateChangeListener { _, _, selectedMonth, _ ->
                onMonthChange(selectedMonth)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}

@Composable
fun ReminderList(reminders: List<Recordatorio>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        items(reminders.size) { index ->
            Text(
                text = reminders.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

fun getRemindersForMonth(reminders: List<Recordatorio>, month: Int): List<Recordatorio> {
    return reminders.filter { recordatorio ->
        val calendar = Calendar.getInstance()
        calendar.time = recordatorio.recordatorioTime
        calendar.get(Calendar.MONTH) == month
    }
}