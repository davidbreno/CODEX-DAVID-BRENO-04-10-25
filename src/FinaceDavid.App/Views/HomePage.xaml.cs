using System.Collections.Specialized;
using FinaceDavid.ViewModels;
using FinaceDavid.Views.Drawables;

namespace FinaceDavid.Views;

public partial class HomePage : ContentPage
{
    private readonly HomeViewModel _viewModel;
    private readonly DonutChartDrawable _donutDrawable = new();
    private readonly TrendChartDrawable _trendDrawable = new();

    public HomePage(HomeViewModel viewModel)
    {
        InitializeComponent();
        BindingContext = _viewModel = viewModel;
        DonutView.Drawable = _donutDrawable;
        TrendView.Drawable = _trendDrawable;
        _viewModel.DonutSlices.CollectionChanged += OnSlicesChanged;
        _viewModel.TrendPoints.CollectionChanged += OnTrendChanged;
    }

    protected override async void OnAppearing()
    {
        base.OnAppearing();
        await _viewModel.LoadAsync();
        UpdateSlices();
        UpdateTrend();
    }

    private void OnSlicesChanged(object? sender, NotifyCollectionChangedEventArgs e) => UpdateSlices();

    private void OnTrendChanged(object? sender, NotifyCollectionChangedEventArgs e) => UpdateTrend();

    private void UpdateSlices()
    {
        _donutDrawable.Slices = _viewModel.DonutSlices.ToList();
        DonutView.Invalidate();
    }

    private void UpdateTrend()
    {
        _trendDrawable.Points = _viewModel.TrendPoints.ToList();
        TrendView.Invalidate();
    }
}
