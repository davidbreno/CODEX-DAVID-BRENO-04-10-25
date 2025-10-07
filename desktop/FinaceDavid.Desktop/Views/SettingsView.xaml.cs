using FinaceDavid.Desktop.ViewModels;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Win32;
using System.Windows;
using System.Windows.Controls;

namespace FinaceDavid.Desktop.Views;

public partial class SettingsView : UserControl
{
    private readonly SettingsViewModel _viewModel;

    public SettingsView()
    {
        InitializeComponent();
        _viewModel = App.Services.GetRequiredService<SettingsViewModel>();
        DataContext = _viewModel;
    }

    private async void UserControl_Loaded(object sender, RoutedEventArgs e)
    {
        await _viewModel.LoadAsync();
    }

    private async void ToggleTheme_Click(object sender, RoutedEventArgs e)
    {
        await _viewModel.ToggleThemeCommand.ExecuteAsync(null);
    }

    private async void Export_Click(object sender, RoutedEventArgs e)
    {
        await _viewModel.ExportCommand.ExecuteAsync(null);
        MessageBox.Show($"Backup exportado em {_viewModel.BackupPath}", "FINACE DAVID", MessageBoxButton.OK, MessageBoxImage.Information);
    }

    private async void Import_Click(object sender, RoutedEventArgs e)
    {
        var dialog = new OpenFileDialog { Filter = "Backup (*.json)|*.json" };
        if (dialog.ShowDialog() == true)
        {
            await _viewModel.ImportCommand.ExecuteAsync(dialog.FileName);
            MessageBox.Show("Backup importado com sucesso!", "FINACE DAVID", MessageBoxButton.OK, MessageBoxImage.Information);
        }
    }
}
