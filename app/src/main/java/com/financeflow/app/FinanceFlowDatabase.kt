package com.financeflow.app

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.financeflow.authentication.UserDao
import com.financeflow.authentication.UserEntity
import com.financeflow.transactions.BillDao
import com.financeflow.transactions.BillEntity
import com.financeflow.transactions.TransactionDao
import com.financeflow.transactions.TransactionEntity

@Database(
    entities = [UserEntity::class, TransactionEntity::class, BillEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class FinanceFlowDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun billDao(): BillDao
}
