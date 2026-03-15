package com.example.settleupnow.view.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

    val creamyYellow = Color(0xFFEEFABD)
    val sageGreen = Color(0xFFA0D585)
    val steelBlue = Color(0xFF6984A9)
    val deepNavy = Color(0xFF263B6A)

    LaunchedEffect(groupId) {
        viewModel.fetchGroup(groupId)
    }

    if (showAddMemberDialog) {
        AlertDialog(
            onDismissRequest = { showAddMemberDialog = false },
            containerColor = creamyYellow,
            shape = RoundedCornerShape(28.dp),
            title = { 
                Text(
                    text = "Add Member",
                    fontWeight = FontWeight.Bold,
                    color = deepNavy
                ) 
            },
            text = {
                Column {
                    Text(
                        text = "Enter email to invite to $groupName", 
                        fontSize = 14.sp,
                        color = deepNavy.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = memberEmail,
                        onValueChange = { memberEmail = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = deepNavy,
                            unfocusedBorderColor = steelBlue
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (memberEmail.isNotBlank()) {
                            viewModel.addMemberByEmail(groupId, memberEmail) { success, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                if (success) { showAddMemberDialog = false; memberEmail = "" }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = deepNavy),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Add") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAddMemberDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = deepNavy)
                ) { Text("Cancel") }
            }
        )
    }

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
            topBar = {
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier.statusBarsPadding()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = deepNavy
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = groupName.ifBlank { "Group Details" },
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = deepNavy
                            )
                        }
                        IconButton(onClick = { navController.navigate("${Routes.GROUP_INFO}/$groupId") }) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Group Info",
                                tint = deepNavy
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("${Routes.ADD_EXPENSE}/$groupId") },
                    containerColor = deepNavy,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Expense", modifier = Modifier.size(28.dp))
                }
            }
        ) { inner ->
            Column(
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, steelBlue.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "Total Spending",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = steelBlue,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "₹${expenses.sumOf { it.amount }}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = deepNavy
                                )
                            }
                            Surface(
                                color = creamyYellow,
                                shape = CircleShape,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = null,
                                    modifier = Modifier.padding(12.dp),
                                    tint = deepNavy
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { navController.navigate("${Routes.SUMMARY}/$groupId") },
                                modifier = Modifier.weight(1f).height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = deepNavy,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Balances", fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { showAddMemberDialog = true },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(2.dp, deepNavy),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = deepNavy)
                            ) {
                                Icon(Icons.Default.GroupAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Invite", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Text(
                    text = "Recent Activity",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = deepNavy,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if (expenses.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = deepNavy.copy(alpha = 0.1f)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "No expenses yet",
                                style = MaterialTheme.typography.bodyLarge,
                                color = deepNavy.copy(alpha = 0.4f)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(expenses) { expense ->
                            ExpenseItemCard(expense, deepNavy, steelBlue, creamyYellow, onClick = {
                                navController.navigate("${Routes.EXPENSE_INFO}/${expense.expenseId}")
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseItemCard(
    expense: com.example.settleupnow.model.Expense, 
    deepNavy: Color, 
    steelBlue: Color, 
    creamyYellow: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, steelBlue.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = creamyYellow,
                shape = CircleShape,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = null,
                    tint = deepNavy,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 16.sp,
                    color = deepNavy
                )
                Text(
                    text = "Paid by ${expense.paidByName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = steelBlue
                )
            }
            Text(
                text = "₹${expense.amount}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = deepNavy
            )
        }
    }
}
