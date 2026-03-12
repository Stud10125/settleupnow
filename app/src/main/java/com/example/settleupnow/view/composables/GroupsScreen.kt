package com.example.settleupnow.view.composables


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.settleupnow.navigation.Routes

//import com.example.ag.AddGroupViewModel

@Composable
fun GroupsScreen(
    navController: NavHostController
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Groups",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

//            if (groups.isEmpty()) {
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .fillMaxWidth(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "No groups yet.\nTap \"Add Group\" to create one.",
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//                }
//            } else {
//                  }
            LazyColumn {
                items(10) { group ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .background(
                                color = Color(0xFFE3F2FD),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { navController.navigate(Routes.GROUP_DATA) }
                            .padding(16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text("Group Name", fontSize = 18.sp)
                    }
                }
            }
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { navController.navigate(Routes.ADD_GROUP) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add Group")
            }
        }
    }
}