package es.rodal.keoapp.ui.screens.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.rodal.keoapp.R
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.domain.model.getFormattedDate
import es.rodal.keoapp.data.domain.model.getFormattedTime
import es.rodal.keoapp.ui.navigation.NavigationDestinations
import es.rodal.keoapp.ui.utils.KeoBottomAppBar
import es.rodal.keoapp.ui.utils.KeoTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioDetailScreen(
    navigateToEditRecordatorio: (Long) -> Unit,
    navController: NavController,
    viewModel: RecordatorioDetailViewModel = hiltViewModel()
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { deleteConfirmationRequired = true },
                containerColor = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.padding_medium),
                    bottom = dimensionResource(id = R.dimen.padding_medium)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.delete)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Start
    ) { innerPadding ->
        RecordatorioDetailContent(
            padding = innerPadding,
            navigateToEditRecordatorio = navigateToEditRecordatorio,
            navController = navController,
            viewModel = viewModel
        )
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    viewModel.deleteRecordatorio(navController.context, viewModel.recordatorioState.value)
                    navController.popBackStack()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun RecordatorioDetailContent(
    padding: PaddingValues,
    navigateToEditRecordatorio: (Long) -> Unit,
    navController: NavController,
    viewModel: RecordatorioDetailViewModel
) {
    val recordatorio by viewModel.recordatorioState.collectAsState()
    var isNotificationEnabled by remember { mutableStateOf(recordatorio.active) }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(dimensionResource(id = R.dimen.padding_large))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_medium)))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation( defaultElevation = dimensionResource(id = R.dimen.elevation_medium))
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                RecordatorioDetailRow(labelResId = R.string.recordatorio_name, value = recordatorio.name)
                HorizontalDivider()
                RecordatorioDetailRow(labelResId = R.string.recordatorio_description, value = recordatorio.description)
                HorizontalDivider()
                RecordatorioDetailRow(labelResId = R.string.recordatorio_status, value = if (recordatorio.active) stringResource(id = R.string.enabled) else stringResource(id = R.string.disabled))
                HorizontalDivider()
                RecordatorioDetailRow(labelResId = R.string.recordatorio_date, value = recordatorio.recordatorioTime.getFormattedDate())
                HorizontalDivider()
                RecordatorioDetailRow(labelResId = R.string.recordatorio_time, value = recordatorio.recordatorioTime.getFormattedTime())
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ActionButtons(
            recordatorio = recordatorio,
            onEditClick = { navigateToEditRecordatorio(recordatorio.id) },
            onNotificationToggle = {
                isNotificationEnabled = !isNotificationEnabled
                viewModel.reverseActive(navController.context, recordatorio)
            }
        )
    }
}

@Composable
fun RecordatorioDetailRow(labelResId: Int, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small))
    ) {
        Text(
            text = stringResource(id = labelResId),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_large)))
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ActionButtons(
    recordatorio: Recordatorio,
    onNotificationToggle: () -> Unit,
    onEditClick: () -> Unit
) {

    val color by animateColorAsState(
        targetValue = if (recordatorio.active) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.errorContainer, label = "color"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_small)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onNotificationToggle,
            colors = ButtonDefaults.buttonColors(
                containerColor = color,
                contentColor = if (recordatorio.active) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
            ),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.border_radius_small)),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = if (recordatorio.active) stringResource(id = R.string.disable_notification) else stringResource(id = R.string.enable_notification),
                fontSize = 12.sp
            )
        }
        OutlinedButton(
            onClick = onEditClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(id = R.string.edit))
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
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
        }
    )
}