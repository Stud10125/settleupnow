package com.example.settleupnow.view.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.settleupnow.viewmodel.AddGroupViewModel

@Composable
fun AddGroupScreen(
    navController: NavHostController,
    viewModel: AddGroupViewModel
) {
    val groupName by viewModel.groupName.collectAsState()
    val description by viewModel.description.collectAsState()
    val members by viewModel.members.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var memberEmail by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.createGroup { success, message ->
                            if (success) {
                                navController.popBackStack()
                            } else {
                                errorMessage = message
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = groupName.isNotBlank()
                ) {
                    Text("Create")
                }
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Add Group",
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = groupName,
                onValueChange = { viewModel.onGroupNameChange(it) },
                label = { Text("Group name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Members", fontSize = 18.sp)
                TextButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Add member")
                }
            }

            Spacer(Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(members) { user ->
                    ListItem(
                        headlineContent = { Text(user.name) },
                        supportingContent = { Text(user.email) },
                        trailingContent = {
                            IconButton(onClick = { viewModel.removeMember(user) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove")
                            }
                        }
                    )
                }
            }

            errorMessage?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }

    // Dialog for adding member
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Member") },
            text = {
                OutlinedTextField(
                    value = memberEmail,
                    onValueChange = { memberEmail = it },
                    label = { Text("Enter member email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.addMemberByEmail(memberEmail) { success, message ->
                        if (success) {
                            showDialog = false
                            memberEmail = ""
                        } else {
                            errorMessage = message
                        }
                    }
                }) {
                    Text("Done")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}
