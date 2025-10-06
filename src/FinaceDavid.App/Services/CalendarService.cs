using System.Globalization;
using System.Linq;
using FinaceDavid.Domain.Enums;
using FinaceDavid.Services.Models;

namespace FinaceDavid.Services;

public class CalendarService : ICalendarService
{
    private readonly ITransactionService _transactionService;

    public CalendarService(ITransactionService transactionService)
    {
        _transactionService = transactionService;
    }

    public async Task<IReadOnlyList<CalendarDay>> BuildMonthAsync(DateTime monthReference)
    {
        var culture = CultureInfo.GetCultureInfo("pt-BR");
        var firstDay = new DateTime(monthReference.Year, monthReference.Month, 1);
        var firstDayOfWeek = culture.DateTimeFormat.FirstDayOfWeek;
        var offset = ((int)firstDay.DayOfWeek - (int)firstDayOfWeek + 7) % 7;
        var startDate = firstDay.AddDays(-offset);
        var totalDays = 42;
        var range = new DateRange(startDate, startDate.AddDays(totalDays - 1).AddDays(1).AddTicks(-1));
        var transactions = await _transactionService.GetTransactionsAsync(new TransactionFilter(range));

        var lookup = transactions
            .GroupBy(t => t.Data.Date)
            .ToDictionary(g => g.Key, g => g.Sum(t => t.Type == TransactionType.Entrada ? t.Valor : -t.Valor));

        var days = new List<CalendarDay>(totalDays);
        for (var i = 0; i < totalDays; i++)
        {
            var date = startDate.AddDays(i);
            lookup.TryGetValue(date.Date, out var total);
            var hasTransactions = lookup.ContainsKey(date.Date);
            var isCurrentMonth = date.Month == monthReference.Month;
            days.Add(new CalendarDay(date, total, hasTransactions, isCurrentMonth));
        }

        return days;
    }
}
