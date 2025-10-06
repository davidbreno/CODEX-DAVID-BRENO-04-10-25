using FinaceDavid.ViewModels;

namespace FinaceDavid.Views;

public partial class SettingsPage : ContentPage
{
    private readonly SettingsViewModel _viewModel;

    public SettingsPage(SettingsViewModel viewModel)
    {
        InitializeComponent();
        BindingContext = _viewModel = viewModel;
    }

    private async void Switch_Toggled(object sender, ToggledEventArgs e)
    {
        await _viewModel.ToggleThemeCommand.ExecuteAsync(null);
    }
}
