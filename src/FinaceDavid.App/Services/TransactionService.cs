using System.Linq;
using FinaceDavid.Data;
using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;
using FinaceDavid.Services.Models;

namespace FinaceDavid.Services;

public class TransactionService : ITransactionService
{
    private readonly ITransactionRepository _transactionRepository;

    public TransactionService(ITransactionRepository transactionRepository)
    {
        _transactionRepository = transactionRepository;
    }

    public Task<IReadOnlyList<Transaction>> GetTransactionsAsync(TransactionFilter filter)
        => _transactionRepository.GetByFilterAsync(filter.Range.Start, filter.Range.End, filter.Type, filter.Categoria, filter.Search, filter.IncludePending);

    public Task<Transaction?> GetByIdAsync(int id) => _transactionRepository.GetByIdAsync(id);

    public async Task<int> SaveAsync(Transaction transaction)
    {
        if (transaction.Id == 0)
        {
            return await _transactionRepository.InsertAsync(transaction);
        }

        await _transactionRepository.UpdateAsync(transaction);
        return transaction.Id;
    }

    public async Task DeleteAsync(int id)
    {
        var existing = await _transactionRepository.GetByIdAsync(id);
        if (existing is not null)
        {
            await _transactionRepository.DeleteAsync(existing);
        }
    }

    public Task<decimal> GetTotalAsync(TransactionType type, TransactionFilter filter)
        => _transactionRepository.SumByTypeAsync(type, filter.Range.Start, filter.Range.End, filter.IncludePending);

    public async Task<decimal> GetBalanceAsync(TransactionFilter filter)
    {
        var entradas = await GetTotalAsync(TransactionType.Entrada, filter);
        var saidas = await GetTotalAsync(TransactionType.Saida, filter);
        return entradas - saidas;
    }

    public async Task<IReadOnlyDictionary<string, decimal>> GetTotalsByCategoryAsync(TransactionFilter filter)
    {
        var transactions = await GetTransactionsAsync(filter);
        return transactions
            .GroupBy(t => string.IsNullOrWhiteSpace(t.Categoria) ? "Outros" : t.Categoria)
            .ToDictionary(g => g.Key, g => g.Sum(t => t.Valor));
    }

    public async Task<IReadOnlyList<TrendPoint>> GetTrendAsync(TransactionFilter filter)
    {
        var transactions = await GetTransactionsAsync(filter);
        var grouped = transactions
            .GroupBy(t => t.Data.Date)
            .OrderBy(g => g.Key)
            .Select(g => new TrendPoint(
                g.Key,
                g.Where(t => t.Type == TransactionType.Entrada).Sum(t => t.Valor),
                g.Where(t => t.Type == TransactionType.Saida).Sum(t => t.Valor)))
            .ToList();

        return grouped;
    }
}
