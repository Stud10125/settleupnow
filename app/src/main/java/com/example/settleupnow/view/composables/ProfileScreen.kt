package com.example.settleupnow.view.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.settleupnow.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onAccountClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onSupportClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium)

        ProfileOption("Account") { onAccountClick() }
        ProfileOption("Change Password") { onChangePasswordClick() }
        ProfileOption("Support") { onSupportClick() }
        ProfileOption("About") { onAboutClick() }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = {
                viewModel.logout {
                    onLogout()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout", color = Color.White)
        }
    }
}

@Composable
fun ProfileOption(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Start
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun AccountScreen(navController: NavController) {
    Column(Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        Text("Account Screen", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun ChangePasswordScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text("Change Password", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {  },
            placeholder = { Text("Enter email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = "",
            onValueChange = {  },
            placeholder = { Text("Enter new password") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = "",
            onValueChange = { },
            placeholder = { Text("Confirm password") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {  },
            modifier = Modifier.width(170.dp),
        ) {
            Text("Change Password")
        }
    }
}

@Composable
fun SupportScreen(navController: NavController) {
    Column(Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        Text("Support Screen", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun AboutScreen(
    navController: NavController,
    developerName: String = "Your Team",
    onPrivacyPolicyClick: () -> Unit,
    onTermsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        Text("About", style = MaterialTheme.typography.headlineMedium)

        Text("SettleUpNow", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(10.dp))

        Text("From trips to daily life, settle up in seconds.")

        Text(
            "SettleUpNow helps you share expenses in groups. "
                    + "Add expenses, split them among members, and instantly see who owes whom.",
            style = MaterialTheme.typography.bodyLarge
        )

        Text("Developed by $developerName", style = MaterialTheme.typography.bodyMedium)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPrivacyPolicyClick() }
                .padding(vertical = 8.dp)
        ) {
            Text("Privacy Policy", style = MaterialTheme.typography.bodyLarge)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onTermsClick() }
                .padding(vertical = 8.dp)
        ) {
            Text("Terms of Service", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
