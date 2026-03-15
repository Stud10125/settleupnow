package com.example.settleupnow.view.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

    // Color Palette
    val creamyYellow = Color(0xFFEEFABD)
    val sageGreen = Color(0xFFA0D585)
    val steelBlue = Color(0xFF6984A9)
    val deepNavy = Color(0xFF263B6A)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(creamyYellow, sageGreen)
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                            enabled = groupName.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = deepNavy,
                                contentColor = Color.White,
                                disabledContainerColor = deepNavy.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text("Create", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        OutlinedButton(
                            onClick = {
                                viewModel.clearData()
                                navController.popBackStack()
                            },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(2.dp, deepNavy),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = deepNavy),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Cancel", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "ADD GROUP",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = deepNavy,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(32.dp))

                OutlinedTextField(
                    value = groupName,
                    onValueChange = { viewModel.onGroupNameChange(it) },
                    label = { Text("Group Name", fontWeight = FontWeight.Medium) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = deepNavy,
                        unfocusedBorderColor = steelBlue.copy(alpha = 0.5f),
                        focusedLabelColor = deepNavy,
                        unfocusedLabelColor = steelBlue,
                        cursorColor = deepNavy,
                        focusedTextColor = deepNavy,
                        unfocusedTextColor = deepNavy
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { viewModel.onDescriptionChange(it) },
                    label = { Text("Description (optional)", fontWeight = FontWeight.Medium) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = deepNavy,
                        unfocusedBorderColor = steelBlue.copy(alpha = 0.5f),
                        focusedLabelColor = deepNavy,
                        unfocusedLabelColor = steelBlue,
                        cursorColor = deepNavy,
                        focusedTextColor = deepNavy,
                        unfocusedTextColor = deepNavy
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Members",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = deepNavy
                    )
                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = deepNavy,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Add", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(members) { user ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.9f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = BorderStroke(1.dp, steelBlue.copy(alpha = 0.1f))
                        ) {
                            ListItem(
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                headlineContent = { 
                                    Text(user.name, fontWeight = FontWeight.Bold, color = deepNavy) 
                                },
                                supportingContent = { 
                                    Text(user.email, color = steelBlue) 
                                },
                                trailingContent = {
                                    IconButton(onClick = { viewModel.removeMember(user) }) {
                                        Icon(
                                            Icons.Default.Delete, 
                                            contentDescription = "Remove", 
                                            tint = Color(0xFFC62828).copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                errorMessage?.let {
                    Spacer(Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            it,
                            color = Color(0xFFC62828),
                            modifier = Modifier.padding(12.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = creamyYellow,
            shape = RoundedCornerShape(28.dp),
            title = { 
                Text("Add Member", color = deepNavy, fontWeight = FontWeight.Bold) 
            },
            text = {
                OutlinedTextField(
                    value = memberEmail,
                    onValueChange = { memberEmail = it },
                    label = { Text("Member Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = deepNavy,
                        unfocusedBorderColor = steelBlue,
                        focusedLabelColor = deepNavy,
                        unfocusedLabelColor = steelBlue,
                        focusedTextColor = deepNavy,
                        unfocusedTextColor = deepNavy
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addMemberByEmail(memberEmail) { success, message ->
                            if (success) {
                                showDialog = false
                                memberEmail = ""
                            } else {
                                errorMessage = message
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = deepNavy),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = deepNavy)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
