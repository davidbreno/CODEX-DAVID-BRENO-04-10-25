using FinaceDavid.Views;

namespace FinaceDavid;

public partial class AppShell : Shell
{
    public AppShell()
    {
        InitializeComponent();
        Routing.RegisterRoute(nameof(TransactionFormPage), typeof(TransactionFormPage));
        Routing.RegisterRoute(nameof(PayableFormPage), typeof(PayableFormPage));
    }
}
