package com.splitpay.app.domain.model

import java.util.UUID

data class Member(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String = ""
)
