using FinaceDavid.Services;
using FinaceDavid.Views;

namespace FinaceDavid;

public partial class App : Application
{
    private readonly IThemeService _themeService;
    private readonly IServiceProvider _serviceProvider;

    public App(IThemeService themeService, IServiceProvider serviceProvider)
    {
        InitializeComponent();
        _themeService = themeService;
        _serviceProvider = serviceProvider;

        MainPage = _serviceProvider.GetRequiredService<LoginPage>();
    }

    protected override async void OnStart()
    {
        base.OnStart();
        await _themeService.ApplySavedThemeAsync();
    }

    public Task ShowHomeAsync()
    {
        return MainThread.InvokeOnMainThreadAsync(() =>
        {
            MainPage = _serviceProvider.GetRequiredService<AppShell>();
        });
    }
}
