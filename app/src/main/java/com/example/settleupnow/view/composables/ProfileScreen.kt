package com.example.settleupnow.view.composables

import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.settleupnow.view.composables.ui.theme.SettleUpNowTheme
import com.example.settleupnow.viewmodel.ProfileViewModel

class ProfileScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SettleUpNowTheme {
                val viewModel: ProfileViewModel = viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation()
                }
            }
        }
    }
}


@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onAccountClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onSupportClick: () -> Unit,
    onAboutClick: () -> Unit
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
fun AccountScreen() {
    Column(Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
        Text("Account Screen", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun ChangePasswordScreen(viewModel: ProfileViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Change Password", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            placeholder = { Text("Enter email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.newPassword,
            onValueChange = { viewModel.onNewPasswordChanged(it) },
            placeholder = { Text("Enter new password") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChanged(it) },
            placeholder = { Text("Confirm password") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.changePassword() },
            modifier = Modifier.width(170.dp),
//            enabled = viewModel.email.isNotBlank() &&
//                    viewModel.newPassword.isNotBlank() &&
//                    viewModel.confirmPassword.isNotBlank()
        ) {
            Text("Change Password")
        }
    }
}


@Composable
fun SupportScreen() {
    Column(Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
        Text("Support Screen", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun AboutScreen(
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


@Composable
fun AppNavigation(){
    val navController= rememberNavController()
    NavHost(navController = navController, startDestination = "profile") {
        composable("profile") {
            ProfileScreen(
                viewModel = viewModel(),
                onAccountClick = { navController.navigate("account") },
                onChangePasswordClick = { navController.navigate("changePassword") },
                onSupportClick = { navController.navigate("support") },
                onAboutClick = { navController.navigate("about") }
            )
        }

        composable("account") { AccountScreen() }
        composable("changePassword") { ChangePasswordScreen() }
        composable("support") { SupportScreen() }
        composable("about") {
            AboutScreen(
                developerName = "SettleUpNow Team",
                onPrivacyPolicyClick = { },
                onTermsClick = { }
            )
        }
    }

}