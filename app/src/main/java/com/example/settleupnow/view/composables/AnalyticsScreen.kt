package com.example.settleupnow.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier.statusBarsPadding()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "FINANCIAL INSIGHTS",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = Color(0xFF263B6A)
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(16.dp))

                val isOverallPositive = totalBalance >= 0
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF263B6A),
                                        Color(0xFF6984A9)
                                    )
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalance,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "NET BALANCE",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White.copy(alpha = 0.7f),
                                    letterSpacing = 1.sp
                                )
                            }
                            
                            Spacer(Modifier.height(8.dp))
                            
                            Text(
                                text = "₹${String.format("%.2f", if (isOverallPositive) totalBalance else -totalBalance)}",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            
                            Spacer(Modifier.height(4.dp))
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isOverallPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                    contentDescription = null,
                                    tint = if (isOverallPositive) Color(0xFFEEFABD) else Color(0xFFFFAB91),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = if (isOverallPositive) "Total you are owed" else "Total you owe",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                Text(
                    text = "Group Breakdown", 
                    style = MaterialTheme.typography.titleMedium, 
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF263B6A)
                )
                
                Spacer(Modifier.height(16.dp))

                if (balances.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().weight(1f), 
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Groups, 
                                contentDescription = null, 
                                modifier = Modifier.size(64.dp),
                                tint = Color(0xFF263B6A).copy(alpha = 0.2f)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "No activity recorded yet", 
                                color = Color(0xFF263B6A).copy(alpha = 0.6f)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(balances) { groupAnalytics ->
                            val isPositive = groupAnalytics.balance >= 0
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navController.navigate("${Routes.GROUP_DATA}/${groupAnalytics.groupId}") },
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF6984A9)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = groupAnalytics.groupName.take(1).uppercase(),
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }

                                    Spacer(Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = groupAnalytics.groupName, 
                                            fontWeight = FontWeight.Bold, 
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color(0xFF263B6A)
                                        )
                                        Text(
                                            text = if (isPositive) "You are owed" else "You owe",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF6984A9)
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "₹${String.format("%.2f", if (isPositive) groupAnalytics.balance else -groupAnalytics.balance)}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isPositive) Color(0xFF2E7D32) else Color(0xFFC62828)
                                        )
                                        Icon(
                                            imageVector = Icons.Default.ChevronRight,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = Color(0xFF263B6A).copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
