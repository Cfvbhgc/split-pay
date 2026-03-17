package com.splitpay.app.domain.usecase

import com.splitpay.app.domain.model.Balance
import com.splitpay.app.domain.model.Expense
import com.splitpay.app.domain.model.Member

class GetGroupBalancesUseCase {

    operator fun invoke(expenses: List<Expense>, members: List<Member>): List<Balance> {
        val totalPaid = mutableMapOf<String, Double>()
        val totalOwed = mutableMapOf<String, Double>()

        for (member in members) {
            totalPaid[member.id] = 0.0
            totalOwed[member.id] = 0.0
        }

        for (expense in expenses) {
            totalPaid[expense.paidBy] =
                (totalPaid[expense.paidBy] ?: 0.0) + expense.amount

            for (split in expense.splits) {
                totalOwed[split.memberId] =
                    (totalOwed[split.memberId] ?: 0.0) + split.amount
            }
        }

        return members.map { member ->
            val paid = totalPaid[member.id] ?: 0.0
            val owed = totalOwed[member.id] ?: 0.0
            Balance(
                memberId = member.id,
                memberName = member.name,
                totalPaid = paid,
                totalOwed = owed,
                netBalance = paid - owed
            )
        }
    }
}
