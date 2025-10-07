using FinaceDavid.Services.Models;

namespace FinaceDavid.Services;

public interface IFilterStateService
{
    PeriodFilter CurrentPeriod { get; }
    DateRange CurrentRange { get; }
    void SetPeriod(PeriodFilter period, DateTime? start = null, DateTime? end = null);
    event EventHandler? FilterChanged;
}
