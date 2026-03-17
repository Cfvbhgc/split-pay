package com.splitpay.app.domain.usecase

import com.splitpay.app.domain.model.DebtSettlement
import com.splitpay.app.domain.model.Expense
import com.splitpay.app.domain.model.Member
import java.util.PriorityQueue
import kotlin.math.abs
import kotlin.math.min

class CalculateDebtsUseCase {

    operator fun invoke(expenses: List<Expense>, members: List<Member>): List<DebtSettlement> {
        val memberMap = members.associateBy { it.id }
        val netBalances = mutableMapOf<String, Double>()

        for (member in members) {
            netBalances[member.id] = 0.0
        }

        for (expense in expenses) {
            netBalances[expense.paidBy] =
                (netBalances[expense.paidBy] ?: 0.0) + expense.amount

            for (split in expense.splits) {
                netBalances[split.memberId] =
                    (netBalances[split.memberId] ?: 0.0) - split.amount
            }
        }

        // Greedy algorithm: match largest creditor with largest debtor to minimize transactions
        val debtors = PriorityQueue<Pair<String, Double>>(compareBy { it.second })
        val creditors = PriorityQueue<Pair<String, Double>>(compareByDescending { it.second })

        for ((memberId, balance) in netBalances) {
            when {
                balance < -0.01 -> debtors.add(memberId to balance)
                balance > 0.01 -> creditors.add(memberId to balance)
            }
        }

        val settlements = mutableListOf<DebtSettlement>()

        while (debtors.isNotEmpty() && creditors.isNotEmpty()) {
            val (debtorId, debtorBalance) = debtors.poll()
            val (creditorId, creditorBalance) = creditors.poll()

            val settleAmount = min(abs(debtorBalance), creditorBalance)
            val roundedAmount = (settleAmount * 100).toLong() / 100.0

            if (roundedAmount > 0.0) {
                settlements.add(
                    DebtSettlement(
                        fromMemberId = debtorId,
                        fromMemberName = memberMap[debtorId]?.name ?: "Unknown",
                        toMemberId = creditorId,
                        toMemberName = memberMap[creditorId]?.name ?: "Unknown",
                        amount = roundedAmount
                    )
                )
            }

            val remainingDebt = debtorBalance + settleAmount
            val remainingCredit = creditorBalance - settleAmount

            if (remainingDebt < -0.01) debtors.add(debtorId to remainingDebt)
            if (remainingCredit > 0.01) creditors.add(creditorId to remainingCredit)
        }

        return settlements
    }
}
