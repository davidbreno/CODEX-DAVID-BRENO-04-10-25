using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using FinaceDavid.Services;

namespace FinaceDavid.ViewModels;

public partial class SettingsViewModel : ViewModelBase
{
    private readonly IThemeService _themeService;
    private readonly IAuthenticationService _authenticationService;

    [ObservableProperty]
    private bool isDarkTheme;

    [ObservableProperty]
    private string currentLocale = "pt-BR";

    [ObservableProperty]
    private string? newPassword;

    [ObservableProperty]
    private string? currentPassword;

    public SettingsViewModel(IThemeService themeService, IAuthenticationService authenticationService)
    {
        _themeService = themeService;
        _authenticationService = authenticationService;
        Title = "Configurações";
        IsDarkTheme = _themeService.GetCurrentTheme() == AppTheme.Dark;
    }

    [RelayCommand]
    private async Task ToggleThemeAsync()
    {
        await _themeService.ToggleThemeAsync();
        IsDarkTheme = _themeService.GetCurrentTheme() == AppTheme.Dark;
    }

    [RelayCommand]
    private async Task ChangePasswordAsync()
    {
        if (string.IsNullOrWhiteSpace(CurrentPassword) || string.IsNullOrWhiteSpace(NewPassword))
        {
            await Shell.Current.DisplayAlert("Atenção", "Informe as senhas", "Ok");
            return;
        }

        if (await _authenticationService.ChangePasswordAsync(CurrentPassword, NewPassword))
        {
            await Shell.Current.DisplayAlert("Sucesso", "Senha atualizada", "Ok");
            CurrentPassword = string.Empty;
            NewPassword = string.Empty;
        }
        else
        {
            await Shell.Current.DisplayAlert("Erro", "Senha atual incorreta", "Ok");
        }
    }
}
