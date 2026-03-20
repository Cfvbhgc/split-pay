package com.splitpay.app.di

import com.splitpay.app.data.local.MockFirestore
import com.splitpay.app.data.repository.ExpenseRepositoryImpl
import com.splitpay.app.data.repository.GroupRepositoryImpl
import com.splitpay.app.domain.model.Expense
import com.splitpay.app.domain.model.Group
import com.splitpay.app.domain.repository.ExpenseRepository
import com.splitpay.app.domain.repository.GroupRepository
import com.splitpay.app.domain.usecase.AddExpenseUseCase
import com.splitpay.app.domain.usecase.CalculateDebtsUseCase
import com.splitpay.app.domain.usecase.CreateGroupUseCase
import com.splitpay.app.domain.usecase.GetGroupBalancesUseCase
import com.splitpay.app.domain.usecase.SettleDebtUseCase

object ServiceLocator {

    private val groupCollection = MockFirestore.collection<Group>("groups") { it.id }
    private val expenseCollection = MockFirestore.collection<Expense>("expenses") { it.id }

    val groupRepository: GroupRepository by lazy {
        GroupRepositoryImpl(groupCollection)
    }

    val expenseRepository: ExpenseRepository by lazy {
        ExpenseRepositoryImpl(expenseCollection)
    }

    val createGroupUseCase: CreateGroupUseCase by lazy {
        CreateGroupUseCase(groupRepository)
    }

    val addExpenseUseCase: AddExpenseUseCase by lazy {
        AddExpenseUseCase(expenseRepository, groupRepository)
    }

    val calculateDebtsUseCase: CalculateDebtsUseCase by lazy {
        CalculateDebtsUseCase()
    }

    val getGroupBalancesUseCase: GetGroupBalancesUseCase by lazy {
        GetGroupBalancesUseCase()
    }

    val settleDebtUseCase: SettleDebtUseCase by lazy {
        SettleDebtUseCase(expenseRepository)
    }
}
