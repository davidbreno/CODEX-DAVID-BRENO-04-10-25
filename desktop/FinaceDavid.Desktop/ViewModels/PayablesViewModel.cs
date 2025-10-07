using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using FinaceDavid.Desktop.Models;
using FinaceDavid.Desktop.Services;
using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading.Tasks;

namespace FinaceDavid.Desktop.ViewModels;

public partial class PayablesViewModel : ViewModelBase
{
    private readonly PayableService _payableService;

    [ObservableProperty]
    private ObservableCollection<Payable> _overdue = new();

    [ObservableProperty]
    private ObservableCollection<Payable> _dueToday = new();

    [ObservableProperty]
    private ObservableCollection<Payable> _upcoming = new();

    public PayablesViewModel(PayableService payableService)
    {
        _payableService = payableService;
    }

    [RelayCommand]
    public async Task LoadAsync()
    {
        var items = await _payableService.GetAllAsync();
        Overdue = new ObservableCollection<Payable>(items.Where(p => p.DueDate.Date < DateTime.Today));
        DueToday = new ObservableCollection<Payable>(items.Where(p => p.DueDate.Date == DateTime.Today));
        Upcoming = new ObservableCollection<Payable>(items.Where(p => p.DueDate.Date > DateTime.Today));
    }

    [RelayCommand]
    public async Task SaveAsync(Payable payable)
    {
        await _payableService.SaveAsync(payable);
        await LoadAsync();
    }

    [RelayCommand]
    public async Task MarkAsPaidAsync(Payable payable)
    {
        await _payableService.MarkAsPaidAsync(payable);
        await LoadAsync();
    }
}
