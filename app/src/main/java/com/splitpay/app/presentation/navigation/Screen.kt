package com.splitpay.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Groups : Screen("groups")
    data object CreateGroup : Screen("create_group")
    data object GroupDetail : Screen("group_detail/{groupId}") {
        fun createRoute(groupId: String) = "group_detail/$groupId"
    }
    data object AddExpense : Screen("add_expense/{groupId}") {
        fun createRoute(groupId: String) = "add_expense/$groupId"
    }
    data object Balances : Screen("balances/{groupId}") {
        fun createRoute(groupId: String) = "balances/$groupId"
    }
}
