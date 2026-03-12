package com.example.settleupnow.view.composables

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.settleupnow.view.composables.ui.theme.SettleUpNowTheme
import com.example.settleupnow.viewmodel.GroupDetailUiState
import com.example.settleupnow.viewmodel.GroupDetailViewModel
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavController
import com.example.settleupnow.viewmodel.MemberActivityViewModel


@Composable
fun GroupDetailScreen(
    navController: NavController,
    vm: GroupDetailViewModel,
    onBack: () -> Unit
) {
    val state by vm.uiState.collectAsState()
    var showBalance by remember { mutableStateOf(false) }

    BackHandler { onBack() }

    Scaffold { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "(Inside group screen)\n${state.groupName}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.expenses) { e ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(e.title)
                        Text(e.amount.toString())
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.navigate(Routes.ADD_EXPENSE) },
                    modifier = Modifier.weight(1f)
                ) { Text("Add Exp") }

                OutlinedButton(
                    onClick = { showBalance = !showBalance },
                    modifier = Modifier.weight(1f)
                ) { Text("Balance") }
            }

            if (showBalance) {
                val sum = state.expenses.sumOf { it.amount }
                Text("Total Balance: ₹$sum")
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Back") }
        }
    }
}
