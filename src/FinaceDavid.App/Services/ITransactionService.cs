using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;
using FinaceDavid.Services.Models;

namespace FinaceDavid.Services;

public interface ITransactionService
{
    Task<IReadOnlyList<Transaction>> GetTransactionsAsync(TransactionFilter filter);
    Task<Transaction?> GetByIdAsync(int id);
    Task<int> SaveAsync(Transaction transaction);
    Task DeleteAsync(int id);
    Task<decimal> GetTotalAsync(TransactionType type, TransactionFilter filter);
    Task<decimal> GetBalanceAsync(TransactionFilter filter);
    Task<IReadOnlyDictionary<string, decimal>> GetTotalsByCategoryAsync(TransactionFilter filter);
    Task<IReadOnlyList<TrendPoint>> GetTrendAsync(TransactionFilter filter);
}
