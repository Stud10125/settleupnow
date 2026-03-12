package com.example.settleupnow.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.settleupnow.view.composables.*
import com.example.settleupnow.viewmodel.AddExpencesViewModel
import com.example.settleupnow.viewmodel.AddGroupViewModel

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

}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val addExpencesViewModel : AddExpencesViewModel = viewModel()
    val addGroupViewModel: AddGroupViewModel = viewModel()



    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreenUI(
                navController = navController
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreenUI(
                navController = navController
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                navController = navController
            )
        }

        composable(Routes.GROUP_DETAILS) {
            GroupDetailScreen(
                navController = navController
            )
        }

        composable(Routes.ADD_EXPENSE) {
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

        composable(Routes.GROUP_DATA) {
            GroupDetailScreen(
                navController = navController
            )
        }

        composable(Routes.GROUP_INFO) {
            GroupInfo(
                navController = navController
            )
        }

        composable(Routes.SUMMARY) {
            SummaryScreen(
                navController = navController
            )
        }

        composable(Routes.EDIT_EXPENSE) {
            EditExpenseScreen(
                navController = navController
            )
        }

        composable(Routes.EXPENSE_INFO) {
            ExpenseInfoScreen(
                navController = navController
            )
        }

    }
}
