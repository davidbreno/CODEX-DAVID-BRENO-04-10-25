using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using FinaceDavid.Desktop.Models;
using FinaceDavid.Desktop.Services;
using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading.Tasks;

namespace FinaceDavid.Desktop.ViewModels;

public partial class TransactionsViewModel : ViewModelBase
{
    private readonly TransactionService _transactionService;

    [ObservableProperty]
    private ObservableCollection<Transaction> _transactions = new();

    [ObservableProperty]
    private TransactionType? _typeFilter;

    [ObservableProperty]
    private string _searchText = string.Empty;

    [ObservableProperty]
    private DateTime? _startDate;

    [ObservableProperty]
    private DateTime? _endDate;

    public TransactionsViewModel(TransactionService transactionService)
    {
        _transactionService = transactionService;
    }

    [RelayCommand]
    public async Task LoadAsync()
    {
        await RefreshAsync();
    }

    [RelayCommand]
    public async Task RefreshAsync()
    {
        var items = await _transactionService.GetTransactionsAsync(_startDate, _endDate, _typeFilter, _searchText);
        Transactions = new ObservableCollection<Transaction>(items.OrderByDescending(t => t.Date));
    }

    [RelayCommand]
    public async Task AddOrUpdateAsync(Transaction transaction)
    {
        await _transactionService.SaveAsync(transaction);
        await RefreshAsync();
    }

    [RelayCommand]
    public async Task DeleteAsync(Transaction transaction)
    {
        await _transactionService.DeleteAsync(transaction);
        await RefreshAsync();
    }
}
