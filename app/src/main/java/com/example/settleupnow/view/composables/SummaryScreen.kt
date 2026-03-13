package com.example.settleupnow.view.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.settleupnow.viewmodel.SimplifiedTransaction
import com.example.settleupnow.viewmodel.SummaryViewModel

@Composable
fun SummaryScreen(
    groupId: String,
    navController: NavController,
    viewModel: SummaryViewModel = viewModel()
) {
    val balances by viewModel.balances.collectAsState()
    var showSimplifyPopup by remember { mutableStateOf(false) }
    var simplifiedTransactions by remember { mutableStateOf<List<SimplifiedTransaction>>(emptyList()) }

    LaunchedEffect(groupId) {
        viewModel.loadSummary(groupId)
    }

    if (showSimplifyPopup) {
        AlertDialog(
            onDismissRequest = { showSimplifyPopup = false },
            title = { Text("Simplified Settlements") },
            text = {
                if (simplifiedTransactions.isEmpty()) {
                    Text("No settlements needed!")
                } else {
                    LazyColumn {
                        items(simplifiedTransactions) { transaction ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "${transaction.from} pays ${transaction.to}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Amount: ₹${String.format("%.2f", transaction.amount)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSimplifyPopup = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Group Summary",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (balances.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Calculating balances...", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(balances) { userBalance ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (userBalance.balance >= 0)
                                Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = userBalance.userName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (userBalance.balance >= 0)
                                    "Gets back ₹${String.format("%.2f", userBalance.balance)}"
                                    else "Owes ₹${String.format("%.2f", -userBalance.balance)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (userBalance.balance >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                simplifiedTransactions = viewModel.getSimplifiedTransactions()
                showSimplifyPopup = true
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = "Simplify")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Back to Group")
        }
    }
}
