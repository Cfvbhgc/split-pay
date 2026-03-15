package com.splitpay.app.domain.model

data class Balance(
    val memberId: String,
    val memberName: String,
    val totalPaid: Double,
    val totalOwed: Double,
    val netBalance: Double
)
