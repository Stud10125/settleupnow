package com.example.settleupnow.view.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController


@Composable
fun HomeScreen(navController: NavHostController) {

    var footerIndex by remember { mutableStateOf(0) }

    Column(Modifier.fillMaxSize()){
        Column(Modifier.fillMaxWidth().weight(1f)) {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    if (footerIndex == 0) {
                        GroupsScreen(
                            navController = navController
                        )
                    } else if (footerIndex == 1) {
                        AnalyticsScreen(
                            navController = navController
                        )
                    } else if (footerIndex == 2) {
                        ProfileScreen(
                            viewModel = viewModel(),
                            onAccountClick = { },
                            onChangePasswordClick = { },
                            onSupportClick = { },
                            onAboutClick = { }
                        )
                    }
                }
            }
        }
        Column (modifier = Modifier.fillMaxWidth()){
            TabRow(
                selectedTabIndex = footerIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = footerIndex == 0,
                    onClick = { footerIndex = 0 },
                    text = { Text("Home") }
                )
                Tab(
                    selected = footerIndex == 1,
                    onClick = { footerIndex = 1 },
                    text = { Text("Analytics") }
                )
                Tab(
                    selected = footerIndex == 2,
                    onClick = { footerIndex = 2 },
                    text = { Text("Profile") }
                )
            }
        }
    }
}


