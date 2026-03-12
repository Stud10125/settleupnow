package com.example.settleupnow.view.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.settleupnow.navigation.Routes
import com.example.settleupnow.viewmodel.ExpenseInfoViewModel

@Composable
fun ExpenseInfoScreen(
    navController: NavHostController,
    viewModel: ExpenseInfoViewModel = viewModel()
) {
    val expense by viewModel.expense.collectAsState()

    Scaffold(
        topBar = {
//            Row(
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                }
//                title = { Text("Expense Info", fontWeight = FontWeight.Bold) },
//            )
        }
    ) { padding ->
        expense?.let { exp ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(text = exp.title, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Text(text = "₹${exp.amount}", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
                Text(text = "Paid by ${exp.paidBy}", fontSize = 16.sp, color = MaterialTheme.colorScheme.secondary)

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Split details", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 8.dp)
                ) {
                    items(exp.splits.toList()) { (member, amount) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = member)
                            Text(text = "₹$amount")
                        }
                        HorizontalDivider(thickness = 0.5.dp)
                    }
                }

                Button(
                    onClick = { navController.navigate(Routes.EDIT_EXPENSE) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Edit")
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
