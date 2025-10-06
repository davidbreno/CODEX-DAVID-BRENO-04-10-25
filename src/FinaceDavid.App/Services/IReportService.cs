using FinaceDavid.Services.Models;

namespace FinaceDavid.Services;

public interface IReportService
{
    Task<IReadOnlyList<ChartSlice>> BuildDonutAsync(TransactionFilter filter);
    Task<IReadOnlyList<TrendPoint>> BuildTrendAsync(TransactionFilter filter);
    Task<IReadOnlyList<DailySummary>> BuildCalendarSummaryAsync(DateRange range);
}
