package com.financeflow.app

import androidx.room.TypeConverter
import com.financeflow.transactions.TransactionType
import java.time.Instant

class RoomConverters {
    @TypeConverter
    fun fromInstant(value: Instant?): Long? = value?.toEpochMilli()

    @TypeConverter
    fun toInstant(value: Long?): Instant? = value?.let(Instant::ofEpochMilli)

    @TypeConverter
    fun fromTransactionType(type: TransactionType?): String? = type?.name

    @TypeConverter
    fun toTransactionType(value: String?): TransactionType? = value?.let { TransactionType.valueOf(it) }
}
