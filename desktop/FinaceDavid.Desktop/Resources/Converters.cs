using FinaceDavid.Desktop.Models;
using FinaceDavid.Desktop.ViewModels;
using OxyPlot;
using OxyPlot.Axes;
using OxyPlot.Series;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Windows;
using System.Windows.Data;
using System.Windows.Media;

namespace FinaceDavid.Desktop.Resources;

public class BoolToVisibilityConverter : IValueConverter
{
    public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
    {
        return value is bool flag && flag ? Visibility.Visible : Visibility.Collapsed;
    }

    public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture) => throw new NotImplementedException();
}

public class StringToVisibilityConverter : IValueConverter
{
    public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
    {
        return value is string text && !string.IsNullOrWhiteSpace(text) ? Visibility.Visible : Visibility.Collapsed;
    }

    public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture) => throw new NotImplementedException();
}

public class TrendToPlotModelConverter : IValueConverter
{
    public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
    {
        var points = value as IEnumerable<ChartPoint> ?? Enumerable.Empty<ChartPoint>();
        var model = new PlotModel { Background = OxyColors.Transparent };
        var categoryAxis = new CategoryAxis { Position = AxisPosition.Bottom };
        foreach (var point in points)
        {
            categoryAxis.Labels.Add(point.Label);
        }
        model.Axes.Add(categoryAxis);
        model.Axes.Add(new LinearAxis { Position = AxisPosition.Left, Title = "Saldo", MajorGridlineStyle = LineStyle.Solid, MinorGridlineStyle = LineStyle.Dot, TextColor = OxyColor.FromRgb(200, 205, 216) });

        var series = new AreaSeries { Color = OxyColor.Parse("#20C4F4"), MarkerType = MarkerType.Circle };
        int index = 0;
        foreach (var point in points)
        {
            series.Points.Add(new DataPoint(index, (double)point.Value));
            series.Points2.Add(new DataPoint(index, 0));
            index++;
        }
        model.Series.Add(series);
        return model;
    }

    public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture) => throw new NotImplementedException();
}

public class DonutToPlotModelConverter : IValueConverter
{
    public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
    {
        var points = value as IEnumerable<ChartPoint> ?? Enumerable.Empty<ChartPoint>();
        var model = new PlotModel { Background = OxyColors.Transparent };
        var series = new PieSeries { InnerDiameter = 0.6, StrokeThickness = 2, InsideLabelFormat = "{1}: {0:C}" };
        foreach (var point in points)
        {
            series.Slices.Add(new PieSlice(point.Label, (double)point.Value));
        }
        model.Series.Add(series);
        return model;
    }

    public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture) => throw new NotImplementedException();
}

public class TransactionTypeToBrushConverter : IValueConverter
{
    public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
    {
        if (value is TransactionType type)
        {
            return type == TransactionType.Entrada ? Brushes.LightGreen : Brushes.IndianRed;
        }

        return Brushes.White;
    }

    public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture) => throw new NotImplementedException();
}
