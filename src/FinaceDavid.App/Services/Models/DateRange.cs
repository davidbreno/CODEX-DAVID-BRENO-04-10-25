namespace FinaceDavid.Services.Models;

public record struct DateRange(DateTime Start, DateTime End)
{
    public static DateRange FromPeriod(PeriodFilter period, DateTime? customStart = null, DateTime? customEnd = null)
    {
        var today = DateTime.Today;
        return period switch
        {
            PeriodFilter.Hoje => new DateRange(today, today.AddDays(1).AddTicks(-1)),
            PeriodFilter.SeteDias => new DateRange(today.AddDays(-6), today.AddDays(1).AddTicks(-1)),
            PeriodFilter.MesAtual => new DateRange(new DateTime(today.Year, today.Month, 1), new DateTime(today.Year, today.Month, 1).AddMonths(1).AddTicks(-1)),
            PeriodFilter.Intervalo when customStart.HasValue && customEnd.HasValue => new DateRange(customStart.Value.Date, customEnd.Value.Date.AddDays(1).AddTicks(-1)),
            _ => new DateRange(today.AddMonths(-1), today.AddDays(1).AddTicks(-1))
        };
    }
}
