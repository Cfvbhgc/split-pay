package com.splitpay.app.domain.model

import java.util.UUID

enum class SplitType { EQUAL, CUSTOM }

data class Split(
    val memberId: String,
    val amount: Double
)

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val groupId: String,
    val amount: Double,
    val description: String,
    val paidBy: String,
    val splitType: SplitType = SplitType.EQUAL,
    val splits: List<Split> = emptyList(),
    val date: Long = System.currentTimeMillis(),
    val isSettlement: Boolean = false
)
