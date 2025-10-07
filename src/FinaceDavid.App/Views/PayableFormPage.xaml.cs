using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;
using FinaceDavid.ViewModels;

namespace FinaceDavid.Views;

public partial class PayableFormPage : ContentPage, IQueryAttributable
{
    private readonly PayableFormViewModel _viewModel;

    public PayableFormPage(PayableFormViewModel viewModel)
    {
        InitializeComponent();
        BindingContext = _viewModel = viewModel;
        StatusPicker.ItemsSource = Enum.GetValues(typeof(PayableStatus)).Cast<PayableStatus>().ToList();
    }

    public void ApplyQueryAttributes(IDictionary<string, object> query)
    {
        if (query.TryGetValue("Payable", out var value) && value is Payable payable)
        {
            _viewModel.Load(payable);
        }
    }
}
