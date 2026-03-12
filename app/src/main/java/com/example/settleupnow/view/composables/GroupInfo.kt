package com.example.settleupnow.view.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.settleupnow.viewmodel.GroupInfoViewModel
@Composable
fun GroupInfoScreen(groupId: String, viewModel: GroupInfoViewModel = viewModel()) {
    val members by viewModel.members.collectAsState()

    LaunchedEffect(groupId) {
        viewModel.fetchMembers(groupId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Group Members", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        if (members.isEmpty()) {
            Text("No members yet", color = Color.Gray)
        } else {
            LazyColumn {
                items(members) { member ->
                    Text("${member.name} (${member.email})", fontSize = 16.sp)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}
