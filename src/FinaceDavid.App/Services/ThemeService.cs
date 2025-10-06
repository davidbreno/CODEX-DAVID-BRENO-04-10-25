namespace FinaceDavid.Services;

public class ThemeService : IThemeService
{
    private const string ThemePreferenceKey = "finacedavid_theme";
    private const AppTheme DefaultTheme = AppTheme.Dark;

    public async Task ApplySavedThemeAsync()
    {
        var themeValue = Preferences.Get(ThemePreferenceKey, DefaultTheme.ToString());
        if (!Enum.TryParse<AppTheme>(themeValue, out var theme))
        {
            theme = DefaultTheme;
        }

        Application.Current!.UserAppTheme = theme;
        await Task.CompletedTask;
    }

    public AppTheme GetCurrentTheme()
    {
        return Application.Current?.UserAppTheme ?? DefaultTheme;
    }

    public async Task ToggleThemeAsync()
    {
        var current = GetCurrentTheme();
        var next = current == AppTheme.Dark ? AppTheme.Light : AppTheme.Dark;
        Application.Current!.UserAppTheme = next;
        Preferences.Set(ThemePreferenceKey, next.ToString());
        await Task.CompletedTask;
    }
}
