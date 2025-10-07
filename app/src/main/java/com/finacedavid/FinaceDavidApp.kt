package com.finacedavid

import android.app.Application
import androidx.room.Room
import com.finacedavid.data.local.FinaceDavidDatabase
import com.finacedavid.data.repository.AuthRepository
import com.finacedavid.data.repository.PayableRepository
import com.finacedavid.data.repository.TransactionRepository
import com.finacedavid.settings.ThemeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class FinaceDavidApp : Application() {
    private val scope = CoroutineScope(SupervisorJob())

    val database: FinaceDavidDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            FinaceDavidDatabase::class.java,
            "finace_david.db"
        ).fallbackToDestructiveMigration().build()
    }

    val themeRepository by lazy { ThemeRepository(applicationContext) }

    val authRepository by lazy { AuthRepository(applicationContext, database.userDao(), scope) }

    val transactionRepository by lazy { TransactionRepository(database.transactionDao()) }

    val payableRepository by lazy { PayableRepository(database.payableDao()) }
}
