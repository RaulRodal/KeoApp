package es.rodal.keoapp.ui.screens.history

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.rodal.keoapp.R
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.ui.screens.detail.DeleteConfirmationDialog
import es.rodal.keoapp.ui.utils.KeoBottomAppBar
import es.rodal.keoapp.ui.utils.KeoTopAppBar
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RecordatorioHistoryScreen(
    navController: NavController,
    navigateToRecordatorioEntry: () -> Unit,
    navigateToRecordatorioDetail: (Int) -> Unit,
    viewModel: RecordatorioHistoryViewModel = hiltViewModel()
) {
    RecordatorioScaffold(
        navController = navController,
        navigateToRecordatorioEntry = navigateToRecordatorioEntry,
        navigateToRecordatorioDetail = navigateToRecordatorioDetail,
        viewModel = viewModel
    )
}

@Composable
fun RecordatorioScaffold(
    navController: NavController,
    navigateToRecordatorioEntry: () -> Unit,
    navigateToRecordatorioDetail: (Int) -> Unit,
    viewModel: RecordatorioHistoryViewModel
) {
    Scaffold(
        topBar = {
            KeoTopAppBar(
                title = "",
                canNavigateBack = true,
                navController = navController
            )
        },
        bottomBar = {
            KeoBottomAppBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToRecordatorioEntry) {
                Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            when (viewModel.recordatorioState.isEmpty()) {
                true -> EmptyView()
                false -> RecordatorioLazyColumn(
                    viewModel = viewModel,
                    context = navController.context,
                    navigateToRecordatorioDetail = navigateToRecordatorioDetail
                )
            }
        }
    }
}

@Composable
fun RecordatorioLazyColumn(
    viewModel: RecordatorioHistoryViewModel,
    context: Context,
    navigateToRecordatorioDetail: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.padding_small))
    ) {
        items(viewModel.recordatorioState.asReversed()) { recordatorio ->
            RecordatorioCard(
                navigateToRecordatorioDetail = navigateToRecordatorioDetail,
                recordatorio = recordatorio,
                viewModel = viewModel,
                context = context,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
fun RecordatorioCard(
    navigateToRecordatorioDetail: (Int) -> Unit,
    recordatorio: Recordatorio,
    viewModel: RecordatorioHistoryViewModel,
    context: Context,
    modifier: Modifier = Modifier
) {
    val timePrefix = stringResource(id = R.string.time_prefix)
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                viewModel.deleteRecordatorio(context, recordatorio)
            },
            onDeleteCancel = { deleteConfirmationRequired = false },
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )
    }

    val color by animateColorAsState(
        targetValue = if (recordatorio.active) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.errorContainer
        },
        label = "color"
    )

    val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy '$timePrefix' h:mm a", Locale.getDefault())

    Card(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
            .shadow(
                dimensionResource(id = R.dimen.shadow_medium),
                RoundedCornerShape(dimensionResource(id = R.dimen.corner_medium))
            )
            .background(
                Color.White,
                RoundedCornerShape(dimensionResource(id = R.dimen.corner_medium))
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { navigateToRecordatorioDetail(recordatorio.id.toInt()) },
                    onLongPress = { deleteConfirmationRequired = true }
                )
            },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_medium)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.elevation_medium))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(2f)) {
                Text(
                    text = recordatorio.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
                )
                Text(
                    text = dateFormat.format(recordatorio.recordatorioTime),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
            Button(
                onClick = { viewModel.reverseActive(context, recordatorio) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = color,
                    contentColor = if (recordatorio.active) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    }
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = if (recordatorio.active) {
                        stringResource(id = R.string.cancel)
                    } else {
                        stringResource(
                            id = R.string.activate
                        )
                    },
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun EmptyView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(id = R.string.no_history_yet),
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}
