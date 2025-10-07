package com.finacedavid.domain.model

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId

sealed class PeriodFilter(val label: String) {
    abstract fun range(now: OffsetDateTime = OffsetDateTime.now()): Pair<OffsetDateTime, OffsetDateTime>

    object Today : PeriodFilter("Hoje") {
        override fun range(now: OffsetDateTime): Pair<OffsetDateTime, OffsetDateTime> {
            val start = now.toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime()
            val end = start.plusDays(1)
            return start to end
        }
    }

    object Last7Days : PeriodFilter("7 dias") {
        override fun range(now: OffsetDateTime): Pair<OffsetDateTime, OffsetDateTime> {
            val end = now
            val start = now.minusDays(7)
            return start to end
        }
    }

    object CurrentMonth : PeriodFilter("Mês atual") {
        override fun range(now: OffsetDateTime): Pair<OffsetDateTime, OffsetDateTime> {
            val firstDay = now.withDayOfMonth(1).toLocalDate()
            val start = firstDay.atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime()
            val end = start.plusMonths(1)
            return start to end
        }
    }

    data class Custom(val startDate: LocalDate, val endDate: LocalDate) : PeriodFilter("Intervalo") {
        override fun range(now: OffsetDateTime): Pair<OffsetDateTime, OffsetDateTime> {
            val start = startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime()
            val end = endDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime()
            return start to end
        }
    }
}
