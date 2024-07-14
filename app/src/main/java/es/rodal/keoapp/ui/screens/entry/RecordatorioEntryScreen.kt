package es.rodal.keoapp.ui.screens.entry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.ui.utils.KeoBottomAppBar
import es.rodal.keoapp.ui.utils.KeoTopAppBar
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioEntryScreen (
    navController: NavController,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: RecordatorioEntryViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            KeoTopAppBar(
                title = "Nuevo Recordatorio",
                canNavigateBack = true,
                navigateUp = onNavigateUp
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
            Form(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun Form(
    viewModel: RecordatorioEntryViewModel,
    navController: NavController
) {
    val context = navController.context
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            date = "$dayOfMonth/${month + 1}/$year"
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
            time = "$hourOfDay:$minute"
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
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre del recordatorio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripcion del recordatorio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { datePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (date.isEmpty()) "Seleccionar fecha" else date)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { timePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (time.isEmpty()) "Seleccionar hora" else time)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Comprobacion campos vacios
                if (name.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {
                    val currentCalendar = Calendar.getInstance()
                    // Comprobacion de que no sea pasado
                    if (calendar.after(currentCalendar)) {
                        viewModel.addRecordatorio(context, Recordatorio(
                            name = name,
                            description = description,
                            recordatorioTime = calendar.time
                        ))
                        Toast.makeText(context, "Recordatorio guardado", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context,"La fecha y hora del recordatorio ya han pasado.",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context,"Por favor, complete todos los campos",Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Guardar Tarea y Establecer Recordatorio")
        }


    }
}