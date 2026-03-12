package com.example.settleupnow.view.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.settleupnow.navigation.Routes
import com.example.settleupnow.viewmodel.ExpenseInfoViewModel

@Composable
fun ExpenseInfoScreen(
    expenseId: String,
    navController: NavController,
    viewModel: ExpenseInfoViewModel = viewModel()
) {
    val expense by viewModel.expense.collectAsState()
    val participantAmounts by viewModel.participantAmounts.collectAsState()

    LaunchedEffect(expenseId) {
        viewModel.loadExpense(expenseId)
    }

    Scaffold(
        floatingActionButton = {
            if (expense != null) {
                FloatingActionButton(
                    onClick = { navController.navigate("${Routes.EDIT_EXPENSE}/$expenseId") }
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Expense")
                }
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
                Text("Expense Details", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            if (expense == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(expense!!.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("Total: ₹${expense!!.amount}", fontSize = 20.sp, color = Color(0xFF2E7D32))
                        Spacer(Modifier.height(8.dp))
                        Text("Paid by: ${expense!!.paidByName}", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text("Splits", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                LazyColumn {
                    items(participantAmounts.toList()) { (name, amount) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(name, fontSize = 16.sp)
                            Text("₹$amount", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    }
                }
            }
        }
    }
}
