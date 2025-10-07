using FinaceDavid.ViewModels;

namespace FinaceDavid.Views;

public partial class PayablesPage : ContentPage
{
    private readonly PayablesViewModel _viewModel;

    public PayablesPage(PayablesViewModel viewModel)
    {
        InitializeComponent();
        BindingContext = _viewModel = viewModel;
    }

    protected override async void OnAppearing()
    {
        base.OnAppearing();
        await _viewModel.LoadAsync();
    }
}
