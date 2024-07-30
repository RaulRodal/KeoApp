package es.rodal.keoapp.ui.screens.calendar

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.rodal.keoapp.R
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.domain.model.getFormattedTime
import es.rodal.keoapp.ui.navigation.NavigationDestinations
import es.rodal.keoapp.ui.utils.KeoBottomAppBar
import es.rodal.keoapp.ui.utils.KeoTopAppBar
import java.util.Calendar
import java.util.Date
import android.widget.CalendarView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.res.dimensionResource
import androidx.core.content.ContextCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioCalendarScreen(
    navController: NavController,
    viewModel: RecordatorioCalendarViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            KeoTopAppBar(
                title = stringResource(id = NavigationDestinations.RecordatorioCalendarDestination.titleRes),
                canNavigateBack = true,
                navigateBack = navController::navigateUp
            )
        },
        bottomBar = {
            KeoBottomAppBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_medium)),
        ) {
            CalendarView(
                onDayChange = { day ->
                    viewModel.filterRecordatorios(day = day)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            when (viewModel.recordatoriosFilteredState.isEmpty()){
                true -> Text(
                    text = stringResource(id = R.string.recordatorio_day_empty_card_message),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.padding_large))
                )
                false -> ReminderList(viewModel.recordatoriosFilteredState)
            }
        }
    }
}

@Composable
fun CalendarView(onDayChange: (Date) -> Unit) {
    val context = LocalContext.current

    AndroidView(
        factory = { CalendarView(context).apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.white))

            setOnDateChangeListener { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                onDayChange(calendar.time)
            }
        }},
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}

@Composable
fun ReminderList(recordatorios: List<Recordatorio>) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        items(recordatorios.size) { index ->
                RecordatorioItem(recordatorio = recordatorios[index])
        }
    }
}

@Composable
fun RecordatorioItem(recordatorio: Recordatorio) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_large))
    ) {
        Text(text = recordatorio.name)
        Spacer(modifier = Modifier.weight(0.5f))
        Text(text = recordatorio.recordatorioTime.getFormattedTime())
    }
}


