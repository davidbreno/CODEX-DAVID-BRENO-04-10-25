using FinaceDavid.Services.Models;

namespace FinaceDavid.Services;

public class FilterStateService : IFilterStateService
{
    private const string PeriodKey = "finacedavid_period";
    private PeriodFilter _currentPeriod;
    private DateRange _currentRange;

    public FilterStateService()
    {
        var saved = Preferences.Get(PeriodKey, PeriodFilter.MesAtual.ToString());
        if (!Enum.TryParse<PeriodFilter>(saved, out _currentPeriod))
        {
            _currentPeriod = PeriodFilter.MesAtual;
        }

        _currentRange = DateRange.FromPeriod(_currentPeriod);
    }

    public event EventHandler? FilterChanged;

    public PeriodFilter CurrentPeriod => _currentPeriod;

    public DateRange CurrentRange => _currentRange;

    public void SetPeriod(PeriodFilter period, DateTime? start = null, DateTime? end = null)
    {
        _currentPeriod = period;
        _currentRange = DateRange.FromPeriod(period, start, end);
        Preferences.Set(PeriodKey, period.ToString());
        FilterChanged?.Invoke(this, EventArgs.Empty);
    }
}
