package es.rodal.keoapp.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.ui.navigation.NavigationDestinations
import es.rodal.keoapp.ui.utils.KeoBottomAppBar
import es.rodal.keoapp.ui.utils.KeoTopAppBar
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioCalendar2Screen(
    navController: NavController,
    viewModel: RecordatorioCalendarViewModel = hiltViewModel()
) {

    val currentDay = remember { mutableStateOf(Date()) }
    val reminders = remember { mutableStateOf(viewModel.recordatoriosFilteredState) }

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
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            RecordatorioCalendar(
                reminders = reminders.value,
                onDaySelected = { day ->
                    currentDay.value = day
                    viewModel.filterRecordatorios(day = day)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReminderList(reminders.value)
        }
    }
}


@Composable
fun RecordatorioCalendar(
    reminders: List<Recordatorio>,
    onDaySelected: (Date) -> Unit
) {
    val currentMonth = remember { mutableStateOf(Calendar.getInstance()) }
    val daysInMonth = getDaysInMonth(currentMonth.value)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        CalendarHeader(currentMonth.value)
        Spacer(modifier = Modifier.height(8.dp))
        CalendarDaysHeader()
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(daysInMonth.chunked(7)) { week ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    week.forEach { day ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (reminders.isNotEmpty()) Color(0xFFFFEB3B) else Color.Transparent,
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(4.dp)
                                .clickable {
                                    if (day != null) {
                                        onDaySelected(day)
                                    }
                                }
                        ) {
                            if (day != null) {
                                Text(
                                    text = day.date.toString(),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun CalendarHeader(calendar: Calendar) {
    val dateFormat = remember { SimpleDateFormat("MMMM yyyy", Locale.getDefault()) }
    Text(
        text = dateFormat.format(calendar.time),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun CalendarDaysHeader() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
            Text(
                text = day,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

fun getDaysInMonth(calendar: Calendar): List<Date?> {
    val daysInMonth = mutableListOf<Date?>()
    val tempCalendar = calendar.clone() as Calendar
    tempCalendar.set(Calendar.DAY_OF_MONTH, 1)
    val firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK) - 1

    for (i in 0 until firstDayOfWeek) {
        daysInMonth.add(null)
    }

    val maxDay = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    for (day in 1..maxDay) {
        tempCalendar.set(Calendar.DAY_OF_MONTH, day)
        daysInMonth.add(tempCalendar.time)
    }

    return daysInMonth
}