using FinaceDavid.Desktop.ViewModels;
using Xunit;

namespace FinaceDavid.Desktop.Tests;

public class PeriodHelperTests
{
    [Fact]
    public void MonthRange_StartsAtFirstDay()
    {
        var (start, end) = PeriodHelper.ToDateRange(PeriodFilter.Month);
        Assert.Equal(1, start.Day);
        Assert.True(end >= start);
    }

    [Fact]
    public void WeekRange_HasSevenDays()
    {
        var (start, end) = PeriodHelper.ToDateRange(PeriodFilter.Week);
        Assert.Equal(7, (end.Date - start.Date).Days);
    }
}
