package com.splitpay.app.presentation.screens.addexpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitpay.app.domain.model.Expense
import com.splitpay.app.domain.model.Member
import com.splitpay.app.domain.model.Split
import com.splitpay.app.domain.model.SplitType
import com.splitpay.app.domain.repository.GroupRepository
import com.splitpay.app.domain.usecase.AddExpenseUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddExpenseState(
    val amount: String = "",
    val description: String = "",
    val members: List<Member> = emptyList(),
    val selectedPayerId: String? = null,
    val splitType: SplitType = SplitType.EQUAL,
    val customSplits: Map<String, String> = emptyMap(),
    val error: String? = null,
    val isLoading: Boolean = true
)

class AddExpenseViewModel(
    private val groupId: String,
    private val groupRepository: GroupRepository,
    private val addExpenseUseCase: AddExpenseUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddExpenseState())
    val state: StateFlow<AddExpenseState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<AddExpenseEvent>()
    val events: SharedFlow<AddExpenseEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            val group = groupRepository.getGroupById(groupId).first()
            if (group != null) {
                _state.update {
                    it.copy(
                        members = group.members,
                        selectedPayerId = group.members.firstOrNull()?.id,
                        customSplits = group.members.associate { m -> m.id to "" },
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateAmount(amount: String) {
        _state.update { it.copy(amount = amount, error = null) }
    }

    fun updateDescription(description: String) {
        _state.update { it.copy(description = description, error = null) }
    }

    fun selectPayer(memberId: String) {
        _state.update { it.copy(selectedPayerId = memberId) }
    }

    fun updateSplitType(type: SplitType) {
        _state.update { it.copy(splitType = type) }
    }

    fun updateCustomSplit(memberId: String, amount: String) {
        _state.update {
            it.copy(customSplits = it.customSplits + (memberId to amount))
        }
    }

    fun addExpense() {
        viewModelScope.launch {
            try {
                val amount = _state.value.amount.toDoubleOrNull()
                    ?: throw IllegalArgumentException("Invalid amount")
                val payerId = _state.value.selectedPayerId
                    ?: throw IllegalArgumentException("Select a payer")

                val splits = when (_state.value.splitType) {
                    SplitType.EQUAL -> emptyList()
                    SplitType.CUSTOM -> _state.value.customSplits.map { (memberId, amountStr) ->
                        Split(memberId, amountStr.toDoubleOrNull() ?: 0.0)
                    }
                }

                val expense = Expense(
                    groupId = groupId,
                    amount = amount,
                    description = _state.value.description.trim(),
                    paidBy = payerId,
                    splitType = _state.value.splitType,
                    splits = splits
                )

                addExpenseUseCase(expense)
                _events.emit(AddExpenseEvent.ExpenseAdded)
            } catch (e: IllegalArgumentException) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }
}

sealed class AddExpenseEvent {
    data object ExpenseAdded : AddExpenseEvent()
}
