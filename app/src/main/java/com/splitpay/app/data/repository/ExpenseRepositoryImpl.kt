package com.splitpay.app.data.repository

import com.splitpay.app.data.local.MockCollection
import com.splitpay.app.domain.model.Expense
import com.splitpay.app.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class ExpenseRepositoryImpl(
    private val collection: MockCollection<Expense>
) : ExpenseRepository {

    override fun getExpensesForGroup(groupId: String): Flow<List<Expense>> =
        collection.query { it.groupId == groupId }

    override fun getExpenseById(expenseId: String): Flow<Expense?> =
        collection.getById(expenseId)

    override suspend fun addExpense(expense: Expense) {
        collection.put(expense)
    }

    override suspend fun deleteExpense(expenseId: String) {
        collection.delete(expenseId)
    }
}
