package com.finacedavid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.finacedavid.data.local.dao.PayableDao
import com.finacedavid.data.local.dao.TransactionDao
import com.finacedavid.data.local.dao.UserDao

@Database(
    entities = [UserEntity::class, TransactionEntity::class, PayableEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FinaceDavidDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun payableDao(): PayableDao
}
