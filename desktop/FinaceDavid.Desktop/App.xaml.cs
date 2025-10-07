using FinaceDavid.Desktop.Data;
using FinaceDavid.Desktop.Services;
using FinaceDavid.Desktop.ViewModels;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.IO;
using System.Windows;

namespace FinaceDavid.Desktop;

public partial class App : Application
{
    public static IServiceProvider Services { get; private set; } = null!;

    protected override void OnStartup(StartupEventArgs e)
    {
        base.OnStartup(e);

        var services = new ServiceCollection();
        ConfigureServices(services);
        Services = services.BuildServiceProvider();

        var dbInitializer = Services.GetRequiredService<DatabaseInitializer>();
        dbInitializer.Initialize();
    }

    private static void ConfigureServices(ServiceCollection services)
    {
        var dataPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), "FinaceDavid");
        if (!Directory.Exists(dataPath))
        {
            Directory.CreateDirectory(dataPath);
        }

        services.AddSingleton(new DatabaseOptions(Path.Combine(dataPath, "finacedavid.db")));
        services.AddSingleton<AppDbContext>();

        services.AddSingleton<DatabaseInitializer>();
        services.AddSingleton<PasswordHasher>();
        services.AddSingleton<AuthService>();
        services.AddSingleton<TransactionService>();
        services.AddSingleton<PayableService>();
        services.AddSingleton<PreferenceService>();
        services.AddSingleton<BackupService>();
        services.AddSingleton<CalendarService>();

        services.AddTransient<LoginViewModel>();
        services.AddTransient<HomeViewModel>();
        services.AddTransient<TransactionsViewModel>();
        services.AddTransient<PayablesViewModel>();
        services.AddTransient<CalendarViewModel>();
        services.AddTransient<SettingsViewModel>();
    }
}
