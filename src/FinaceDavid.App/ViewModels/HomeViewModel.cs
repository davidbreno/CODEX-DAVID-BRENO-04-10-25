using System.Collections.ObjectModel;
using System.Globalization;
using System.Linq;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;
using FinaceDavid.Services;
using FinaceDavid.Services.Models;

namespace FinaceDavid.ViewModels;

public partial class HomeViewModel : ViewModelBase
{
    private readonly ITransactionService _transactionService;
    private readonly IReportService _reportService;
    private readonly IThemeService _themeService;
    private readonly IFilterStateService _filterStateService;
    private readonly ICalendarService _calendarService;
    private readonly CultureInfo _culture = CultureInfo.GetCultureInfo("pt-BR");

    [ObservableProperty]
    private decimal saldo;

    [ObservableProperty]
    private decimal totalEntradas;

    [ObservableProperty]
    private decimal totalSaidas;

    [ObservableProperty]
    private string periodDescription = string.Empty;

    [ObservableProperty]
    private string resumoNarrativa = string.Empty;

    [ObservableProperty]
    private bool isResumoExpanded = true;

    [ObservableProperty]
    private DateTime calendarMonth = new(DateTime.Today.Year, DateTime.Today.Month, 1);

    [ObservableProperty]
    private CalendarDay? highlightedDay;

    [ObservableProperty]
    private string highlightedDayLabel = "Selecione uma data";

    [ObservableProperty]
    private decimal highlightedDayEntradas;

    [ObservableProperty]
    private decimal highlightedDaySaidas;

    public decimal HighlightedDaySaldo => HighlightedDayEntradas - HighlightedDaySaidas;

    public ObservableCollection<ChartSlice> DonutSlices { get; } = new();
    public ObservableCollection<TrendPoint> TrendPoints { get; } = new();
    public ObservableCollection<Transaction> RecentTransactions { get; } = new();
    public ObservableCollection<CalendarDay> CalendarDays { get; } = new();
    public ObservableCollection<DailySummary> PeriodSummaries { get; } = new();
    public ObservableCollection<Transaction> DailyTransactions { get; } = new();
    public ObservableCollection<CategoryBreakdown> CategoryHighlights { get; } = new();

    public HomeViewModel(
        ITransactionService transactionService,
        IReportService reportService,
        IThemeService themeService,
        IFilterStateService filterStateService,
        ICalendarService calendarService)
    {
        _transactionService = transactionService;
        _reportService = reportService;
        _themeService = themeService;
        _filterStateService = filterStateService;
        _calendarService = calendarService;
        Title = "Dashboard";
        _filterStateService.FilterChanged += (_, _) => _ = LoadAsync();
    }

    public async Task LoadAsync()
    {
        if (IsBusy)
        {
            return;
        }

        IsBusy = true;
        try
        {
            var range = _filterStateService.CurrentRange;
            var filter = new TransactionFilter(range);

            PeriodDescription = BuildPeriodDescription(_filterStateService.CurrentPeriod, range);

            var entradasTask = _transactionService.GetTotalAsync(TransactionType.Entrada, filter);
            var saidasTask = _transactionService.GetTotalAsync(TransactionType.Saida, filter);
            var transactionsTask = _transactionService.GetTransactionsAsync(filter);
            var donutTask = _reportService.BuildDonutAsync(filter);
            var trendTask = _reportService.BuildTrendAsync(filter);

            TotalEntradas = await entradasTask;
            TotalSaidas = await saidasTask;
            Saldo = TotalEntradas - TotalSaidas;

            SyncCollection(DonutSlices, await donutTask);
            SyncCollection(TrendPoints, await trendTask);

            var transactions = await transactionsTask;
            UpdateRecentTransactions(transactions);
            UpdateCategoryHighlights(transactions);

            var summaries = transactions
                .GroupBy(t => t.Data.Date)
                .Select(g => new DailySummary(
                    g.Key,
                    g.Where(t => t.Type == TransactionType.Entrada).Sum(t => t.Valor),
                    g.Where(t => t.Type == TransactionType.Saida).Sum(t => t.Valor)))
                .OrderBy(s => s.Date)
                .ToList();
            UpdatePeriodSummaries(summaries);

            CalendarMonth = new DateTime(range.Start.Year, range.Start.Month, 1);
            await RefreshCalendarAsync(range.Start);
        }
        finally
        {
            IsBusy = false;
        }
    }

    [RelayCommand]
    private Task ToggleThemeAsync() => _themeService.ToggleThemeAsync();

    [RelayCommand]
    private void SetPeriod(string periodKey)
    {
        if (Enum.TryParse<PeriodFilter>(periodKey, out var period))
        {
            _filterStateService.SetPeriod(period);
        }
    }

    [RelayCommand]
    private async Task OpenSectionAsync(string section)
    {
        switch (section)
        {
            case "Entradas":
                _filterStateService.SetPeriod(_filterStateService.CurrentPeriod);
                await Shell.Current.GoToAsync("//transactions?type=Entrada");
                break;
            case "Saídas":
                _filterStateService.SetPeriod(_filterStateService.CurrentPeriod);
                await Shell.Current.GoToAsync("//transactions?type=Saida");
                break;
            case "Contas":
                await Shell.Current.GoToAsync("//payables");
                break;
            case "Tema":
                await ToggleThemeAsync();
                break;
            case "Resumo":
                IsResumoExpanded = !IsResumoExpanded;
                break;
        }
    }

    [RelayCommand]
    private async Task NextCalendarMonthAsync()
    {
        CalendarMonth = CalendarMonth.AddMonths(1);
        await RefreshCalendarAsync();
    }

