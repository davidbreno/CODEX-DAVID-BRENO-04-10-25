using System.Collections.ObjectModel;
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

    [ObservableProperty]
    private decimal saldo;

    [ObservableProperty]
    private decimal totalEntradas;

    [ObservableProperty]
    private decimal totalSaidas;

    public ObservableCollection<ChartSlice> DonutSlices { get; } = new();
    public ObservableCollection<TrendPoint> TrendPoints { get; } = new();
    public ObservableCollection<Transaction> RecentTransactions { get; } = new();

    public HomeViewModel(
        ITransactionService transactionService,
        IReportService reportService,
        IThemeService themeService,
        IFilterStateService filterStateService)
    {
        _transactionService = transactionService;
        _reportService = reportService;
        _themeService = themeService;
        _filterStateService = filterStateService;
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
            var filter = new TransactionFilter(_filterStateService.CurrentRange);
            TotalEntradas = await _transactionService.GetTotalAsync(TransactionType.Entrada, filter);
            TotalSaidas = await _transactionService.GetTotalAsync(TransactionType.Saida, filter);
            Saldo = TotalEntradas - TotalSaidas;

            DonutSlices.Clear();
            foreach (var slice in await _reportService.BuildDonutAsync(filter))
            {
                DonutSlices.Add(slice);
            }

            TrendPoints.Clear();
            foreach (var point in await _reportService.BuildTrendAsync(filter))
            {
                TrendPoints.Add(point);
            }

            RecentTransactions.Clear();
            var transactions = await _transactionService.GetTransactionsAsync(filter);
            foreach (var item in transactions.Take(10))
            {
                RecentTransactions.Add(item);
            }
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
        }
    }
}
