using CommunityToolkit.Mvvm.ComponentModel;
using FinaceDavid.Desktop.Models;
using FinaceDavid.Desktop.Services;
using System;
using System.Collections.ObjectModel;
using System.Threading.Tasks;

namespace FinaceDavid.Desktop.ViewModels;

public partial class CalendarViewModel : ViewModelBase
{
    private readonly CalendarService _calendarService;

    [ObservableProperty]
    private ObservableCollection<CalendarDaySummary> _days = new();

    [ObservableProperty]
    private DateTime _currentMonth = DateTime.Today;

    public CalendarViewModel(CalendarService calendarService)
    {
        _calendarService = calendarService;
    }

    public async Task LoadAsync()
    {
        await RefreshAsync();
    }

    public async Task ChangeMonthAsync(int offset)
    {
        CurrentMonth = CurrentMonth.AddMonths(offset);
        await RefreshAsync();
    }

    private async Task RefreshAsync()
    {
        Days = new ObservableCollection<CalendarDaySummary>(await _calendarService.LoadMonthAsync(CurrentMonth));
    }
}

public record CalendarDaySummary(DateTime Date, decimal TotalIncome, decimal TotalOutcome);
