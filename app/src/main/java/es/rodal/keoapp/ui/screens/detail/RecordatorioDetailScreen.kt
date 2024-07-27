package es.rodal.keoapp.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.rodal.keoapp.R
import es.rodal.keoapp.data.domain.model.getFormattedDate
import es.rodal.keoapp.data.domain.model.getFormattedTime
import es.rodal.keoapp.ui.navigation.NavigationDestinations
import es.rodal.keoapp.ui.utils.KeoBottomAppBar
import es.rodal.keoapp.ui.utils.KeoTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioDetailScreen(
    modifier: Modifier = Modifier,
    navigateToEditRecordatorio: (Long) -> Unit,
    navController: NavController,
    viewModel: RecordatorioDetailViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            KeoTopAppBar(
                title = stringResource(id = NavigationDestinations.RecordatorioDetailDestination.titleRes),
                canNavigateBack = true,
                navigateBack = { navController.popBackStack() }
            )
        },
        bottomBar = {
            KeoBottomAppBar(navController)
        }
    ) { innerPadding ->
        RecordatorioDetailColumn(
            padding = innerPadding,
            navigateToEditRecordatorio = navigateToEditRecordatorio,
            navController = navController,
            viewModel = viewModel
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioDetailColumn(
    padding: PaddingValues,
    modifier: Modifier = Modifier,
    navigateToEditRecordatorio: (Long) -> Unit,
    navController: NavController,
    viewModel: RecordatorioDetailViewModel
) {

    val recordatorio by viewModel.recordatorioState.collectAsState()

    Column(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_large))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        Text(text = stringResource(id = R.string.recordatorio_details), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))


        Row {
            Text(text = stringResource(id = R.string.recordatorio_name), style = MaterialTheme.typography.labelLarge, modifier = modifier.weight(1f))
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_large)))
            Text(text = recordatorio.name, style = MaterialTheme.typography.labelLarge)
        }
        Row {
            Text(text = stringResource(id = R.string.recordatorio_description), style = MaterialTheme.typography.labelLarge, modifier = modifier.weight(1f))
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_large)))
            Text(text = recordatorio.description, style = MaterialTheme.typography.labelLarge)
        }
        Row {
            Text(text = stringResource(id = R.string.recordatorio_date), style = MaterialTheme.typography.labelLarge, modifier = modifier.weight(1f))
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_large)))
            Text(text = recordatorio.recordatorioTime.getFormattedDate(), style = MaterialTheme.typography.labelLarge)
        }
        Row {
            Text(text = stringResource(id = R.string.recordatorio_time), style = MaterialTheme.typography.labelLarge, modifier = modifier.weight(1f))
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_large)))
            Text(text = recordatorio.recordatorioTime.getFormattedTime(), style = MaterialTheme.typography.labelLarge)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { deleteConfirmationRequired = true },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = R.string.delete))
            }
            OutlinedButton(
                onClick = { navigateToEditRecordatorio(recordatorio.id!!) },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = R.string.edit))
            }
        }

        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    viewModel.deleteRecordatorio(navController.context, recordatorio)
                    navController.popBackStack()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
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