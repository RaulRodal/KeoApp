package es.rodal.keoapp.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import es.rodal.keoapp.R
import java.util.Objects


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeoTopAppBar(navigateBack: (() -> Unit)?, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Icon(painter = painterResource(id = R.mipmap.ic_launcher_foreground), contentDescription = null)
//            Text(
//                text = stringResource(id = R.string.app_name),
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
        },
        navigationIcon = {
            if (Objects.nonNull(navigateBack)) {
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@Composable
fun KeoBottomAppBar(navController: NavController, modifier: Modifier = Modifier) {
    NavigationBar(modifier) {
        Row (
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationBarItem(
                selected = true,
                onClick = {  },
                icon = { Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = stringResource(id = R.string.home)
                ) }
            )
            NavigationBarItem(
                selected = true,
                onClick = { /*TODO*/ },
                icon = { Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = stringResource(id = R.string.home)
                ) }
            )
            NavigationBarItem(
                selected = true,
                onClick = { /*TODO*/ },
                icon = { Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = stringResource(id = R.string.home)
                ) }
            )
        }
    }
}


