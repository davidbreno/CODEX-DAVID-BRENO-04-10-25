package com.financeflow

import android.app.Application
import androidx.room.Room
import com.financeflow.app.FinanceFlowDatabase
import com.financeflow.authentication.AuthRepository
import com.financeflow.settings.ThemeRepository
import com.financeflow.settings.dataStore
import com.financeflow.transactions.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class FinanceFlowApp : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            FinanceFlowDatabase::class.java,
            "financeflow.db"
        ).fallbackToDestructiveMigration().build()
    }

    val themeRepository by lazy { ThemeRepository(applicationContext.dataStore) }

    val authRepository by lazy {
        AuthRepository(database.userDao(), applicationScope, themeRepository)
    }

    val transactionRepository by lazy {
        TransactionRepository(database.transactionDao(), database.billDao(), applicationScope)
    }
}
