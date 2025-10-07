using FinaceDavid.Desktop.Models;
using FinaceDavid.Desktop.ViewModels;
using Microsoft.Extensions.DependencyInjection;
using System.Windows;
using System.Windows.Controls;

namespace FinaceDavid.Desktop.Views;

public partial class PayablesView : UserControl
{
    private readonly PayablesViewModel _viewModel;

    public PayablesView()
    {
        InitializeComponent();
        _viewModel = App.Services.GetRequiredService<PayablesViewModel>();
        DataContext = _viewModel;
    }

    private async void UserControl_Loaded(object sender, RoutedEventArgs e)
    {
        await _viewModel.LoadCommand.ExecuteAsync(null);
    }

    private async void MarkPaid_Click(object sender, RoutedEventArgs e)
    {
        var selected = OverdueList.SelectedItem as Payable ?? TodayList.SelectedItem as Payable ?? UpcomingList.SelectedItem as Payable;
        if (selected != null)
        {
            await _viewModel.MarkAsPaidCommand.ExecuteAsync(selected);
        }
    }
}
