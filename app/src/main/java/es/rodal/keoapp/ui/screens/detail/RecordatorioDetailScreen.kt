package es.rodal.keoapp.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.rodal.keoapp.R
import es.rodal.keoapp.data.domain.model.getFormattedDate
import es.rodal.keoapp.data.domain.model.getFormattedTime
import es.rodal.keoapp.ui.screens.history.EmptyView
import es.rodal.keoapp.ui.screens.history.RecordatorioHistoryViewModel
import es.rodal.keoapp.ui.screens.history.RecordatorioLazyColumn
import es.rodal.keoapp.ui.utils.KeoBottomAppBar
import es.rodal.keoapp.ui.utils.KeoTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioDetailScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navController: NavController,
    navigateToRecordatorioEntry: () -> Unit,
    navigateToRecordatorioDetail: (Int) -> Unit,
    viewModel: RecordatorioDetailViewModel = hiltViewModel()
){
    Scaffold(
        topBar = {
            KeoTopAppBar("", false)
        },
        bottomBar = {
            KeoBottomAppBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToRecordatorioEntry) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        RecordatorioDetailColumn(
            padding = innerPadding,
            navigateToEditRecordatorio = { TODO() },
            navigateBack = navigateBack
        )
    }
}

@Composable
fun RecordatorioDetailColumn(
    padding: PaddingValues,
    modifier: Modifier = Modifier,
    navigateToEditRecordatorio: (Int) -> Unit,
    navigateBack: () -> Unit,
    viewModel: RecordatorioDetailViewModel = hiltViewModel()
) {

    val recordatorio by viewModel.recordatorioState.collectAsState()

    var name by remember { mutableStateOf(TextFieldValue(recordatorio.name)) }
    var description by remember { mutableStateOf(TextFieldValue(recordatorio.description)) }
    var date by remember { mutableStateOf(recordatorio.recordatorioTime.getFormattedDate()) }
    var time by remember { mutableStateOf(recordatorio.recordatorioTime.getFormattedTime()) }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(padding)) {

        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        Text(text = "Recordatorio Details", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = TextFieldValue(date),
            onValueChange = { /* Update date here */ },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = TextFieldValue(time),
            onValueChange = { /* Update time here */ },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = {
                val updatedRecordatorio = recordatorio.copy(
                    name = name.text,
                    description = description.text
                )
                //onSave(updatedRecordatorio)
            }) {
                Text(text = "Save")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = { deleteConfirmationRequired = true },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.delete))
            }
            if (deleteConfirmationRequired) {
                DeleteConfirmationDialog(
                    onDeleteConfirm = {
                        deleteConfirmationRequired = false
                        viewModel.deleteRecordatorio(recordatorio)
                    },
                    onDeleteCancel = { deleteConfirmationRequired = false },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                )
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        })
}