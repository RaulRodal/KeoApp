package es.rodal.keoapp.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import es.rodal.keoapp.R
import es.rodal.keoapp.ui.navigation.NavigationDestinations


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeoTopAppBar(
    title: String = "",
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateBack: () -> Unit = {}
) {
    CenterAlignedTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    ), title = {
        if (title.isBlank()) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = null
            )
        } else {
            Text(
                text = title, style = MaterialTheme.typography.headlineSmall
            )

        }
    }, navigationIcon = {
        if (canNavigateBack) {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        }
    }, actions = {
        MoreButtonWithMenu(
            navigateUp = { /*TODO*/ },
            navigateToScreen1 = { /*TODO*/ },
            navigateToScreen2 = { /*TODO*/ })
    })
}

@Composable
fun KeoBottomAppBar(navController: NavController, modifier: Modifier = Modifier) {
    NavigationBar(modifier) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationBarItem(selected = false,
                onClick = { navController.navigate(NavigationDestinations.RecordatorioCalendarDestination.route) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = stringResource(id = R.string.home)
                    )
                })
            NavigationBarItem(selected = false,
                onClick = { navController.navigate(NavigationDestinations.RecordatorioHomeDestination.route) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = stringResource(id = R.string.home)
                    )
                })
            NavigationBarItem(selected = false,
                onClick = { navController.navigate(NavigationDestinations.RecordatorioHistoryDestination.route) },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = stringResource(id = R.string.history)
                    )
                })
        }
    }
}

@Composable
fun MoreButtonWithMenu(
    navigateUp: () -> Unit,
    navigateToScreen1: () -> Unit,
    navigateToScreen2: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = stringResource(id = R.string.more_options)
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Go to Screen 1") }, onClick = {
                expanded = false
                navigateUp()
            })
            DropdownMenuItem(text = { Text("Go to Screen 1") }, onClick = {
                expanded = false
                navigateUp()
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewKeoTopAppBar() {
    KeoTopAppBar(title = "Nuevo recordatorio", canNavigateBack = true, navigateBack = {})
}
