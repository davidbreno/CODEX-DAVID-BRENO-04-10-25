using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;
using FinaceDavid.ViewModels;

namespace FinaceDavid.Views;

public partial class TransactionFormPage : ContentPage, IQueryAttributable
{
    private readonly TransactionFormViewModel _viewModel;

    public TransactionFormPage(TransactionFormViewModel viewModel)
    {
        InitializeComponent();
        BindingContext = _viewModel = viewModel;
        TypePicker.ItemsSource = Enum.GetValues(typeof(TransactionType)).Cast<TransactionType>().ToList();
        StatusPicker.ItemsSource = Enum.GetValues(typeof(TransactionStatus)).Cast<TransactionStatus>().ToList();
    }

    public void ApplyQueryAttributes(IDictionary<string, object> query)
    {
        if (query.TryGetValue("Transaction", out var value) && value is Transaction transaction)
        {
            _viewModel.LoadTransaction(transaction);
        }
    }
}
