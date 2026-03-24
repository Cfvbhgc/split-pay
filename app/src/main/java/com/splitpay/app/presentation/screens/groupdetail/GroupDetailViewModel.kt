package com.splitpay.app.presentation.screens.groupdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitpay.app.domain.model.Balance
import com.splitpay.app.domain.model.Expense
import com.splitpay.app.domain.model.Group
import com.splitpay.app.domain.repository.ExpenseRepository
import com.splitpay.app.domain.repository.GroupRepository
import com.splitpay.app.domain.usecase.GetGroupBalancesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GroupDetailState(
    val group: Group? = null,
    val expenses: List<Expense> = emptyList(),
    val balances: List<Balance> = emptyList(),
    val totalExpenses: Double = 0.0
)

class GroupDetailViewModel(
    private val groupId: String,
    groupRepository: GroupRepository,
    expenseRepository: ExpenseRepository,
    private val getGroupBalancesUseCase: GetGroupBalancesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GroupDetailState())
    val state: StateFlow<GroupDetailState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                groupRepository.getGroupById(groupId),
                expenseRepository.getExpensesForGroup(groupId)
            ) { group, expenses ->
                val balances = if (group != null) {
                    getGroupBalancesUseCase(expenses, group.members)
                } else emptyList()

                GroupDetailState(
                    group = group,
                    expenses = expenses.sortedByDescending { it.date },
                    balances = balances,
                    totalExpenses = expenses.filter { !it.isSettlement }.sumOf { it.amount }
                )
            }.collect { newState ->
                _state.update { newState }
            }
        }
    }

    fun getMemberName(memberId: String): String {
        return _state.value.group?.members?.find { it.id == memberId }?.name ?: "Unknown"
    }
}
