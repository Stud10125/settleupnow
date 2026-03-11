package com.example.settleupnow.view.composables

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.settleupnow.R

import com.example.settleupnow.view.composables.ui.theme.SettleUpNowTheme
import com.example.settleupnow.viewmodel.AddExpencesViewModel
import kotlin.compareTo

class AddExpenseScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SettleUpNowTheme {
                val viewModel: AddExpencesViewModel = viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AddExpencesScreen(viewModel = viewModel)
                }
            }
        }
    }
}


@Composable
fun AddExpencesScreen(
    viewModel: AddExpencesViewModel,
    modifier: Modifier = Modifier
) {
    val expenceTitle by viewModel.expenceTitle.collectAsState()
    val description by viewModel.description.collectAsState()
    val splitType by viewModel.splitType.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val paidBy by viewModel.paidBy.collectAsState()
    val checkedList by viewModel.checkedList.collectAsState()
    val expencesList by viewModel.expencesList.collectAsState()
    val total by viewModel.total.collectAsState()

    val hasInvalidUnequalInput = splitType == "Unequal" && expencesList.any { str ->
        str.any { char -> !char.isDigit() && char != '+' && !char.isWhitespace() }
    }
    val isTitleValid = expenceTitle.isNotBlank()
    val amountValue = amount.toDoubleOrNull() ?: 0.0
    val isAmountValid =
        if (splitType == "Equal") amountValue > 0 else (total > 0 && !hasInvalidUnequalInput)
    val isFormValid = isTitleValid && isAmountValid

    var expanded by remember { mutableStateOf(false) }
    val members = List(10) { "Member ${it + 1}" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Add New Expense",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Title Input
        Text(text = "What is this for? *", fontWeight = FontWeight.Bold)
        TextField(
            value = expenceTitle,
            onValueChange = { viewModel.expenceTitle(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Title") }
        )
        if (expenceTitle.isNotEmpty() && !isTitleValid) {
            Text("Title cannot be empty", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Description Input
        Text(text = "Add a description", fontWeight = FontWeight.Bold)
        TextField(
            value = description,
            onValueChange = { viewModel.description(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Optional description") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Paid By Dropdown
        Text(text = "Paid By", fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = paidBy,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            // This box sits on top to detect clicks for the dropdown
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = !expanded }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                members.forEach { member ->
                    DropdownMenuItem(
                        text = { Text(member) },
                        onClick = {
                            viewModel.paidBy(member)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Split Type
        Text(text = "How to split?", fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = splitType == "Equal",
                onClick = { viewModel.splitType("Equal") }
            )
            Text("Equally")
            Spacer(modifier = Modifier.width(20.dp))
            RadioButton(
                selected = splitType == "Unequal",
                onClick = { viewModel.splitType("Unequal") }
            )
            Text("Unequally")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (splitType == "Equal") {
            // Equal Split Input
            Text(text = "Total Amount *", fontWeight = FontWeight.Bold)
            TextField(
                value = amount,
                onValueChange = { viewModel._amount(it) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            if (amount.isNotEmpty() && amountValue <= 0) {
                Text("Amount must be greater than 0", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Select Members", fontWeight = FontWeight.Bold)

            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(checkedList) { index, isChecked ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { viewModel.checkedList(index, it) }
                        )
                        Text("Member ${index + 1}")
                    }
                }
            }
        } else {
            // Unequal Split Input
            Text(text = "Enter amount for each *", fontWeight = FontWeight.Bold)
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(expencesList) { index, memberAmount ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Member ${index + 1}", modifier = Modifier.weight(1f))
                        TextField(
                            value = memberAmount,
                            onValueChange = { viewModel.expencesList(index, it) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                    }
                }
            }

            // Summary box using basic Column and Background
            val isTotalInvalid = total <= 0
            val hasInteractedWithUnequal = expencesList.any { it.isNotEmpty() }
            val showError = (isTotalInvalid || hasInvalidUnequalInput) && hasInteractedWithUnequal

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(if (showError) Color(0xFFFFCCCC) else Color(0xFFEEEEEE))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Calculated:", fontWeight = FontWeight.Bold)
                    Text("$total", fontWeight = FontWeight.Bold)
                }
                if (showError) {
                    Text(
                        text = if (hasInvalidUnequalInput)
                            "Invalid! Use numbers and '+' only"
                        else "Total must be more than 0",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Validation Text
        if (!isFormValid) {
            Text(
                text = "Please fix the errors or fill required fields.",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Save Button
        Button(
            onClick = { /* Save logic */ },
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Save Expense")
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview7() {
//    SettleUpNowTheme {
//        Greeting7("Android")
//    }
//}