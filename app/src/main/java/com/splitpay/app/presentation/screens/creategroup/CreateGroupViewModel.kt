package com.splitpay.app.presentation.screens.creategroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitpay.app.domain.model.Group
import com.splitpay.app.domain.model.Member
import com.splitpay.app.domain.usecase.CreateGroupUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateGroupState(
    val groupName: String = "",
    val memberName: String = "",
    val members: List<Member> = emptyList(),
    val error: String? = null
)

class CreateGroupViewModel(
    private val createGroupUseCase: CreateGroupUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CreateGroupState())
    val state: StateFlow<CreateGroupState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<CreateGroupEvent>()
    val events: SharedFlow<CreateGroupEvent> = _events.asSharedFlow()

    fun updateGroupName(name: String) {
        _state.update { it.copy(groupName = name, error = null) }
    }

    fun updateMemberName(name: String) {
        _state.update { it.copy(memberName = name) }
    }

    fun addMember() {
        val name = _state.value.memberName.trim()
        if (name.isBlank()) return
        _state.update {
            it.copy(
                members = it.members + Member(name = name),
                memberName = ""
            )
        }
    }

    fun removeMember(memberId: String) {
        _state.update {
            it.copy(members = it.members.filter { m -> m.id != memberId })
        }
    }

    fun createGroup() {
        viewModelScope.launch {
            try {
                val group = Group(
                    name = _state.value.groupName.trim(),
                    members = _state.value.members
                )
                createGroupUseCase(group)
                _events.emit(CreateGroupEvent.GroupCreated)
            } catch (e: IllegalArgumentException) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }
}

sealed class CreateGroupEvent {
    data object GroupCreated : CreateGroupEvent()
}
