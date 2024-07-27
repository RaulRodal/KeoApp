package es.rodal.keoapp.ui.screens.entry

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.rodal.keoapp.R
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.ui.utils.KeoBottomAppBar
import es.rodal.keoapp.ui.utils.KeoTopAppBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioEntryScreen(
    navController: NavController,
    canNavigateBack: Boolean = true,
    viewModel: RecordatorioEntryViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            KeoTopAppBar(
                title = stringResource(id = R.string.recordatorio_entry_title),
                canNavigateBack = true,
                navigateBack = navController::navigateUp
            )
        },
        bottomBar = {
            KeoBottomAppBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Form(viewModel = viewModel, navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Form(viewModel: RecordatorioEntryViewModel, navController: NavController) {
    val recordatorio by viewModel.recordatorioState.collectAsState()
    val context = navController.context
    var name by remember { mutableStateOf(recordatorio.name) }
    var description by remember { mutableStateOf(recordatorio.description) }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var calendar by rememberSaveable { mutableStateOf(recordatorio.recordatorioTime?.let {
        Calendar.getInstance().apply { time = it }
    } ?: Calendar.getInstance()) }

    var isDateSelected by remember { mutableStateOf(recordatorio != null) }
    var isTimeSelected by remember { mutableStateOf(recordatorio != null) }
    var selectedDate by remember { mutableStateOf(recordatorio?.recordatorioTime?.let {
        dateFormat.format(it)
    } ?: "") }
    var selectedTime by remember { mutableStateOf(recordatorio?.recordatorioTime?.let {
        String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
    } ?: "") }

    LaunchedEffect(recordatorio) {
        name = recordatorio.name
        description = recordatorio.description
        calendar = recordatorio.recordatorioTime?.let {
            Calendar.getInstance().apply { time = it }
        } ?: Calendar.getInstance()
        selectedDate = dateFormat.format(calendar.time)
        selectedTime = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = Modifier.fillMaxSize().padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = stringResource(id = R.string.recordatorio_name)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = stringResource(id = R.string.recordatorio_description)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        val openDialog = remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState()

        OutlinedButton(
            onClick = { openDialog.value = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isDateSelected) selectedDate else stringResource(id = R.string.select_date))
        }
        if (openDialog.value) {
            val confirmEnabled = remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
            DatePickerDialog(
                onDismissRequest = { openDialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDialog.value = false
                            datePickerState.selectedDateMillis?.let { dateInMillis ->
                                val datePicker = Calendar.getInstance().apply {
                                    timeInMillis = dateInMillis
                                }
                                val year = datePicker.get(Calendar.YEAR)
                                val month = datePicker.get(Calendar.MONTH)
                                val day = datePicker.get(Calendar.DAY_OF_MONTH)

                                calendar.set(Calendar.YEAR, year)
                                calendar.set(Calendar.MONTH, month)
                                calendar.set(Calendar.DAY_OF_MONTH, day)

                                isDateSelected = true
                                selectedDate = dateFormat.format(calendar.time)
                            }
                        },
                        enabled = confirmEnabled.value
                    ) { Text("OK") }
                },
                dismissButton = { TextButton(onClick = { openDialog.value = false }) { Text("Cancel") } }
            ) { DatePicker(state = datePickerState) }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        var showTimePicker by remember { mutableStateOf(false) }
        val state = rememberTimePickerState(
            initialHour = if (isTimeSelected) calendar.get(Calendar.HOUR_OF_DAY) else 0,
            initialMinute = if (isTimeSelected) calendar.get(Calendar.MINUTE) else 0
        )

        OutlinedButton(
            onClick = { showTimePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isTimeSelected) selectedTime else stringResource(id = R.string.select_time))
        }

        if (showTimePicker) {
            TimePickerDialog(
                onCancel = { showTimePicker = false },
                onConfirm = {
                    calendar.set(Calendar.HOUR_OF_DAY, state.hour)
                    calendar.set(Calendar.MINUTE, state.minute)
                    isTimeSelected = true
                    selectedTime = String.format("%02d:%02d", state.hour, state.minute)
                    showTimePicker = false
                },
            ) { TimeInput(state = state) }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        Button(
            onClick = {
                if (name.isNotEmpty() && isDateSelected && isTimeSelected) {
                    if (calendar.after(Calendar.getInstance())) {
                        if (recordatorio != null) {
                            viewModel.updateRecordatorio(context, recordatorio.copy(
                                name = name,
                                description = description,
                                recordatorioTime = calendar.time
                            ))
                            Toast.makeText(context, context.getString(R.string.reminder_updated), Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.addRecordatorio(context, Recordatorio(
                                name = name,
                                description = description,
                                recordatorioTime = calendar.time
                            ))
                            Toast.makeText(context, context.getString(R.string.reminder_saved), Toast.LENGTH_SHORT).show()
                        }
                        Toast.makeText(context, context.getString(R.string.reminder_saved), Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, context.getString(R.string.wrong_datetime), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.please_fill_all_fields), Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = if (recordatorio == null) R.string.set_reminder else R.string.update_reminder))
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            toggle()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = title, style = MaterialTheme.typography.headlineSmall)
                content()
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onCancel) { Text("Cancel") }
                    TextButton(onClick = onConfirm) { Text("OK") }
                }
            }
        }
    }
}