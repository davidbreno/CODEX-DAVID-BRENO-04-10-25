using System.Collections.ObjectModel;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;
using FinaceDavid.Services;
using FinaceDavid.Services.Models;

namespace FinaceDavid.ViewModels;

public partial class TransactionsViewModel : ViewModelBase
{
    private readonly ITransactionService _transactionService;
    private readonly IFilterStateService _filterStateService;

    public ObservableCollection<Transaction> Transactions { get; } = new();

    [ObservableProperty]
    private string? searchText;

    [ObservableProperty]
    private string? categoria;

    [ObservableProperty]
    private TransactionType? selectedType;

    [ObservableProperty]
    private bool includePending = true;

    partial void OnSearchTextChanged(string? value) => _ = LoadAsync();

    partial void OnCategoriaChanged(string? value) => _ = LoadAsync();

    partial void OnIncludePendingChanged(bool value) => _ = LoadAsync();

    public TransactionsViewModel(ITransactionService transactionService, IFilterStateService filterStateService)
    {
        _transactionService = transactionService;
        _filterStateService = filterStateService;
        Title = "Transações";
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
            var filter = new TransactionFilter(
                _filterStateService.CurrentRange,
                SelectedType,
                Categoria,
                SearchText,
                IncludePending);

            var items = await _transactionService.GetTransactionsAsync(filter);
            Transactions.Clear();
            foreach (var item in items)
            {
                Transactions.Add(item);
            }
        }
        finally
        {
            IsBusy = false;
        }
    }

    partial void OnSelectedTypeChanged(TransactionType? value) => _ = LoadAsync();

    [RelayCommand]
    private void SetTab(string tabKey)
    {
        if (Enum.TryParse<TransactionType>(tabKey, out var type))
        {
            SelectedType = type;
        }
        else
        {
            SelectedType = null;
        }

        if (tabKey.Equals("Todas", StringComparison.OrdinalIgnoreCase))
        {
            SelectedType = null;
        }
    }

    [RelayCommand]
    private async Task AddTransactionAsync()
    {
        await Shell.Current.GoToAsync(nameof(Views.TransactionFormPage));
    }

    [RelayCommand]
    private async Task EditTransactionAsync(Transaction transaction)
    {
        if (transaction is null)
        {
            return;
        }

        await Shell.Current.GoToAsync(nameof(Views.TransactionFormPage), new Dictionary<string, object>
        {
            ["Transaction"] = transaction
        });
    }

    [RelayCommand]
    private async Task DeleteTransactionAsync(Transaction transaction)
    {
        if (transaction is null)
        {
            return;
        }

        await _transactionService.DeleteAsync(transaction.Id);
        await LoadAsync();
    }
}
