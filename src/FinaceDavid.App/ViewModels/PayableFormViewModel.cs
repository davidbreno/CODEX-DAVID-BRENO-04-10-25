using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;
using FinaceDavid.Services;

namespace FinaceDavid.ViewModels;

public partial class PayableFormViewModel : ViewModelBase
{
    private readonly IPayableService _payableService;

    [ObservableProperty]
    private int id;

    [ObservableProperty]
    private string titulo = string.Empty;

    [ObservableProperty]
    private decimal valor;

    [ObservableProperty]
    private DateTime vencimento = DateTime.Today;

    [ObservableProperty]
    private PayableStatus status = PayableStatus.Pendente;

    [ObservableProperty]
    private string nota = string.Empty;

    public PayableFormViewModel(IPayableService payableService)
    {
        _payableService = payableService;
        Title = "Conta";
    }

    public void Load(Payable? payable)
    {
        if (payable is null)
        {
            return;
        }

        Id = payable.Id;
        Titulo = payable.Titulo;
        Valor = payable.Valor;
        Vencimento = payable.Vencimento;
        Status = payable.Status;
        Nota = payable.Nota;
    }

    [RelayCommand]
    private async Task SaveAsync()
    {
        if (string.IsNullOrWhiteSpace(Titulo) || Valor <= 0)
        {
            await Shell.Current.DisplayAlert("Atenção", "Preencha os dados obrigatórios", "Ok");
            return;
        }

        var payable = new Payable
        {
            Id = Id,
            Titulo = Titulo,
            Valor = Valor,
            Vencimento = Vencimento,
            Status = Status,
            Nota = Nota
        };

        await _payableService.SaveAsync(payable);
        await Shell.Current.DisplayAlert("Sucesso", "Conta salva", "Ok");
        await Shell.Current.GoToAsync("..", true);
    }
}
