package com.splitpay.app.domain.model

data class DebtSettlement(
    val fromMemberId: String,
    val fromMemberName: String,
    val toMemberId: String,
    val toMemberName: String,
    val amount: Double
)
