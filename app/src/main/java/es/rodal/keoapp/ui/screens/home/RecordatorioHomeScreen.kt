package es.rodal.keoapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import es.rodal.keoapp.R
import es.rodal.keoapp.ui.navigation.NavigationDestinations
import es.rodal.keoapp.ui.utils.KeoTopAppBar
import es.rodal.keoapp.ui.utils.PermissionAlarmDialog
import es.rodal.keoapp.ui.utils.PermissionDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioHomeScreen(
    navController: NavController,
    askNotificationPermission: Boolean,
    askAlarmPermission: Boolean
) {

    PermissionAlarmDialog(
        askAlarmPermission = askAlarmPermission
    )
    PermissionDialog(
        askNotificationPermission = askNotificationPermission
    )

    Scaffold(
        topBar = {
            KeoTopAppBar(
                showBar = false,
                canNavigateBack = false
            )
        }
    ) { innerPadding ->
        RecordatorioHomeSurface(navController = navController, paddingValues = innerPadding)
    }
}
    @Composable
    fun RecordatorioHomeSurface(
        navController: NavController,
        paddingValues: PaddingValues
    ) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
                //.padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .background(Color(0xFFFCF9F8), shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(350.dp)
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

            Text(
                text = stringResource(id = R.string.recordatorio_home_screen_subtitle),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_large)))

            Button(
                onClick = { navController.navigate(NavigationDestinations.RecordatorioCalendarDestination.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_medium)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(text = stringResource(id = R.string.recordatorio_show_calendar))
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_medium)))

            Button(
                onClick = { navController.navigate(NavigationDestinations.RecordatorioHistoryDestination.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_medium)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = stringResource(id = R.string.recordatorio_show_reminders))
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_medium)))

            Button(
                onClick = { navController.navigate("${NavigationDestinations.RecordatorioEntryDestination.route}/0") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_medium)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(text = stringResource(id = R.string.recordatorio_add_reminder))
            }
        }
    }
}