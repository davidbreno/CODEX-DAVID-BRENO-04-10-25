using System.Collections.ObjectModel;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using FinaceDavid.Services;
using FinaceDavid.Services.Models;

namespace FinaceDavid.ViewModels;

public partial class CalendarViewModel : ViewModelBase
{
    private readonly ICalendarService _calendarService;
    private readonly IFilterStateService _filterStateService;

    [ObservableProperty]
    private DateTime referenceMonth = DateTime.Today;

    public ObservableCollection<CalendarDay> Days { get; } = new();

    public CalendarViewModel(ICalendarService calendarService, IFilterStateService filterStateService)
    {
        _calendarService = calendarService;
        _filterStateService = filterStateService;
        Title = "Calendário";
    }

    public async Task LoadAsync()
    {
        IsBusy = true;
        try
        {
            var days = await _calendarService.BuildMonthAsync(ReferenceMonth);
            Days.Clear();
            foreach (var day in days)
            {
                Days.Add(day);
            }
        }
        finally
        {
            IsBusy = false;
        }
    }

    [RelayCommand]
    private async Task NextMonthAsync()
    {
        ReferenceMonth = ReferenceMonth.AddMonths(1);
        await LoadAsync();
    }

    [RelayCommand]
    private async Task PreviousMonthAsync()
    {
        ReferenceMonth = ReferenceMonth.AddMonths(-1);
        await LoadAsync();
    }

    [RelayCommand]
    private void SelectDay(CalendarDay? day)
    {
        if (day is null)
        {
            return;
        }

        _filterStateService.SetPeriod(PeriodFilter.Intervalo, day.Value.Date, day.Value.Date);
    }
}
