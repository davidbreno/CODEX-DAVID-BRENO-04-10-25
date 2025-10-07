using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace FinaceDavid.Desktop.Models;

public enum TransactionType
{
    Entrada,
    Saida
}

public enum TransactionStatus
{
    Pago,
    Pendente
}

public class Transaction
{
    [Key]
    public int Id { get; set; }
    [Required]
    public TransactionType Type { get; set; }
    [Column(TypeName = "decimal(18,2)")]
    public decimal Amount { get; set; }
    public DateTime Date { get; set; }
    public string Category { get; set; } = string.Empty;
    public string Description { get; set; } = string.Empty;
    public TransactionStatus Status { get; set; }
    public string? AttachmentPath { get; set; }
    public DateTime CreatedAt { get; set; }
    public DateTime UpdatedAt { get; set; }
}