    [RelayCommand]
    private async Task PreviousCalendarMonthAsync()
    {
        CalendarMonth = CalendarMonth.AddMonths(-1);
        await RefreshCalendarAsync();
    }

    [RelayCommand]
    private async Task SelectCalendarDayAsync(CalendarDay? day)
    {
        if (day is null)
        {
            return;
        }

        CalendarMonth = new DateTime(day.Date.Year, day.Date.Month, 1);
        HighlightedDay = day;
        await UpdateHighlightedDayAsync(day);
        _filterStateService.SetPeriod(PeriodFilter.Intervalo, day.Date, day.Date);
    }

    private async Task RefreshCalendarAsync(DateTime? highlightDate = null)
    {
        var days = await _calendarService.BuildMonthAsync(CalendarMonth);
        SyncCollection(CalendarDays, days);

        var selected = highlightDate.HasValue
            ? CalendarDays.FirstOrDefault(d => d.Date.Date == highlightDate.Value.Date)
            : CalendarDays.FirstOrDefault(d => d.IsCurrentMonth && d.IsToday)
              ?? CalendarDays.FirstOrDefault(d => d.IsCurrentMonth)
              ?? CalendarDays.FirstOrDefault();

        HighlightedDay = selected;

        if (selected is not null)
        {
            await UpdateHighlightedDayAsync(selected);
        }
        else
        {
            DailyTransactions.Clear();
            HighlightedDayEntradas = 0;
            HighlightedDaySaidas = 0;
            HighlightedDayLabel = "Selecione uma data";
        }
    }

    private async Task UpdateHighlightedDayAsync(CalendarDay day)
    {
        UpdateCalendarSelection(day);
        HighlightedDayLabel = _culture.TextInfo.ToTitleCase(day.Date.ToString("dddd, dd 'de' MMMM", _culture));

        var range = new DateRange(day.Date, day.Date.AddDays(1).AddTicks(-1));
        var filter = new TransactionFilter(range);
        var transactions = await _transactionService.GetTransactionsAsync(filter);

        DailyTransactions.Clear();
        foreach (var transaction in transactions.OrderByDescending(t => t.Data))
        {
            DailyTransactions.Add(transaction);
        }

        HighlightedDayEntradas = transactions.Where(t => t.Type == TransactionType.Entrada).Sum(t => t.Valor);
        HighlightedDaySaidas = transactions.Where(t => t.Type == TransactionType.Saida).Sum(t => t.Valor);
    }

    private void UpdateRecentTransactions(IEnumerable<Transaction> transactions)
    {
        RecentTransactions.Clear();
        foreach (var item in transactions.OrderByDescending(t => t.Data).Take(8))
        {
            RecentTransactions.Add(item);
        }
    }

    private void UpdateCategoryHighlights(IEnumerable<Transaction> transactions)
    {
        var highlights = transactions
            .Where(t => t.Type == TransactionType.Saida)
            .GroupBy(t => string.IsNullOrWhiteSpace(t.Categoria) ? "Outros" : t.Categoria)
            .Select(g => new CategoryBreakdown(g.Key, g.Sum(t => t.Valor)))
            .OrderByDescending(c => c.Total)
            .Take(4)
            .ToList();

        SyncCollection(CategoryHighlights, highlights);
    }

    private void UpdatePeriodSummaries(IReadOnlyList<DailySummary> summaries)
    {
        var ordered = summaries
            .OrderByDescending(s => s.Date)
            .Take(7)
            .OrderBy(s => s.Date)
            .ToList();

        SyncCollection(PeriodSummaries, ordered);

        var totalEntrada = summaries.Sum(s => s.TotalEntrada);
        var totalSaida = summaries.Sum(s => s.TotalSaida);
        var saldoPeriodo = totalEntrada - totalSaida;

        ResumoNarrativa = summaries.Any()
            ? $"No período você recebeu {totalEntrada.ToString("C2", _culture)} e pagou {totalSaida.ToString("C2", _culture)}, fechando com {saldoPeriodo.ToString("C2", _culture)}."
            : "Nenhuma movimentação registrada no período selecionado.";
    }

    private void UpdateCalendarSelection(CalendarDay? selected)
    {
        foreach (var day in CalendarDays)
        {
            day.IsSelected = selected is not null && day.Date.Date == selected.Date.Date;
        }
    }

    private string BuildPeriodDescription(PeriodFilter period, DateRange range)
    {
        var start = range.Start.Date;
        var end = range.End.Date;

        return period switch
        {
            PeriodFilter.Hoje => "Hoje",
            PeriodFilter.SeteDias => "Últimos 7 dias",
            PeriodFilter.MesAtual => _culture.TextInfo.ToTitleCase(start.ToString("MMMM yyyy", _culture)),
            PeriodFilter.Intervalo => $"{start:dd MMM} — {end:dd MMM}",
            _ => $"{start:dd/MM} - {end:dd/MM}"
        };
    }

    private static void SyncCollection<T>(ObservableCollection<T> target, IEnumerable<T> source)
    {
        target.Clear();
        foreach (var item in source)
        {
            target.Add(item);
        }
    }

    partial void OnHighlightedDayEntradasChanged(decimal value) => OnPropertyChanged(nameof(HighlightedDaySaldo));

    partial void OnHighlightedDaySaidasChanged(decimal value) => OnPropertyChanged(nameof(HighlightedDaySaldo));

    partial void OnHighlightedDayChanged(CalendarDay? value) => UpdateCalendarSelection(value);
}
