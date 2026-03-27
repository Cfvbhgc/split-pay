package com.splitpay.app.presentation.screens.balances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitpay.app.domain.model.Balance
import com.splitpay.app.domain.model.DebtSettlement
import com.splitpay.app.domain.model.Group
import com.splitpay.app.domain.repository.ExpenseRepository
import com.splitpay.app.domain.repository.GroupRepository
import com.splitpay.app.domain.usecase.CalculateDebtsUseCase
import com.splitpay.app.domain.usecase.GetGroupBalancesUseCase
import com.splitpay.app.domain.usecase.SettleDebtUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BalancesState(
    val group: Group? = null,
    val balances: List<Balance> = emptyList(),
    val settlements: List<DebtSettlement> = emptyList(),
    val settlingIndex: Int? = null
)

class BalancesViewModel(
    private val groupId: String,
    groupRepository: GroupRepository,
    expenseRepository: ExpenseRepository,
    private val getGroupBalancesUseCase: GetGroupBalancesUseCase,
    private val calculateDebtsUseCase: CalculateDebtsUseCase,
    private val settleDebtUseCase: SettleDebtUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BalancesState())
    val state: StateFlow<BalancesState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                groupRepository.getGroupById(groupId),
                expenseRepository.getExpensesForGroup(groupId)
            ) { group, expenses ->
                if (group != null) {
                    BalancesState(
                        group = group,
                        balances = getGroupBalancesUseCase(expenses, group.members),
                        settlements = calculateDebtsUseCase(expenses, group.members)
                    )
                } else {
                    BalancesState()
                }
            }.collect { newState ->
                _state.update { newState }
            }
        }
    }

    fun settleDebt(index: Int, settlement: DebtSettlement) {
        viewModelScope.launch {
            _state.update { it.copy(settlingIndex = index) }
            settleDebtUseCase(groupId, settlement)
            _state.update { it.copy(settlingIndex = null) }
        }
    }
}
