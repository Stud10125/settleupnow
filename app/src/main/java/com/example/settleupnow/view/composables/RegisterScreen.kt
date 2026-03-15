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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.settleupnow.viewmodel.RegisterViewModel

@Composable
fun RegisterScreenUI(
    viewModel: RegisterViewModel,
    onLogin: () -> Unit
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val creamyYellow = Color(0xFFEEFABD)
    val sageGreen = Color(0xFFA0D585)
    val steelBlue = Color(0xFF6984A9)
    val deepNavy = Color(0xFF263B6A)

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validateInputs(): Boolean {
        var isValid = true
        
        if (viewModel.name.value.trim().isEmpty()) {
            nameError = "Name is required"
            isValid = false
        } else if (viewModel.name.value.trim().length < 3) {
            nameError = "Name too short"
            isValid = false
        } else {
            nameError = null
        }

        if (viewModel.email.value.trim().isEmpty()) {
            emailError = "Email is required"
            isValid = false
        } else if (!isEmailValid(viewModel.email.value.trim())) {
            emailError = "Invalid email format"
            isValid = false
        } else {
            emailError = null
        }

        if (viewModel.password.value.isEmpty()) {
            passwordError = "Password is required"
            isValid = false
        } else if (viewModel.password.value.length < 6) {
            passwordError = "At least 6 characters required"
            isValid = false
        } else {
            passwordError = null
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordError = "Please confirm password"
            isValid = false
        } else if (confirmPassword != viewModel.password.value) {
            confirmPasswordError = "Passwords do not match"
            isValid = false
        } else {
            confirmPasswordError = null
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
                    .size(80.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(deepNavy, steelBlue)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp),
                    tint = Color.White
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = deepNavy
            )

            Text(
                "Start your journey with us",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = deepNavy.copy(alpha = 0.7f)
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = viewModel.name.value,
                onValueChange = { 
                    viewModel.name.value = it
                    if (nameError != null) nameError = null
                },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                isError = nameError != null,
                supportingText = { nameError?.let { Text(it) } },
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
                value = viewModel.email.value,
                onValueChange = { 
                    viewModel.email.value = it
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
                value = viewModel.password.value,
                onValueChange = { 
                    viewModel.password.value = it 
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

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { 
                    confirmPassword = it 
                    if (confirmPasswordError != null) confirmPasswordError = null
                },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle password", tint = steelBlue)
                    }
                },
                isError = confirmPasswordError != null,
                supportingText = { confirmPasswordError?.let { Text(it) } },
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
                        viewModel.signUp { success, message ->
                            if (success) {
                                onLogin()
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
                Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            TextButton(onClick = onLogin) {
                Text(
                    text = buildAnnotatedString {
                        append("Already have an account? ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, color = deepNavy)) {
                            append("Sign In")
                        }
                    },
                    color = deepNavy.copy(alpha = 0.8f)
                )
            }
        }
    }
}
