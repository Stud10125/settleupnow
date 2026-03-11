package com.example.settleupnow.view.composables

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.settleupnow.view.composables.ui.theme.SettleUpNowTheme
import com.example.settleupnow.viewmodel.MemberActivityViewModel

class MemberActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val groupName = intent.getStringExtra("groupName") ?: "Group"

        setContent {
            SettleUpNowTheme {
                val vm: MemberActivityViewModel = viewModel()
                val members by vm.members.collectAsState()

                MemberScreen(
                    title = groupName,
                    members = members,
                    onBack = { finish() }
                )
            }
        }
    }
}

@Composable
private fun MemberScreen(
    title: String,
    members: List<String>,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))


            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF5F5F5)
            ) {
                if (members.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No members yet", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp)
                    ) {
                        items(members, key = { it }) { name ->
                            MemberRow(name = name)
                            Divider(color = Color(0xFFE0E0E0))
                        }

                    }

                }
            }

            OutlinedButton(
                onClick = onBack,

                ) { Text("Back") }
        }
    }
}

@Composable
private fun MemberRow(name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFE3F2FD)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.firstOrNull()?.uppercase() ?: "?",
                color = Color(0xFF0D47A1),
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}