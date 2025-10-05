package com.financeflow.transactions

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY occurred_at DESC")
    fun observeTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(transaction: TransactionEntity): Long

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) FROM transactions")
    fun observeTotalIncome(): Flow<Double?>

    @Query("SELECT SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) FROM transactions")
    fun observeTotalExpense(): Flow<Double?>
}

@Dao
interface BillDao {
    @Query("SELECT * FROM bills WHERE is_paid = 0 ORDER BY due_date ASC")
    fun observeUpcomingBills(): Flow<List<BillEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(bill: BillEntity): Long

    @Update
    suspend fun update(bill: BillEntity)

    @Delete
    suspend fun delete(bill: BillEntity)
}
