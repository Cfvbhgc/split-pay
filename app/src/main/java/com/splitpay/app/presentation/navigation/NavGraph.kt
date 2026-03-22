package com.splitpay.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.splitpay.app.di.ServiceLocator
import com.splitpay.app.presentation.screens.addexpense.AddExpenseScreen
import com.splitpay.app.presentation.screens.addexpense.AddExpenseViewModel
import com.splitpay.app.presentation.screens.balances.BalancesScreen
import com.splitpay.app.presentation.screens.balances.BalancesViewModel
import com.splitpay.app.presentation.screens.creategroup.CreateGroupScreen
import com.splitpay.app.presentation.screens.creategroup.CreateGroupViewModel
import com.splitpay.app.presentation.screens.groupdetail.GroupDetailScreen
import com.splitpay.app.presentation.screens.groupdetail.GroupDetailViewModel
import com.splitpay.app.presentation.screens.groups.GroupsScreen
import com.splitpay.app.presentation.screens.groups.GroupsViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Groups.route) {

        composable(Screen.Groups.route) {
            GroupsScreen(
                viewModel = GroupsViewModel(ServiceLocator.groupRepository),
                onGroupClick = { groupId ->
                    navController.navigate(Screen.GroupDetail.createRoute(groupId))
                },
                onCreateGroupClick = {
                    navController.navigate(Screen.CreateGroup.route)
                }
            )
        }

        composable(Screen.CreateGroup.route) {
            CreateGroupScreen(
                viewModel = CreateGroupViewModel(ServiceLocator.createGroupUseCase),
                onGroupCreated = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.GroupDetail.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            GroupDetailScreen(
                viewModel = GroupDetailViewModel(
                    groupId = groupId,
                    groupRepository = ServiceLocator.groupRepository,
                    expenseRepository = ServiceLocator.expenseRepository,
                    getGroupBalancesUseCase = ServiceLocator.getGroupBalancesUseCase
                ),
                onAddExpenseClick = {
                    navController.navigate(Screen.AddExpense.createRoute(groupId))
                },
                onViewBalancesClick = {
                    navController.navigate(Screen.Balances.createRoute(groupId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddExpense.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            AddExpenseScreen(
                viewModel = AddExpenseViewModel(
                    groupId = groupId,
                    groupRepository = ServiceLocator.groupRepository,
                    addExpenseUseCase = ServiceLocator.addExpenseUseCase
                ),
                onExpenseAdded = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Balances.route,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            BalancesScreen(
                viewModel = BalancesViewModel(
                    groupId = groupId,
                    groupRepository = ServiceLocator.groupRepository,
                    expenseRepository = ServiceLocator.expenseRepository,
                    getGroupBalancesUseCase = ServiceLocator.getGroupBalancesUseCase,
                    calculateDebtsUseCase = ServiceLocator.calculateDebtsUseCase,
                    settleDebtUseCase = ServiceLocator.settleDebtUseCase
                ),
                onBack = { navController.popBackStack() }
            )
        }
    }
}
