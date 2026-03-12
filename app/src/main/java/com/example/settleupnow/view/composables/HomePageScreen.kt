package com.example.settleupnow.view.composables


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import com.example.ag.AddGroupViewModel
import com.example.settleupnow.view.composables.ui.theme.SettleUpNowTheme
import com.example.settleupnow.viewmodel.AddGroupViewModel


@Composable
fun HomeScreen(
    navController: NavController,
    groups: List<String>,
    onAddClick: () -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Groups",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            if (groups.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No groups yet.\nTap \"Add Group\" to create one.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn {
                    items(groups) { group ->
                        GroupCard(
                            name = group,
                            onClick = { navController.navigate("group_details/$group") }
                        )
                    }
                }
                Button(onClick = onAddClick) { Text("Add Group") }
            }

            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Group")
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEAEAEA))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                BottomButton("Groups/Home")
                BottomButton("Activity")
                BottomButton("Analytics")
                BottomButton("Profile")
            }
        }
    }
}

@Composable
fun AppWithNav(
    navController: NavController,
    vm: AddGroupViewModel
) {
    val groups by vm.groups.collectAsState()

    HomeScreen(
        navController = navController,
        groups = groups,
        onAddClick = { navController.navigate(Routes.ADD_GROUP) }
    )
}


@Composable
private fun AddGroupScreen(
    onAdd: (String) -> Unit,
    onCancel: () -> Unit
) {
    var groupName by remember { mutableStateOf("") }

    BackHandler { onCancel() }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Add Group",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Enter group name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { onAdd(groupName.trim()) },
                enabled = groupName.trim().isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add")
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    }
}


@Composable
private fun GroupCard(
    name: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(
                color = Color(0xFFE3F2FD),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(name, fontSize = 18.sp)
    }
}

@Composable
private fun BottomButton(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        modifier = Modifier.clickable { /* no-op */ }
    )
}
