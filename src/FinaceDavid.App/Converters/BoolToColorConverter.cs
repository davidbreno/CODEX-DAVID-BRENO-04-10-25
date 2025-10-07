using System.Globalization;

namespace FinaceDavid.Converters;

public class BoolToColorConverter : IValueConverter
{
    public object Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        var has = value is bool b && b;
        if (parameter is string param && param == "has")
        {
            return has ? Color.FromArgb("#1E293B") : Color.FromArgb("#111827");
        }

        return has ? Color.FromArgb("#38BDF8") : Color.FromArgb("#1F2937");
    }

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture) => throw new NotImplementedException();
}
