using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using FinaceDavid.Desktop.Services;
using System.Threading.Tasks;

namespace FinaceDavid.Desktop.ViewModels;

public partial class SettingsViewModel : ViewModelBase
{
    private readonly PreferenceService _preferenceService;
    private readonly BackupService _backupService;

    [ObservableProperty]
    private string _theme = "Dark";

    [ObservableProperty]
    private string? _backupPath;

    public SettingsViewModel(PreferenceService preferenceService, BackupService backupService)
    {
        _preferenceService = preferenceService;
        _backupService = backupService;
    }

    public async Task LoadAsync()
    {
        Theme = await _preferenceService.GetThemeAsync();
    }

    [RelayCommand]
    public async Task ToggleThemeAsync()
    {
        Theme = Theme == "Dark" ? "Light" : "Dark";
        await _preferenceService.SaveThemeAsync(Theme);
    }

    [RelayCommand]
    public async Task ExportAsync()
    {
        BackupPath = await _backupService.ExportAsync();
    }

    [RelayCommand]
    public async Task ImportAsync(string path)
    {
        await _backupService.ImportAsync(path);
    }
}
