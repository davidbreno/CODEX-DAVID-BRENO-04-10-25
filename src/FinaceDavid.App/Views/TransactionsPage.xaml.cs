using FinaceDavid.Domain.Enums;
using FinaceDavid.ViewModels;

namespace FinaceDavid.Views;

public partial class TransactionsPage : ContentPage, IQueryAttributable
{
    private readonly TransactionsViewModel _viewModel;

    public TransactionsPage(TransactionsViewModel viewModel)
    {
        InitializeComponent();
        BindingContext = _viewModel = viewModel;
    }

    protected override async void OnAppearing()
    {
        base.OnAppearing();
        await _viewModel.LoadAsync();
    }

    public void ApplyQueryAttributes(IDictionary<string, object> query)
    {
        if (query.TryGetValue("type", out var typeObj) && typeObj is string typeValue)
        {
            if (Enum.TryParse<TransactionType>(typeValue, out var type))
            {
                _viewModel.SelectedType = type;
            }
            else
            {
                _viewModel.SelectedType = null;
            }
        }
    }
}
