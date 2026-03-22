package com.splitpay.app

import android.app.Application
import com.splitpay.app.di.ServiceLocator

class SplitPayApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Touch ServiceLocator to trigger lazy initialization
        ServiceLocator.groupRepository
        ServiceLocator.expenseRepository
    }
}
