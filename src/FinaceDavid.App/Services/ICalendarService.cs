using FinaceDavid.Services.Models;

namespace FinaceDavid.Services;

public interface ICalendarService
{
    Task<IReadOnlyList<CalendarDay>> BuildMonthAsync(DateTime monthReference);
}
