package com.example.settleupnow.view.composables

import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
    val expenses by viewModel.expenses.collectAsState()
    val context = LocalContext.current

    var showAddMemberDialog by remember { mutableStateOf(false) }
    var memberEmail by remember { mutableStateOf("") }

    // Load group info when screen opens
    LaunchedEffect(groupId) {
        viewModel.fetchGroup(groupId)
    }

    if (showAddMemberDialog) {
        AlertDialog(
            onDismissRequest = { showAddMemberDialog = false },
            title = { Text("Add New Member") },
            text = {
                Column {
                    Text("Enter the email of the user you want to add to this group.")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = memberEmail,
                        onValueChange = { memberEmail = it },
                        placeholder = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (memberEmail.isNotBlank()) {
                        viewModel.addMemberByEmail(groupId, memberEmail) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success) {
                                showAddMemberDialog = false
                                memberEmail = ""
                            }
                        }
                    }
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddMemberDialog = false }) {
                    Text("Cancel")
                }
            }
        )
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

            // Expenses list
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(expenses) { expense ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { navController.navigate("${Routes.EXPENSE_INFO}/${expense.expenseId}") }
                    ) {
                        Text(text = expense.title, modifier = Modifier.weight(1f))
                        Text(
                            text = "₹${expense.amount}", 
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                if (expenses.isEmpty()) {
                    item {
                        Text(
                            "No expenses yet. Add one below!",
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("${Routes.ADD_EXPENSE}/$groupId") },
                    modifier = Modifier.weight(1f)
                ) { Text("Add Expense") }

                OutlinedButton(
                    onClick = { navController.navigate("${Routes.SUMMARY}/$groupId") },
                    modifier = Modifier.weight(1f)
                ) { Text("Balance") }
            }

            OutlinedButton(
                onClick = { showAddMemberDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Add Member") }
        }
    }
}
