namespace FinaceDavid.Services;

public interface IThemeService
{
    Task ApplySavedThemeAsync();
    AppTheme GetCurrentTheme();
    Task ToggleThemeAsync();
}
