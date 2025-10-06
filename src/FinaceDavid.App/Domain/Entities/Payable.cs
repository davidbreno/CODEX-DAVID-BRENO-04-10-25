using FinaceDavid.Domain.Enums;
using SQLite;

namespace FinaceDavid.Domain.Entities;

[Table("Payables")]
public class Payable
{
    [PrimaryKey, AutoIncrement]
    public int Id { get; set; }

    [NotNull]
    public string Titulo { get; set; } = string.Empty;

    [NotNull]
    public decimal Valor { get; set; }

    [NotNull]
    public DateTime Vencimento { get; set; }

    [NotNull]
    public PayableStatus Status { get; set; } = PayableStatus.Pendente;

    public string Nota { get; set; } = string.Empty;

    [NotNull]
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

    [NotNull]
    public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;
}
