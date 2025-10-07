using FinaceDavid.Desktop.ViewModels;
using Microsoft.Extensions.DependencyInjection;
using System.Windows;
using System.Windows.Controls;

namespace FinaceDavid.Desktop.Views;

public partial class CalendarView : UserControl
{
    private readonly CalendarViewModel _viewModel;

    public CalendarView()
    {
        InitializeComponent();
        _viewModel = App.Services.GetRequiredService<CalendarViewModel>();
        DataContext = _viewModel;
    }

    private async void UserControl_Loaded(object sender, RoutedEventArgs e)
    {
        await _viewModel.LoadAsync();
    }

    private async void Previous_Click(object sender, RoutedEventArgs e)
    {
        await _viewModel.ChangeMonthAsync(-1);
    }

    private async void Next_Click(object sender, RoutedEventArgs e)
    {
        await _viewModel.ChangeMonthAsync(1);
    }
}
