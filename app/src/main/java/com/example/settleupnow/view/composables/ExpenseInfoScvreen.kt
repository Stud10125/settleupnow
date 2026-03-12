package com.example.settleupnow.view.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.settleupnow.navigation.Routes

@Composable
fun ExpenseInfoScreen(navController: NavHostController) {

    val items = listOf("Member 1", "Member 2", "Member 3", "Member 4", "Member 5")
    val money = listOf("₹0", "₹0", "₹0", "₹0", "₹0")

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(onClick = { navController.popBackStack() }) {
                    Text("<-")
                }


                Text(
                    text = "Expense Info",
                    modifier = Modifier
                        .weight(1f) // take the center space
                        .padding(horizontal = 12.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                // Right-side spacer to balance the back button width visually
                Spacer(modifier = Modifier.width(48.dp))
            }


            // Members & amounts
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp)
            ) {
                itemsIndexed(items) { index, member ->
                    val amount = money.getOrNull(index) ?: "₹0"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = member)
                        Text(text = amount)
                    }
                }
            }


            Button(
                onClick = { navController.navigate(Routes.EDIT_EXPENSE) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Edit")
            }
        }
    }
}