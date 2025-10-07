namespace FinaceDavid.Services.Models;

public record DailySummary(DateTime Date, decimal TotalEntrada, decimal TotalSaida)
{
    public decimal Saldo => TotalEntrada - TotalSaida;
}
