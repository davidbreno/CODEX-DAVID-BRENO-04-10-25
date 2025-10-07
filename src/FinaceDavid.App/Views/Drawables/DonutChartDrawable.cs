using FinaceDavid.Services.Models;

namespace FinaceDavid.Views.Drawables;

public class DonutChartDrawable : IDrawable
{
    public IEnumerable<ChartSlice> Slices { get; set; } = Enumerable.Empty<ChartSlice>();

    public void Draw(ICanvas canvas, RectF dirtyRect)
    {
        canvas.FillColor = Colors.Transparent;
        canvas.FillRectangle(dirtyRect);

        var total = Slices.Sum(s => (float)s.Value);
        if (total <= 0)
        {
            canvas.StrokeColor = Colors.Gray;
            canvas.StrokeSize = 4;
            canvas.DrawEllipse(dirtyRect.Center.X - 50, dirtyRect.Center.Y - 50, 100, 100);
            return;
        }

        var startAngle = -90f;
        var radius = Math.Min(dirtyRect.Width, dirtyRect.Height) / 2 - 10;
        var center = dirtyRect.Center;

        foreach (var slice in Slices)
        {
            var sweep = (float)(slice.Value / (decimal)total) * 360f;
            canvas.FillColor = slice.Color;
            canvas.StrokeSize = 0;
            canvas.FillArc(center.X - radius, center.Y - radius, radius * 2, radius * 2, startAngle, sweep, true);
            startAngle += sweep;
        }

        var innerRadius = radius * 0.6f;
        canvas.FillColor = Colors.FromArgb("#1F2933");
        canvas.FillEllipse(center.X - innerRadius, center.Y - innerRadius, innerRadius * 2, innerRadius * 2);
    }
}
