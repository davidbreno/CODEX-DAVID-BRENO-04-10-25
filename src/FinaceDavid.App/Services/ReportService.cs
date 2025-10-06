using System.Linq;
using FinaceDavid.Domain.Enums;
using FinaceDavid.Services.Models;

namespace FinaceDavid.Services;

public class ReportService : IReportService
{
    private readonly ITransactionService _transactionService;

    public ReportService(ITransactionService transactionService)
    {
        _transactionService = transactionService;
    }

    public async Task<IReadOnlyList<ChartSlice>> BuildDonutAsync(TransactionFilter filter)
    {
        var entrada = await _transactionService.GetTotalAsync(TransactionType.Entrada, filter);
        var saida = await _transactionService.GetTotalAsync(TransactionType.Saida, filter);

        return new List<ChartSlice>
        {
            new("Entradas", entrada, Color.FromArgb("#2DD4BF")),
            new("Saídas", saida, Color.FromArgb("#38BDF8"))
        };
    }

    public Task<IReadOnlyList<TrendPoint>> BuildTrendAsync(TransactionFilter filter)
        => _transactionService.GetTrendAsync(filter);

    public async Task<IReadOnlyList<DailySummary>> BuildCalendarSummaryAsync(DateRange range)
    {
        var filter = new TransactionFilter(range);
        var transactions = await _transactionService.GetTransactionsAsync(filter);

        var map = new Dictionary<DateTime, DailySummary>();
        foreach (var transaction in transactions)
        {
            var day = transaction.Data.Date;
            if (!map.TryGetValue(day, out var summary))
            {
                summary = new DailySummary(day, 0, 0);
            }

            summary = transaction.Type == TransactionType.Entrada
                ? summary with { TotalEntrada = summary.TotalEntrada + transaction.Valor }
                : summary with { TotalSaida = summary.TotalSaida + transaction.Valor };

            map[day] = summary;
        }

        return map.Values.OrderBy(s => s.Date).ToList();
    }
}
