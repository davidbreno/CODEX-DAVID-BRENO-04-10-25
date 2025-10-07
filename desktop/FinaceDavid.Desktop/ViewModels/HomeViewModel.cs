using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using FinaceDavid.Desktop.Models;
using FinaceDavid.Desktop.Services;
using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading.Tasks;

namespace FinaceDavid.Desktop.ViewModels;

public partial class HomeViewModel : ViewModelBase
{
    private readonly TransactionService _transactionService;
    private readonly CalendarService _calendarService;

    [ObservableProperty]
    private decimal _totalIncome;

    [ObservableProperty]
    private decimal _totalOutcome;

    [ObservableProperty]
    private decimal _balance;

    [ObservableProperty]
    private ObservableCollection<Transaction> _recentTransactions = new();

    [ObservableProperty]
    private ObservableCollection<ChartPoint> _distribution = new();

    [ObservableProperty]
    private ObservableCollection<ChartPoint> _trend = new();

    [ObservableProperty]
    private PeriodFilter _selectedPeriod = PeriodFilter.Month;

    public HomeViewModel(TransactionService transactionService, CalendarService calendarService)
    {
        _transactionService = transactionService;
        _calendarService = calendarService;
    }

    [RelayCommand]
    public async Task LoadAsync()
    {
        await RefreshAsync();
    }

    [RelayCommand]
    private async Task ChangePeriodAsync(PeriodFilter period)
    {
        SelectedPeriod = period;
        await RefreshAsync();
    }

    private async Task RefreshAsync()
    {
        var range = PeriodHelper.ToDateRange(SelectedPeriod);
        var transactions = await _transactionService.GetTransactionsInRangeAsync(range.start, range.end);
        TotalIncome = transactions.Where(t => t.Type == TransactionType.Entrada).Sum(t => t.Amount);
        TotalOutcome = transactions.Where(t => t.Type == TransactionType.Saida).Sum(t => t.Amount);
        Balance = TotalIncome - TotalOutcome;

        RecentTransactions = new ObservableCollection<Transaction>(transactions.OrderByDescending(t => t.Date).Take(5));

        Distribution = new ObservableCollection<ChartPoint>(new[]
        {
            new ChartPoint("Entradas", TotalIncome),
            new ChartPoint("Saídas", TotalOutcome)
        });

        Trend = new ObservableCollection<ChartPoint>(
            _calendarService.BuildTrend(transactions, range.start, range.end));
    }
}

public enum PeriodFilter
{
    Day,
    Week,
    Month
}

public static class PeriodHelper
{
    public static (DateTime start, DateTime end) ToDateRange(PeriodFilter filter)
    {
        var now = DateTime.Now;
        return filter switch
        {
            PeriodFilter.Day => (now.Date, now.Date.AddDays(1).AddTicks(-1)),
            PeriodFilter.Week => (now.Date.AddDays(-(int)now.DayOfWeek), now.Date.AddDays(7 - (int)now.DayOfWeek).AddTicks(-1)),
            _ => (new DateTime(now.Year, now.Month, 1), new DateTime(now.Year, now.Month, DateTime.DaysInMonth(now.Year, now.Month)).AddDays(1).AddTicks(-1))
        };
    }
}

public record ChartPoint(string Label, decimal Value);
