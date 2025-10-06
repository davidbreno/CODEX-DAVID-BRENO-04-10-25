using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;

namespace FinaceDavid.Data;

public interface ITransactionRepository
{
    Task<IReadOnlyList<Transaction>> GetByFilterAsync(DateTime start, DateTime end, TransactionType? type = null, string? categoria = null, string? search = null, bool includePending = true);
    Task<Transaction?> GetByIdAsync(int id);
    Task<int> InsertAsync(Transaction transaction);
    Task UpdateAsync(Transaction transaction);
    Task DeleteAsync(Transaction transaction);
    Task<decimal> SumByTypeAsync(TransactionType type, DateTime start, DateTime end, bool includePending);
}
