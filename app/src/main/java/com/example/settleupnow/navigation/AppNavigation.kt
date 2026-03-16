package com.example.settleupnow.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.settleupnow.view.composables.*
import com.example.settleupnow.viewmodel.*
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val ADD_GROUP = "add_group"
    const val GROUP_DATA = "group_data"
    const val GROUP_DETAILS = "group_details"
    const val ADD_EXPENSE = "add_expense"
    const val GROUP_INFO = "group_info"
    const val SUMMARY = "summary"
    const val EDIT_EXPENSE = "edit_expense"
    const val EXPENSE_INFO = "expense_info"
    const val ABOUT = "about"
}

@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel = viewModel(),
    addGroupViewModel: AddGroupViewModel = viewModel(),
    registerViewModel: RegisterViewModel = viewModel(),
    addExpencesViewModel: AddExpencesViewModel = viewModel(),
    summaryViewModel: SummaryViewModel = viewModel()
) {
    val navController = rememberNavController()
    val auth = Firebase.auth
    val startDestination = if (auth.currentUser != null) Routes.HOME else Routes.LOGIN

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) {
            LoginScreenUI(
                navController = navController,
                onRegister = { navController.navigate(Routes.REGISTER) },
                viewModel = loginViewModel
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreenUI(
                viewModel = registerViewModel,
                onLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }

        composable("${Routes.ADD_EXPENSE}/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            addExpencesViewModel.setGroupId(groupId)
            AddExpencesScreen(
                navController = navController,
                viewModel = addExpencesViewModel
            )
        }

        composable(Routes.ADD_GROUP) {
            AddGroupScreen(
                navController = navController,
                viewModel = addGroupViewModel
            )
        }

        composable("${Routes.GROUP_DATA}/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            GroupDetailScreen(navController, groupId)
        }

        composable("${Routes.GROUP_INFO}/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            GroupInfoScreen(groupId = groupId, navController = navController)
        }

        composable("${Routes.EXPENSE_INFO}/{expenseId}") { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getString("expenseId") ?: ""
            ExpenseInfoScreen(expenseId = expenseId, navController = navController)
        }

        composable("${Routes.SUMMARY}/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            SummaryScreen(groupId = groupId, navController = navController, viewModel = summaryViewModel)
        }

        composable("${Routes.EDIT_EXPENSE}/{expenseId}") { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getString("expenseId") ?: ""
            EditExpenseScreen(expenseId = expenseId, navController = navController)
        }

        composable(Routes.ABOUT) {
            AboutScreen(
                navController = navController
            )
        }
    }
}
