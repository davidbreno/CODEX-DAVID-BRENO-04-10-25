using FinaceDavid.Domain.Enums;

namespace FinaceDavid.Services.Models;

public record TransactionFilter(
    DateRange Range,
    TransactionType? Type = null,
    string? Categoria = null,
    string? Search = null,
    bool IncludePending = true);
