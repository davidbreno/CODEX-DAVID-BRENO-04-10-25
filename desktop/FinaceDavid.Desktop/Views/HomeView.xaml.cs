using FinaceDavid.Desktop.ViewModels;
using Microsoft.Extensions.DependencyInjection;
using System.Windows;
using System.Windows.Controls;

namespace FinaceDavid.Desktop.Views;

public partial class HomeView : UserControl
{
    private readonly HomeViewModel _viewModel;

    public HomeView()
    {
        InitializeComponent();
        _viewModel = App.Services.GetRequiredService<HomeViewModel>();
        DataContext = _viewModel;
    }

    private async void UserControl_Loaded(object sender, RoutedEventArgs e)
    {
        await _viewModel.LoadCommand.ExecuteAsync(null);
    }

    private async void Today_Click(object sender, RoutedEventArgs e)
    {
        await _viewModel.ChangePeriodCommand.ExecuteAsync(PeriodFilter.Day);
    }

    private async void Week_Click(object sender, RoutedEventArgs e)
    {
        await _viewModel.ChangePeriodCommand.ExecuteAsync(PeriodFilter.Week);
    }

    private async void Month_Click(object sender, RoutedEventArgs e)
    {
        await _viewModel.ChangePeriodCommand.ExecuteAsync(PeriodFilter.Month);
    }
}
