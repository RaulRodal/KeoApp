/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.rodal.keoapp.ui.recordatorio.home

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.rodal.keoapp.R
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.ui.utils.PermissionAlarmDialog
import es.rodal.keoapp.ui.utils.PermissionDialog

@Composable
fun RecordatorioScreen(
    navController: NavController,
    askNotificationPermission: Boolean,
    askAlarmPermission: Boolean,
    navigateToRecordatorioEntry: () -> Unit,
    viewModel: RecordatorioViewModel = hiltViewModel()
) {
    PermissionAlarmDialog(
        askAlarmPermission = askAlarmPermission
    )
    PermissionDialog(
        askNotificationPermission = askNotificationPermission
    )
    RecordatorioScaffold(
        navController = navController,
        navigateToRecordatorioEntry = navigateToRecordatorioEntry,
        viewModel = viewModel
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioScaffold(
    navController: NavController,
    navigateToRecordatorioEntry: () -> Unit,
    viewModel: RecordatorioViewModel
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
                    Text(stringResource(id = R.string.app_name))
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToRecordatorioEntry) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            when (viewModel.recordatorioState.isEmpty()) {
                true -> EmptyView()
                false -> RecordatorioLazyColumn(viewModel, navController.context)
            }
        }
    }
}


@Composable
fun RecordatorioLazyColumn(viewModel: RecordatorioViewModel, context: Context/*, navigateToRecordatorioDetail: (Recordatorio) -> Unit*/) {
    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {

        items(viewModel.recordatorioState.asReversed()) { recordatorio ->
            RecordatorioCard(
                recordatorio = recordatorio,
                viewModel = viewModel,
                context = context,
                modifier = Modifier
                    .padding(8.dp)
            )
        }

    }
}

@Composable
fun RecordatorioCard(
    recordatorio: Recordatorio,
    viewModel: RecordatorioViewModel,
    context: Context,
    modifier: Modifier = Modifier
) {
    val color by animateColorAsState(//color dependiendo activo o no
        targetValue = if (recordatorio.active) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.errorContainer, label = "color"
    )
    val borderColor by animateColorAsState(//color borde dependiendo activo o no
        targetValue = if (recordatorio.active) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onErrorContainer, label = "color"
    )

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row {
            Column(modifier = Modifier
                .padding(8.dp)
                .weight(2f)) {
                Text(text = recordatorio.name, modifier = Modifier.padding(16.dp))
                Text(
                    text = recordatorio.recordatorioTime.toString(),
                    modifier = Modifier.padding(8.dp)
                )
            }
            Column(modifier = Modifier
                .padding(8.dp)
                .weight(1f)) {
                Button(onClick = { viewModel.deleteRecordatorio(context, recordatorio) }) {
                    Text(stringResource(id = R.string.delete))
                }
                Button(onClick = { viewModel.reverseActive(context, recordatorio) }) {
                    Text(if (recordatorio.active) stringResource(id = R.string.cancel) else stringResource(id = R.string.activate))
                }
            }
        }
    }
}

@Composable
fun EmptyView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(id = R.string.no_history_yet),
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}


