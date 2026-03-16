package com.example.settleupnow.view.composables

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.settleupnow.viewmodel.AddExpencesViewModel

@Composable
fun AddExpencesScreen(
    viewModel: AddExpencesViewModel,
    navController: NavHostController,
) {
    val expenceTitle by viewModel.expenceTitle.collectAsState()
    val description by viewModel.description.collectAsState()
    val splitType by viewModel.splitType.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val paidBy by viewModel.paidBy.collectAsState()
    val checkedList by viewModel.checkedList.collectAsState()
    val expencesList by viewModel.expencesList.collectAsState()
    val total by viewModel.total.collectAsState()
    val members by viewModel.members.collectAsState()

    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    val creamyYellow = Color(0xFFEEFABD)
    val sageGreen = Color(0xFFA0D585)
    val steelBlue = Color(0xFF6984A9)
    val deepNavy = Color(0xFF263B6A)

    val hasInvalidUnequalInput = splitType == "Unequal" && expencesList.any { str ->
        str.any { char -> !char.isDigit() && char != '+' && !char.isWhitespace() }
    }
    val isTitleValid = expenceTitle.isNotBlank()
    val amountValue = amount.toDoubleOrNull() ?: 0.0
    val isAmountValid =
        if (splitType == "Equal") amountValue > 0 else (total > 0 && !hasInvalidUnequalInput)
    val isFormValid = isTitleValid && isAmountValid

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
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
                        IconButton(onClick = {
                            viewModel.clearData()
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = deepNavy
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ADD EXPENSE",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = deepNavy
                        )
                    }
                }
            },
            bottomBar = {
                Column {
                    if (splitType.equals("Unequal")) {
                        Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 1.dp)) {
                            Text(
                                "Enter amount for each *",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = deepNavy
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val isTotalInvalid = total <= 0
                            val hasInteractedWithUnequal = expencesList.any { it.isNotEmpty() }
                            val showError =
                                (isTotalInvalid || hasInvalidUnequalInput) && hasInteractedWithUnequal

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (showError) Color(0xFFFFEBEE) else creamyYellow.copy(
                                        alpha = 0.5f
                                    )
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (showError) Color.Red.copy(alpha = 0.3f) else deepNavy.copy(
                                        alpha = 0.2f
                                    )
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            "Total Calculated",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = deepNavy
                                        )
                                        if (showError) {
                                            Text(
                                                text = if (hasInvalidUnequalInput) "Invalid input!" else "Total must be > 0",
                                                color = Color.Red,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                    Text(
                                        "₹ $total",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = deepNavy
                                    )
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .navigationBarsPadding()
                    ) {
                        Button(
                            onClick = {
                                viewModel.saveExpense { success, message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    if (success) {
                                        viewModel.clearData()
                                        navController.popBackStack()
                                    }
                                }
                            },
                            enabled = isFormValid,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = deepNavy,
                                disabledContainerColor = deepNavy.copy(alpha = 0.5f)
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                "Save Expense",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    Spacer(Modifier.height(16.dp))
                    
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        border = BorderStroke(2.dp, sageGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            OutlinedTextField(
                                value = expenceTitle,
                                onValueChange = { viewModel.expenceTitle(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                label = { Text("What is this for? *") },
                                placeholder = { Text("e.g. Dinner, Movie") },
                                leadingIcon = { Icon(Icons.Default.Title, null, tint = deepNavy) },
                                shape = RoundedCornerShape(16.dp),
                                isError = expenceTitle.isNotEmpty() && !isTitleValid,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = deepNavy,
                                    unfocusedBorderColor = steelBlue.copy(alpha = 0.5f),
                                    focusedLabelColor = deepNavy,
                                    unfocusedLabelColor = steelBlue
                                )
                            )
                            if (expenceTitle.isNotEmpty() && !isTitleValid) {
                                Text("Title cannot be empty", color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = description,
                                onValueChange = { viewModel.description(it) },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Description") },
                                placeholder = { Text("Optional details") },
                                leadingIcon = { Icon(Icons.Default.Description, null, tint = deepNavy) },
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = deepNavy,
                                    unfocusedBorderColor = steelBlue.copy(alpha = 0.5f),
                                    focusedLabelColor = deepNavy,
                                    unfocusedLabelColor = steelBlue
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Payment Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = deepNavy)
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = paidBy,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Paid By") },
                            leadingIcon = { Icon(Icons.Default.Payments, null, tint = deepNavy) },
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = deepNavy,
                                unfocusedBorderColor = steelBlue.copy(alpha = 0.5f),
                                focusedLabelColor = deepNavy,
                                unfocusedLabelColor = steelBlue
                            )
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { expanded = !expanded }
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color.White).fillMaxWidth(0.8f)
                        ) {
                            members.forEach { member ->
                                DropdownMenuItem(
                                    text = { Text(member.name, color = deepNavy) },
                                    onClick = {
                                        viewModel.paidBy(member.name)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("How to split?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = deepNavy)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = splitType == "Equal",
                            onClick = { viewModel.splitType("Equal") },
                            colors = RadioButtonDefaults.colors(selectedColor = deepNavy)
                        )
                        Text("Equally", color = deepNavy)
                        Spacer(modifier = Modifier.width(20.dp))
                        RadioButton(
                            selected = splitType == "Unequal",
                            onClick = { viewModel.splitType("Unequal") },
                            colors = RadioButtonDefaults.colors(selectedColor = deepNavy)
                        )
                        Text("Unequally", color = deepNavy)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (splitType == "Equal") {
                    item {
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { viewModel._amount(it) },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Total Amount *") },
                            prefix = { Text("₹ ", color = deepNavy, fontWeight = FontWeight.Bold) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = deepNavy,
                                unfocusedBorderColor = steelBlue.copy(alpha = 0.5f),
                                focusedLabelColor = deepNavy,
                                unfocusedLabelColor = steelBlue
                            )
                        )
                        if (amount.isNotEmpty() && amountValue <= 0) {
                            Text("Amount must be greater than 0", color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Select Members", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = deepNavy)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    itemsIndexed(members) { index, member ->
                        val isChecked = checkedList.getOrElse(index) { false }
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isChecked) creamyYellow.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.7f)
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isChecked) sageGreen else steelBlue.copy(alpha = 0.1f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { viewModel.checkedList(index, !isChecked) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { viewModel.checkedList(index, it) },
                                    colors = CheckboxDefaults.colors(checkedColor = deepNavy)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(member.name, fontWeight = FontWeight.Medium, color = deepNavy)
                            }
                        }
                    }
                } else {
                    itemsIndexed(members) { index, member ->
                        val memberAmount = expencesList.getOrElse(index) { "" }
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, steelBlue.copy(alpha = 0.1f)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(member.name, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium, color = deepNavy)
                                OutlinedTextField(
                                    value = memberAmount,
                                    onValueChange = { viewModel.expencesList(index, it) },
                                    modifier = Modifier.width(120.dp),
                                    prefix = { Text("₹ ", color = deepNavy) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = deepNavy,
                                        unfocusedBorderColor = steelBlue.copy(alpha = 0.5f),
                                        focusedLabelColor = deepNavy,
                                        unfocusedLabelColor = steelBlue
                                    )
                                )
                            }
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(100.dp)) 
                }
            }
        }
    }
}
