using FinaceDavid.Desktop.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FinaceDavid.Desktop.Services;

public class CalendarService
{
    private readonly TransactionService _transactionService;

    public CalendarService(TransactionService transactionService)
    {
        _transactionService = transactionService;
    }

    public async Task<IEnumerable<CalendarDaySummary>> LoadMonthAsync(DateTime month)
    {
        var start = new DateTime(month.Year, month.Month, 1);
        var end = start.AddMonths(1).AddTicks(-1);
        var transactions = await _transactionService.GetTransactionsInRangeAsync(start, end);

        return Enumerable.Range(0, DateTime.DaysInMonth(month.Year, month.Month))
            .Select(i => start.AddDays(i))
            .Select(day => new CalendarDaySummary(
                day,
                transactions.Where(t => t.Type == TransactionType.Entrada && t.Date.Date == day.Date).Sum(t => t.Amount),
                transactions.Where(t => t.Type == TransactionType.Saida && t.Date.Date == day.Date).Sum(t => t.Amount)
            ));
    }

    public IEnumerable<ChartPoint> BuildTrend(IEnumerable<Transaction> transactions, DateTime start, DateTime end)
    {
        var days = (end.Date - start.Date).Days + 1;
        return Enumerable.Range(0, days)
            .Select(i => start.Date.AddDays(i))
            .Select(day => new ChartPoint(day.ToString("dd/MM"),
                transactions.Where(t => t.Date.Date == day && t.Type == TransactionType.Entrada).Sum(t => t.Amount) -
                transactions.Where(t => t.Date.Date == day && t.Type == TransactionType.Saida).Sum(t => t.Amount)));
    }
}
