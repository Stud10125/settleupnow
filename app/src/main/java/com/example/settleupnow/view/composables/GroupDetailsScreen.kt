package com.example.settleupnow.view.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.settleupnow.navigation.Routes
import com.example.settleupnow.viewmodel.GroupDetailsViewModel

@Composable
fun GroupDetailScreen(
    navController: NavController,
    groupId: String,
    viewModel: GroupDetailsViewModel = viewModel()
) {
    val groupName by viewModel.groupName.collectAsState()

    // Load group info when screen opens
    LaunchedEffect(groupId) {
        viewModel.fetchGroup(groupId)
    }

    Scaffold { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = groupName.ifBlank { "Loading..." },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Row {
                    IconButton(onClick = {
                        navController.navigate("${Routes.GROUP_INFO}/$groupId")
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Group Info"
                        )
                    }

                }
            }

            Spacer(Modifier.height(16.dp))

            // Expenses list placeholder
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(10) { e ->
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clickable { navController.navigate(Routes.EXPENSE_INFO) }
                    ) {
                        Text(text = "Expense", modifier = Modifier.weight(1f))
                        Text(text = "Rate", modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.navigate(Routes.ADD_EXPENSE) },
                    modifier = Modifier.weight(1f)
                ) { Text("Add Expense") }

                OutlinedButton(
                    onClick = { navController.navigate(Routes.SUMMARY) },
                    modifier = Modifier.weight(1f)
                ) { Text("Balance") }
            }

            OutlinedButton(
                onClick = { /* Add member logic */ },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Add Member") }
        }
    }
}
