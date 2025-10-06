using System.ComponentModel.DataAnnotations;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using FinaceDavid.Services;

namespace FinaceDavid.ViewModels;

public partial class LoginViewModel : ViewModelBase
{
    private readonly IAuthenticationService _authenticationService;
    private readonly App _app;

    [ObservableProperty]
    [NotifyDataErrorInfo]
    [Required(ErrorMessage = "Informe uma senha ou PIN")]
    [MinLength(4, ErrorMessage = "Mínimo de 4 dígitos")]
    [MaxLength(12, ErrorMessage = "Máximo de 12 caracteres")]
    private string password = string.Empty;

    [ObservableProperty]
    private string confirmPassword = string.Empty;

    [ObservableProperty]
    private bool isFirstAccess;

    [ObservableProperty]
    private string statusMessage = string.Empty;

    public LoginViewModel(IAuthenticationService authenticationService, App app)
    {
        _authenticationService = authenticationService;
        _app = app;
        Title = "Acessar";
    }

    public async Task InitializeAsync()
    {
        IsBusy = true;
        try
        {
            IsFirstAccess = !await _authenticationService.HasCredentialAsync();
        }
        finally
        {
            IsBusy = false;
        }
    }

    [RelayCommand]
    private async Task AuthenticateAsync()
    {
        if (IsFirstAccess)
        {
            await RegisterAsync();
            return;
        }

        if (!ValidatePassword())
        {
            return;
        }

        IsBusy = true;
        try
        {
            if (await _authenticationService.ValidateAsync(Password))
            {
                Password = string.Empty;
                await _app.ShowHomeAsync();
                StatusMessage = string.Empty;
            }
            else
            {
                StatusMessage = "Credenciais inválidas";
            }
        }
        finally
        {
            IsBusy = false;
        }
    }

    private bool ValidatePassword()
    {
        if (string.IsNullOrWhiteSpace(Password))
        {
            StatusMessage = "Informe a senha ou PIN";
            return false;
        }

        return true;
    }

    private async Task RegisterAsync()
    {
        if (string.IsNullOrWhiteSpace(Password) || string.IsNullOrWhiteSpace(ConfirmPassword))
        {
            StatusMessage = "Defina e confirme a senha";
            return;
        }

        if (!string.Equals(Password, ConfirmPassword, StringComparison.Ordinal))
        {
            StatusMessage = "As senhas não conferem";
            return;
        }

        IsBusy = true;
        try
        {
            if (await _authenticationService.RegisterAsync(Password))
            {
                StatusMessage = "Senha criada! Faça login";
                IsFirstAccess = false;
                ConfirmPassword = string.Empty;
                Password = string.Empty;
            }
            else
            {
                StatusMessage = "Não foi possível registrar";
            }
        }
        finally
        {
            IsBusy = false;
        }
    }
}
