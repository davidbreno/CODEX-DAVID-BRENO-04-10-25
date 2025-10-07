using FinaceDavid.Desktop.ViewModels;
using Microsoft.Extensions.DependencyInjection;
using System.Windows;

namespace FinaceDavid.Desktop.Views;

public partial class LoginWindow : Window
{
    private readonly LoginViewModel _viewModel;

    public LoginWindow()
    {
        InitializeComponent();
        _viewModel = App.Services.GetRequiredService<LoginViewModel>();
        DataContext = _viewModel;
    }

    private void Window_Loaded(object sender, RoutedEventArgs e)
    {
    }

    private void PasswordBox_OnPasswordChanged(object sender, RoutedEventArgs e)
    {
        if (sender is System.Windows.Controls.PasswordBox passwordBox)
        {
            _viewModel.Password = passwordBox.Password;
        }
    }

    private async void LoginButton_OnClick(object sender, RoutedEventArgs e)
    {
        await _viewModel.LoginCommand.ExecuteAsync(null);
        if (_viewModel.IsAuthenticated)
        {
            OpenMain();
        }
    }

    private async void RegisterButton_OnClick(object sender, RoutedEventArgs e)
    {
        await _viewModel.RegisterCommand.ExecuteAsync(null);
        if (string.IsNullOrEmpty(_viewModel.ErrorMessage))
        {
            MessageBox.Show("Usuário criado com sucesso!", "FINACE DAVID", MessageBoxButton.OK, MessageBoxImage.Information);
        }
    }

    private void OpenMain()
    {
        var mainWindow = new MainWindow();
        mainWindow.Show();
        Close();
    }
}
