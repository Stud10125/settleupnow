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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun EditExpenseScreen(navController: NavHostController) {

    val members = listOf("Member 1", "Member 2", "Member 3", "Member 4", "Member 5")

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(text = "Members - Cost", modifier = Modifier.padding(bottom = 8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(members) { name ->
                    Row(modifier = Modifier.padding(vertical = 6.dp)) {
                        Text(text = name)
                        Spacer(modifier = Modifier.weight(1f))
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text("Cost") },
                            modifier = Modifier.width(120.dp)
                        )
                    }
                }
            }

            Text("Total : 0", modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { navController.popBackStack() }) {
                    Text("Cancel")
                }
                Button(onClick = { navController.popBackStack() }) {
                    Text("Save")
                }
            }
        }
    }
}