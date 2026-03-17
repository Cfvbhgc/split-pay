package com.splitpay.app.domain.repository

import com.splitpay.app.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpensesForGroup(groupId: String): Flow<List<Expense>>
    fun getExpenseById(expenseId: String): Flow<Expense?>
    suspend fun addExpense(expense: Expense)
    suspend fun deleteExpense(expenseId: String)
}
