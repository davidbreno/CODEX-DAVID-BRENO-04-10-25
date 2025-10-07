namespace FinaceDavid.Services.Models;

public record CalendarDay(DateTime Date, decimal Total, bool HasTransactions, bool IsCurrentMonth);
