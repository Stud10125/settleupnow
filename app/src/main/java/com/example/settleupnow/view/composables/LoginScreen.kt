package com.example.settleupnow.view.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.settleupnow.navigation.Routes
import com.example.settleupnow.viewmodel.LoginViewModel

@Composable
fun LoginScreenUI(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    
    // Validation states
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    // Color Palette
    val creamyYellow = Color(0xFFEEFABD)
    val sageGreen = Color(0xFFA0D585)
    val steelBlue = Color(0xFF6984A9)
    val deepNavy = Color(0xFF263B6A)

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validateInputs(): Boolean {
        var isValid = true
        
        if (email.trim().isEmpty()) {
            emailError = "Email is required"
            isValid = false
        } else if (!isEmailValid(email.trim())) {
            emailError = "Invalid email format"
            isValid = false
        } else {
            emailError = null
        }

        if (password.isEmpty()) {
            passwordError = "Password is required"
            isValid = false
        } else {
            passwordError = null
        }

        return isValid
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(deepNavy, steelBlue)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Logo",
                    modifier = Modifier.size(50.dp),
                    tint = Color.White
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = buildAnnotatedString {
                    append("SettleUp")
                    withStyle(style = SpanStyle(color = deepNavy)) {
                        append("Now")
                    }
                },
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = deepNavy,
                letterSpacing = (-1).sp
            )

            Text(
                "Split smart. Settle fast.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = deepNavy.copy(alpha = 0.7f)
            )

            Spacer(Modifier.height(48.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { 
                    viewModel.updateEmail(it)
                    if (emailError != null) emailError = null
                },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = deepNavy,
                    unfocusedBorderColor = steelBlue.copy(alpha = 0.5f),
                    focusedLabelColor = deepNavy,
                    unfocusedLabelColor = steelBlue,
                    cursorColor = deepNavy,
                    focusedTextColor = deepNavy,
                    unfocusedTextColor = deepNavy,
                    errorSupportingTextColor = Color.Red
                )
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { 
                    viewModel.updatePassword(it)
                    if (passwordError != null) passwordError = null
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle password", tint = steelBlue)
                    }
                },
                isError = passwordError != null,
                supportingText = { passwordError?.let { Text(it) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = deepNavy,
                    unfocusedBorderColor = steelBlue.copy(alpha = 0.5f),
                    focusedLabelColor = deepNavy,
                    unfocusedLabelColor = steelBlue,
                    cursorColor = deepNavy,
                    focusedTextColor = deepNavy,
                    unfocusedTextColor = deepNavy,
                    errorSupportingTextColor = Color.Red
                )
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    if (validateInputs()) {
                        viewModel.loginUser(email, password) { success, message ->
                            if (success) {
                                navController.navigate(Routes.HOME) {
                                    popUpTo(Routes.LOGIN) { inclusive = true }
                                }
                                viewModel.clear()
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = deepNavy,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("Sign In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            TextButton(onClick = {
                navController.navigate(Routes.REGISTER)
                viewModel.clear()
            }) {
                Text(
                    text = buildAnnotatedString {
                        append("New user? ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, color = deepNavy)) {
                            append("Create Account")
                        }
                    },
                    color = deepNavy.copy(alpha = 0.8f)
                )
            }
        }
    }
}
