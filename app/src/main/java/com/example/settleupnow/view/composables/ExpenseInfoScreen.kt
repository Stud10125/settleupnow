package com.example.settleupnow.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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

    val creamyYellow = Color(0xFFEEFABD)
    val sageGreen = Color(0xFFA0D585)
    val steelBlue = Color(0xFF6984A9)
    val deepNavy = Color(0xFF263B6A)

    LaunchedEffect(expenseId) {
        viewModel.loadExpense(expenseId)
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = deepNavy
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "EXPENSE DETAILS",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = deepNavy
                        )
                    }
                }
            },
            floatingActionButton = {
                if (expense != null) {
                    FloatingActionButton(
                        onClick = { navController.navigate("${Routes.EDIT_EXPENSE}/$expenseId") },
                        containerColor = deepNavy,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Expense")
                    }
                }
            }
        ) { inner ->
            Column(
                modifier = Modifier
                    .padding(inner)
                    .padding(horizontal = 20.dp)
                    .fillMaxSize()
            ) {
                if (expense == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = deepNavy)
                    }
                } else {
                    Spacer(Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                color = creamyYellow,
                                shape = CircleShape,
                                modifier = Modifier.size(64.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ReceiptLong,
                                    contentDescription = null,
                                    modifier = Modifier.padding(16.dp),
                                    tint = deepNavy
                                )
                            }

                            Spacer(Modifier.height(16.dp))

                            Text(
                                text = expense!!.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = deepNavy
                            )

                            Text(
                                text = "₹${expense!!.amount}",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = deepNavy
                            )

                            if (expense!!.description.isNotBlank()) {
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = expense!!.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = steelBlue,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, steelBlue.copy(alpha = 0.1f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = creamyYellow,
                                shape = CircleShape,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = deepNavy,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Paid by",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = steelBlue,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = expense!!.paidByName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = deepNavy
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    Text(
                        text = "Split Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = deepNavy
                    )

                    Spacer(Modifier.height(12.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(participantAmounts.toList()) { (name, amount) ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    steelBlue.copy(alpha = 0.1f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(creamyYellow),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = name.take(1).uppercase(),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = deepNavy
                                            )
                                        }
                                        Spacer(Modifier.width(12.dp))
                                        Text(
                                            text = name,
                                            fontWeight = FontWeight.Bold,
                                            color = deepNavy
                                        )
                                    }
                                    Text(
                                        text = "₹$amount",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 18.sp,
                                        color = deepNavy
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
