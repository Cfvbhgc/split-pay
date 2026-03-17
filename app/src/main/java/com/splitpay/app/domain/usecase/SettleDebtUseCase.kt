package com.splitpay.app.domain.usecase

import com.splitpay.app.domain.model.DebtSettlement
import com.splitpay.app.domain.model.Expense
import com.splitpay.app.domain.model.Split
import com.splitpay.app.domain.model.SplitType
import com.splitpay.app.domain.repository.ExpenseRepository

class SettleDebtUseCase(private val expenseRepository: ExpenseRepository) {

    suspend operator fun invoke(groupId: String, settlement: DebtSettlement) {
        val expense = Expense(
            groupId = groupId,
            amount = settlement.amount,
            description = "Settlement: ${settlement.fromMemberName} -> ${settlement.toMemberName}",
            paidBy = settlement.fromMemberId,
            splitType = SplitType.CUSTOM,
            splits = listOf(Split(settlement.toMemberId, settlement.amount)),
            isSettlement = true
        )
        expenseRepository.addExpense(expense)
    }
}
