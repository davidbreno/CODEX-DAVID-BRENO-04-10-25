namespace FinaceDavid.Services.Models;

public record TrendPoint(DateTime Date, decimal Entrada, decimal Saida)
{
    public decimal Saldo => Entrada - Saida;
}
