package com.example.settleupnow.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.settleupnow.navigation.Routes
import com.example.settleupnow.viewmodel.LoginViewModel

@Composable
fun HomeScreen(navController: NavHostController, viewModel: LoginViewModel) {

    var footerIndex by remember { mutableStateOf(footerIndexState.value) }
    
    LaunchedEffect(footerIndex) {
        footerIndexState.value = footerIndex
    }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF263B6A),
                tonalElevation = 8.dp
            ) {
                val itemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.5f),
                    unselectedTextColor = Color.White.copy(alpha = 0.5f),
                    indicatorColor = Color(0xFF6984A9)
                )

                NavigationBarItem(
                    selected = footerIndex == 0,
                    onClick = { footerIndex = 0 },
                    icon = { 
                        Icon(
                            imageVector = if (footerIndex == 0) Icons.Filled.Groups else Icons.Outlined.Groups, 
                            contentDescription = "Home"
                        ) 
                    },
                    label = { 
                        Text(
                            text = "Home", 
                            fontWeight = if (footerIndex == 0) FontWeight.Bold else FontWeight.Normal 
                        ) 
                    },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = footerIndex == 1,
                    onClick = { footerIndex = 1 },
                    icon = { 
                        Icon(
                            imageVector = if (footerIndex == 1) Icons.Filled.BarChart else Icons.Outlined.BarChart, 
                            contentDescription = "Analytics"
                        ) 
                    },
                    label = { 
                        Text(
                            text = "Analytics", 
                            fontWeight = if (footerIndex == 1) FontWeight.Bold else FontWeight.Normal 
                        ) 
                    },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = footerIndex == 2,
                    onClick = { footerIndex = 2 },
                    icon = { 
                        Icon(
                            imageVector = if (footerIndex == 2) Icons.Filled.Person else Icons.Outlined.Person, 
                            contentDescription = "Profile"
                        ) 
                    },
                    label = { 
                        Text(
                            text = "Profile", 
                            fontWeight = if (footerIndex == 2) FontWeight.Bold else FontWeight.Normal 
                        ) 
                    },
                    colors = itemColors
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFEEFABD),
                            Color(0xFFA0D585)
                        )
                    )
                )
                .padding(innerPadding)
        ) {
            when (footerIndex) {
                0 -> GroupsScreen(navController = navController)
                1 -> AnalyticsScreen(navController = navController)
                2 -> ProfileScreen(
                    viewModel = viewModel(),
                    onAboutClick = { navController.navigate(Routes.ABOUT) },
                    onLogout = {
                        viewModel.updateEmail("")
                        viewModel.updatePassword("")
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

val footerIndexState = mutableStateOf(0)
