using System.Collections.ObjectModel;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using FinaceDavid.Domain.Entities;
using FinaceDavid.Services;

namespace FinaceDavid.ViewModels;

public partial class PayablesViewModel : ViewModelBase
{
    private readonly IPayableService _payableService;

    public ObservableCollection<Payable> Vencidas { get; } = new();
    public ObservableCollection<Payable> Hoje { get; } = new();
    public ObservableCollection<Payable> Proximas { get; } = new();

    public PayablesViewModel(IPayableService payableService)
    {
        _payableService = payableService;
        Title = "Contas a Pagar";
    }

    public async Task LoadAsync()
    {
        IsBusy = true;
        try
        {
            var grouped = await _payableService.GetGroupedAsync();
            UpdateCollection(Vencidas, grouped.GetValueOrDefault("Vencidas") ?? Array.Empty<Payable>());
            UpdateCollection(Hoje, grouped.GetValueOrDefault("Hoje") ?? Array.Empty<Payable>());
            UpdateCollection(Proximas, grouped.GetValueOrDefault("Próximas") ?? Array.Empty<Payable>());
        }
        finally
        {
            IsBusy = false;
        }
    }

    private static void UpdateCollection(ObservableCollection<Payable> collection, IReadOnlyList<Payable> items)
    {
        collection.Clear();
        foreach (var item in items)
        {
            collection.Add(item);
        }
    }

    [RelayCommand]
    private async Task AddPayableAsync()
    {
        await Shell.Current.GoToAsync(nameof(Views.PayableFormPage));
    }

    [RelayCommand]
    private async Task EditPayableAsync(Payable payable)
    {
        if (payable is null)
        {
            return;
        }

        await Shell.Current.GoToAsync(nameof(Views.PayableFormPage), new Dictionary<string, object>
        {
            ["Payable"] = payable
        });
    }

    [RelayCommand]
    private async Task MarkAsPaidAsync(Payable payable)
    {
        if (payable is null)
        {
            return;
        }

        var create = await Shell.Current.DisplayAlert("Confirmar", "Deseja gerar uma saída para esta conta?", "Sim", "Não");
        await _payableService.MarkAsPaidAsync(payable.Id, create);
        await LoadAsync();
    }
}
