package com.splitpay.app.domain.usecase

import com.splitpay.app.domain.model.Expense
import com.splitpay.app.domain.model.Split
import com.splitpay.app.domain.model.SplitType
import com.splitpay.app.domain.repository.ExpenseRepository
import com.splitpay.app.domain.repository.GroupRepository
import kotlinx.coroutines.flow.first

class AddExpenseUseCase(
    private val expenseRepository: ExpenseRepository,
    private val groupRepository: GroupRepository
) {

    suspend operator fun invoke(expense: Expense) {
        require(expense.amount > 0) { "Amount must be positive" }
        require(expense.description.isNotBlank()) { "Description cannot be empty" }

        val group = groupRepository.getGroupById(expense.groupId).first()
            ?: throw IllegalArgumentException("Group not found")

        val finalExpense = when (expense.splitType) {
            SplitType.EQUAL -> {
                val splitAmount = expense.amount / group.members.size
                expense.copy(
                    splits = group.members.map { Split(it.id, splitAmount) }
                )
            }
            SplitType.CUSTOM -> {
                require(expense.splits.isNotEmpty()) { "Custom split must specify amounts" }
                val totalSplit = expense.splits.sumOf { it.amount }
                require(kotlin.math.abs(totalSplit - expense.amount) < 0.01) {
                    "Split amounts must equal the total expense"
                }
                expense
            }
        }

        expenseRepository.addExpense(finalExpense)
    }
}
