package com.finacedavid.data.local

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val offsetFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? = value?.let { BigDecimal(it) }

    @TypeConverter
    fun fromOffsetDateTime(value: OffsetDateTime?): String? = value?.format(offsetFormatter)

    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? = value?.let { OffsetDateTime.parse(it, offsetFormatter) }

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromTransactionType(value: TransactionType?): String? = value?.name

    @TypeConverter
    fun toTransactionType(value: String?): TransactionType? = value?.let { TransactionType.valueOf(it) }

    @TypeConverter
    fun fromTransactionStatus(value: TransactionStatus?): String? = value?.name

    @TypeConverter
    fun toTransactionStatus(value: String?): TransactionStatus? = value?.let { TransactionStatus.valueOf(it) }

    @TypeConverter
    fun fromPayableStatus(value: PayableStatus?): String? = value?.name

    @TypeConverter
    fun toPayableStatus(value: String?): PayableStatus? = value?.let { PayableStatus.valueOf(it) }
}
