using CommunityToolkit.Mvvm.ComponentModel;

namespace FinaceDavid.ViewModels;

public abstract partial class ViewModelBase : ObservableValidator
{
    [ObservableProperty]
    private bool isBusy;

    [ObservableProperty]
    private string title = string.Empty;
}
