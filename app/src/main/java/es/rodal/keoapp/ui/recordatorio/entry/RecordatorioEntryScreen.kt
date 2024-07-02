package es.rodal.keoapp.ui.recordatorio.entry

import android.Manifest
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.rodal.keoapp.RecordatorioNotificationReceiver
import es.rodal.keoapp.RecordatorioNotificationReceiver.Companion.NOTIFICATION_ID
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.ui.recordatorio.home.RecordatorioViewModel
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioEntryScreen (
    navController: NavController,
    activity: ComponentActivity,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: RecordatorioEntryViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                title = {
                    Text("Top app bar")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            formGpt(
                viewModel = viewModel,
                navController = navController,
                navigateBack = navigateBack,
                activity = activity
            )
        }
    }
}

@Composable
fun formGpt(
    viewModel: RecordatorioEntryViewModel,
    navController: NavController,
    navigateBack: () -> Unit,
    activity: ComponentActivity
) {
    val context = navController.context
    var taskName by remember { mutableStateOf("") }
    var taskDate by remember { mutableStateOf("") }
    var taskTime by remember { mutableStateOf("") }
    var calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            taskDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            taskTime = "$hourOfDay:$minute"
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = taskName,
            onValueChange = { taskName = it },
            label = { Text("Nombre de la Tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { datePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (taskDate.isEmpty()) "Seleccionar Fecha" else taskDate)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { timePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (taskTime.isEmpty()) "Seleccionar Hora" else taskTime)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.addRecordatorio(context, Recordatorio(
                    name = taskName,
                    recordatorioDone = false,
                    recordatorioTime = calendar.time
                ))
                navigateBack()
            },
//                if (taskName.isNotEmpty() && taskDate.isNotEmpty() && taskTime.isNotEmpty()) {
//                    scheduleNotification(
//                        context = context,
//                        activity = activity,
//                        calendarTimeInMillis = calendar.timeInMillis
//                    )
//                    Toast.makeText(context, "Recordatorio guardado", Toast.LENGTH_SHORT).show()
//                    navigateBack()
//                } else {
//                    Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
//
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Guardar Tarea y Establecer Recordatorio")
        }
    }
}

fun scheduleNotification(
    context: Context,
    activity: ComponentActivity,
    calendarTimeInMillis: Long = Calendar.getInstance().timeInMillis
) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
        // Request SCHEDULE_EXACT_ALARM permission
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM),
            1
        )
    } else {
        val intent = Intent(context, RecordatorioNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Trigger at millis would be the date and time in millis
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarTimeInMillis, pendingIntent)
    }
}