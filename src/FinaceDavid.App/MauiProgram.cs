using CommunityToolkit.Maui;
using FinaceDavid.Data;
using FinaceDavid.Services;
using FinaceDavid.ViewModels;
using FinaceDavid.Views;

namespace FinaceDavid;

public static class MauiProgram
{
    public static MauiApp CreateMauiApp()
    {
        var builder = MauiApp.CreateBuilder();
        builder
            .UseMauiApp<App>()
            .UseMauiCommunityToolkit();

        builder.Services.AddSingleton<App>();
        builder.Services.AddSingleton<IDatabasePathProvider, DatabasePathProvider>();
        builder.Services.AddSingleton<DatabaseInitializer>();
        builder.Services.AddSingleton<IDatabaseInitializer>(sp => sp.GetRequiredService<DatabaseInitializer>());
        builder.Services.AddSingleton<IDatabaseConnectionProvider>(sp => sp.GetRequiredService<DatabaseInitializer>());
        builder.Services.AddSingleton<IFilterStateService, FilterStateService>();
        builder.Services.AddSingleton<IThemeService, ThemeService>();
        builder.Services.AddSingleton<IAuthenticationService, AuthenticationService>();
        builder.Services.AddSingleton<ITransactionRepository, TransactionRepository>();
        builder.Services.AddSingleton<IPayableRepository, PayableRepository>();
        builder.Services.AddSingleton<IUserRepository, UserRepository>();
        builder.Services.AddSingleton<ITransactionService, TransactionService>();
        builder.Services.AddSingleton<IPayableService, PayableService>();
        builder.Services.AddSingleton<IReportService, ReportService>();
        builder.Services.AddSingleton<ICalendarService, CalendarService>();

        builder.Services.AddSingleton<LoginViewModel>();
        builder.Services.AddSingleton<HomeViewModel>();
        builder.Services.AddSingleton<TransactionsViewModel>();
        builder.Services.AddSingleton<PayablesViewModel>();
        builder.Services.AddSingleton<CalendarViewModel>();
        builder.Services.AddTransient<TransactionFormViewModel>();
        builder.Services.AddTransient<PayableFormViewModel>();
        builder.Services.AddSingleton<SettingsViewModel>();

        builder.Services.AddSingleton<LoginPage>();
        builder.Services.AddSingleton<AppShell>();
        builder.Services.AddSingleton<HomePage>();
        builder.Services.AddSingleton<TransactionsPage>();
        builder.Services.AddSingleton<PayablesPage>();
        builder.Services.AddSingleton<CalendarPage>();
        builder.Services.AddTransient<TransactionFormPage>();
        builder.Services.AddTransient<PayableFormPage>();
        builder.Services.AddSingleton<SettingsPage>();

        using var scope = builder.Services.BuildServiceProvider().CreateScope();
        var initializer = scope.ServiceProvider.GetRequiredService<IDatabaseInitializer>();
        initializer.InitializeAsync().GetAwaiter().GetResult();

        return builder.Build();
    }
}
