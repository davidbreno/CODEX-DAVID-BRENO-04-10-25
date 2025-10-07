using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using FinaceDavid.Desktop.Services;
using System.Threading.Tasks;

namespace FinaceDavid.Desktop.ViewModels;

public partial class LoginViewModel : ViewModelBase
{
    private readonly AuthService _authService;

    [ObservableProperty]
    private string _username = string.Empty;

    [ObservableProperty]
    private string _password = string.Empty;

    [ObservableProperty]
    private bool _isBusy;

    [ObservableProperty]
    private string? _errorMessage;

    [ObservableProperty]
    private bool _isAuthenticated;

    public LoginViewModel(AuthService authService)
    {
        _authService = authService;
    }

    [RelayCommand]
    private async Task LoginAsync()
    {
        if (IsBusy)
        {
            return;
        }

        IsBusy = true;
        ErrorMessage = null;
        IsAuthenticated = false;

        try
        {
            var result = await _authService.LoginAsync(Username, Password);
            if (!result)
            {
                ErrorMessage = "Credenciais inválidas";
            }
            else
            {
                IsAuthenticated = true;
            }
        }
        finally
        {
            IsBusy = false;
            Password = string.Empty;
        }
    }

    [RelayCommand]
    private async Task RegisterAsync()
    {
        if (IsBusy)
        {
            return;
        }

        IsBusy = true;
        ErrorMessage = null;

        try
        {
            var result = await _authService.RegisterAsync(Username, Password);
            if (!result)
            {
                ErrorMessage = "Usuário já existe";
            }
        }
        finally
        {
            IsBusy = false;
            Password = string.Empty;
        }
    }
}
