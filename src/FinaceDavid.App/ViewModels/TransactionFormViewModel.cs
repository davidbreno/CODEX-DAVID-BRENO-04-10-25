using System.Globalization;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;
using FinaceDavid.Services;
using FinaceDavid.Services.Models;

namespace FinaceDavid.ViewModels;

public partial class TransactionFormViewModel : ViewModelBase
{
    private readonly ITransactionService _transactionService;
    private readonly IFilterStateService _filterStateService;

    [ObservableProperty]
    private int id;

    [ObservableProperty]
    private TransactionType type = TransactionType.Entrada;

    [ObservableProperty]
    private TransactionStatus status = TransactionStatus.Pago;

    [ObservableProperty]
    private decimal valor;

    [ObservableProperty]
    private DateTime data = DateTime.Today;

    [ObservableProperty]
    private string categoria = string.Empty;

    [ObservableProperty]
    private string descricao = string.Empty;

    [ObservableProperty]
    private string? anexoPath;

    public TransactionFormViewModel(ITransactionService transactionService, IFilterStateService filterStateService)
    {
        _transactionService = transactionService;
        _filterStateService = filterStateService;
        Title = "Registrar";
    }

    public void LoadTransaction(Transaction? transaction)
    {
        if (transaction is null)
        {
            return;
        }

        Id = transaction.Id;
        Type = transaction.Type;
        Status = transaction.Status;
        Valor = transaction.Valor;
        Data = transaction.Data;
        Categoria = transaction.Categoria;
        Descricao = transaction.Descricao;
        AnexoPath = transaction.AnexoPath;
        Title = "Editar";
    }

    [RelayCommand]
    private async Task SaveAsync()
    {
        if (Valor <= 0)
        {
            await Shell.Current.DisplayAlert("Atenção", "Informe um valor válido", "Ok");
            return;
        }

        var transaction = new Transaction
        {
            Id = Id,
            Type = Type,
            Status = Status,
            Valor = Valor,
            Data = Data,
            Categoria = Categoria,
            Descricao = Descricao,
            AnexoPath = AnexoPath
        };

        await _transactionService.SaveAsync(transaction);
        await Shell.Current.DisplayAlert("Sucesso", "Transação salva", "Ok");
        _filterStateService.SetPeriod(PeriodFilter.Intervalo, Data, Data);
        await Shell.Current.GoToAsync("..", true);
    }
}
