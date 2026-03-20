package com.splitpay.app.data.local

import com.splitpay.app.domain.model.Member
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MockAuth {
    private val _currentUser = MutableStateFlow(
        Member(id = "current_user", name = "You", email = "you@splitpay.com")
    )
    val currentUser: StateFlow<Member> = _currentUser.asStateFlow()

    fun signIn(name: String, email: String) {
        _currentUser.value = Member(name = name, email = email)
    }

    fun signOut() {
        _currentUser.value = Member(id = "current_user", name = "You", email = "you@splitpay.com")
    }
}
