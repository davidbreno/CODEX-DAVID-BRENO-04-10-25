using FinaceDavid.Services.Models;

namespace FinaceDavid.Views.Drawables;

public class TrendChartDrawable : IDrawable
{
    public IEnumerable<TrendPoint> Points { get; set; } = Enumerable.Empty<TrendPoint>();

    public void Draw(ICanvas canvas, RectF dirtyRect)
    {
        canvas.FillColor = Colors.Transparent;
        canvas.FillRectangle(dirtyRect);

        var points = Points.ToList();
        if (!points.Any())
        {
            canvas.StrokeColor = Colors.Gray;
            canvas.StrokeSize = 2;
            canvas.DrawLine(dirtyRect.Left + 10, dirtyRect.Center.Y, dirtyRect.Right - 10, dirtyRect.Center.Y);
            return;
        }

        var max = points.Max(p => (float)Math.Max((double)p.Entrada, (double)p.Saida));
        if (max <= 0)
        {
            max = 1;
        }

        var stepX = dirtyRect.Width / Math.Max(points.Count - 1, 1);
        var startX = dirtyRect.Left;

        DrawArea(canvas, dirtyRect, points, stepX, startX, max, p => (float)p.Entrada, Colors.FromArgb("#0EA5E9"), 0.4f);
        DrawArea(canvas, dirtyRect, points, stepX, startX, max, p => (float)p.Saida, Colors.FromArgb("#3B82F6"), 0.3f);

        DrawLine(canvas, dirtyRect, points, stepX, startX, max, p => (float)p.Saldo, Colors.FromArgb("#2DD4BF"));
    }

    private static void DrawArea(ICanvas canvas, RectF rect, IReadOnlyList<TrendPoint> points, float stepX, float startX, float max, Func<TrendPoint, float> selector, Color color, float opacity)
    {
        var path = new PathF();
        path.MoveTo(startX, rect.Bottom);
        for (var i = 0; i < points.Count; i++)
        {
            var x = startX + i * stepX;
            var y = rect.Bottom - (selector(points[i]) / max) * rect.Height;
            path.LineTo(x, y);
        }
        path.LineTo(startX + (points.Count - 1) * stepX, rect.Bottom);
        path.Close();
        canvas.FillColor = color.WithAlpha(opacity);
        canvas.FillPath(path);
    }

    private static void DrawLine(ICanvas canvas, RectF rect, IReadOnlyList<TrendPoint> points, float stepX, float startX, float max, Func<TrendPoint, float> selector, Color color)
    {
        canvas.StrokeSize = 3;
        canvas.StrokeColor = color;
        for (var i = 0; i < points.Count - 1; i++)
        {
            var x1 = startX + i * stepX;
            var y1 = rect.Bottom - (selector(points[i]) / max) * rect.Height;
            var x2 = startX + (i + 1) * stepX;
            var y2 = rect.Bottom - (selector(points[i + 1]) / max) * rect.Height;
            canvas.DrawLine(x1, y1, x2, y2);
        }
    }
}
