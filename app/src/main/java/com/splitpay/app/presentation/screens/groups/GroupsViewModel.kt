package com.splitpay.app.presentation.screens.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitpay.app.domain.model.Group
import com.splitpay.app.domain.repository.GroupRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroupsViewModel(
    private val groupRepository: GroupRepository
) : ViewModel() {

    val groups: StateFlow<List<Group>> = groupRepository.getAllGroups()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteGroup(groupId: String) {
        viewModelScope.launch {
            groupRepository.deleteGroup(groupId)
        }
    }
}
