package com.example.settleupnow.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.LoginViewModel
import com.example.settleupnow.view.composables.*
import com.example.settleupnow.viewmodel.AddGroupViewModel
import com.example.settleupnow.viewmodel.RegisterViewModel
import com.example.settleupnow.viewmodel.GroupDetailViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val ADD_GROUP = "add_group"
    const val GROUP_DETAILS = "group_details/{groupName}"
    const val ADD_EXPENSE = "add_expense"
}

@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel = viewModel(),
    addGroupViewModel: AddGroupViewModel = viewModel(),
    registerViewModel: RegisterViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

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
            AppWithNav(navController = navController, vm = addGroupViewModel)
        }

        composable(Routes.ADD_GROUP) {
//            AddGroupScreen(
//                onAdd = { name ->
//                    addGroupViewModel.addGroup(name)
//                    navController.popBackStack()
//                },
//                onCancel = { navController.popBackStack() }
//            )
        }

        composable("group_details/{groupName}") { backStackEntry ->
            val groupName = backStackEntry.arguments?.getString("groupName") ?: "Group"
            val vm: GroupDetailViewModel = viewModel(
                factory = GroupDetailViewModel.Factory(groupName)
            )
            GroupDetailScreen(
                navController = navController,
                vm = vm,
                onBack = { navController.popBackStack() }
            )
        }


//        composable(Routes.ADD_EXPENSE) {
//            AddExpenseScreen(
//                onAdd = { title, amount ->
//                    addGroupViewModel.addExpense(title, amount)
//                    navController.popBackStack()
//                },
//                onCancel = { navController.popBackStack() }
//            )
//        }
    }
}
