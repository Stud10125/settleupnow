package com.example.settleupnow.view.composables

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.settleupnow.view.composables.ui.theme.SettleUpNowTheme
import com.example.settleupnow.viewmodel.AddGroupViewModel

class AddGroupScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SettleUpNowTheme {
                val viewModel: AddGroupViewModel = viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AddGroupScreenUI(
                        viewModel = viewModel,
                        onCreateSuccess = {},
                        onCancel = { }
                        )
                }
            }
        }
    }
}


@Composable
fun AddGroupScreenUI(
    viewModel: AddGroupViewModel,
    onCreateSuccess: () -> Unit,
    onCancel: () -> Unit,
) {
    val groupName by viewModel.groupName.collectAsState()
    val groupDescription by viewModel.groupDescription.collectAsState()
    val groupMembers by viewModel.groupMembers.collectAsState()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Add Group", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = groupName,
            onValueChange = { viewModel.onGroupNameChanged(it) },
            label = { Text("Group Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = groupDescription,
            onValueChange = { viewModel.onGroupDescriptionChanged(it) },
            label = { Text("Group Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Row (modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween){

            OutlinedTextField(
                value = groupMembers,
                onValueChange = { viewModel.onGroupMembers(it) },
                label = { Text("Group Members") },
                modifier = Modifier.width(240.dp)
            )

            Button(
                onClick = {  },
                modifier = Modifier.width(100.dp)
            ) { Text("Add") }
        }


        Spacer(modifier = Modifier.height(460.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.createGroup(onCreateSuccess) },
//                enabled = groupName.isNotBlank() && groupDescription.isNotBlank()
            ) {
                Text("Create")
            }


            OutlinedButton(onClick = { viewModel.cancel(onCancel) }) { Text("Cancel") }
        }
    }
}

