package com.splitpay.app.domain.model

import java.util.UUID

data class Group(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val members: List<Member> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)
