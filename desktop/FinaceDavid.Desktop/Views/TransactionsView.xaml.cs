using FinaceDavid.Desktop.ViewModels;
using Microsoft.Extensions.DependencyInjection;
using System.Windows;
using System.Windows.Controls;

namespace FinaceDavid.Desktop.Views;

public partial class TransactionsView : UserControl
{
    private readonly TransactionsViewModel _viewModel;

    public TransactionsView()
    {
        InitializeComponent();
        _viewModel = App.Services.GetRequiredService<TransactionsViewModel>();
        DataContext = _viewModel;
    }

    private async void UserControl_Loaded(object sender, RoutedEventArgs e)
    {
        await _viewModel.LoadCommand.ExecuteAsync(null);
    }

    private async void Filter_Click(object sender, RoutedEventArgs e)
    {
        await _viewModel.RefreshCommand.ExecuteAsync(null);
    }
}
