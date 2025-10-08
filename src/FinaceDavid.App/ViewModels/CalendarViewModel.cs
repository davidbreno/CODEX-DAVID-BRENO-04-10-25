using System.Collections.ObjectModel;
using System.Linq;
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
            HighlightDate(_filterStateService.CurrentRange.Start.Date);
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

        _filterStateService.SetPeriod(PeriodFilter.Intervalo, day.Date, day.Date);
        HighlightDate(day.Date);
    }

    private void HighlightDate(DateTime date)
    {
        var selected = Days.FirstOrDefault(d => d.Date.Date == date)
                       ?? Days.FirstOrDefault(d => d.IsCurrentMonth && d.Date.Day == 1)
                       ?? Days.FirstOrDefault(d => d.IsCurrentMonth);

        foreach (var item in Days)
        {
            item.IsSelected = ReferenceEquals(item, selected);
        }
    }
}
