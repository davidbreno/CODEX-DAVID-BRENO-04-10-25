using CommunityToolkit.Mvvm.ComponentModel;

namespace FinaceDavid.Services.Models;

public partial class CalendarDay : ObservableObject
{
    public CalendarDay(DateTime date, decimal total, bool hasTransactions, bool isCurrentMonth)
    {
        Date = date;
        Total = total;
        HasTransactions = hasTransactions;
        IsCurrentMonth = isCurrentMonth;
    }

    public DateTime Date { get; }

    public decimal Total { get; }

    public bool HasTransactions { get; }

    public bool IsCurrentMonth { get; }

    public bool IsToday => Date.Date == DateTime.Today;

    [ObservableProperty]
    private bool isSelected;
}
