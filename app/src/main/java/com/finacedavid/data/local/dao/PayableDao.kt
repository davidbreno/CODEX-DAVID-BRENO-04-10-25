package com.finacedavid.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.finacedavid.data.local.PayableEntity
import com.finacedavid.data.local.PayableStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PayableDao {
    @Query("SELECT * FROM payables ORDER BY dueDate ASC")
    fun observeAll(): Flow<List<PayableEntity>>

    @Query("SELECT * FROM payables WHERE dueDate < :today ORDER BY dueDate ASC")
    suspend fun getOverdue(today: LocalDate): List<PayableEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PayableEntity): Long

    @Update
    suspend fun update(entity: PayableEntity)

    @Delete
    suspend fun delete(entity: PayableEntity)

    @Query("UPDATE payables SET status = :status, updated_at = CURRENT_TIMESTAMP WHERE id = :id")
    suspend fun updateStatus(id: Long, status: PayableStatus)
}
