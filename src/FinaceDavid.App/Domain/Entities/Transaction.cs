using FinaceDavid.Domain.Enums;
using SQLite;

namespace FinaceDavid.Domain.Entities;

[Table("Transactions")]
public class Transaction
{
    [PrimaryKey, AutoIncrement]
    public int Id { get; set; }

    [NotNull]
    public TransactionType Type { get; set; }

    [NotNull]
    public decimal Valor { get; set; }

    [NotNull]
    public DateTime Data { get; set; }

    public string Categoria { get; set; } = string.Empty;

    public string Descricao { get; set; } = string.Empty;

    public string? AnexoPath { get; set; }

    [NotNull]
    public TransactionStatus Status { get; set; } = TransactionStatus.Pago;

    [NotNull]
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

    [NotNull]
    public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;
}
