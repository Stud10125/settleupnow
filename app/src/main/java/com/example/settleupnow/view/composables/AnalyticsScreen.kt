package com.example.settleupnow.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.settleupnow.navigation.Routes
import com.example.settleupnow.viewmodel.AnalyticsViewModel

@Composable
fun AnalyticsScreen(
    navController: NavHostController, 
    viewModel: AnalyticsViewModel = viewModel()
) {
    val balances by viewModel.groupBalances.collectAsState()
    val totalBalance by viewModel.totalBalance.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAnalytics()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Analytics Summary", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        
        Spacer(Modifier.height(20.dp))

        // Total Overview Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (totalBalance >= 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
            )
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (totalBalance >= 0) "Overall: You are owed" else "Overall: You owe",
                    fontSize = 16.sp
                )
                Text(
                    text = "₹${String.format("%.2f", if (totalBalance >= 0) totalBalance else -totalBalance)}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (totalBalance >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Balances by Group", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        
        Spacer(Modifier.height(12.dp))

        if (balances.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No group activity recorded yet.", color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(balances) { groupAnalytics ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable { navController.navigate("${Routes.GROUP_DATA}/${groupAnalytics.groupId}") },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(groupAnalytics.groupName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(
                                    text = if (groupAnalytics.balance >= 0) "You are owed" else "You owe",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = "₹${String.format("%.2f", if (groupAnalytics.balance >= 0) groupAnalytics.balance else -groupAnalytics.balance)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (groupAnalytics.balance >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                        }
                    }
                }
            }
        }
    }
}
