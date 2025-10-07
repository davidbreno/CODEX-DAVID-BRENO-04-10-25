using System.Linq;
using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;
using SQLite;

namespace FinaceDavid.Data;

public class TransactionRepository : ITransactionRepository
{
    private readonly IDatabaseConnectionProvider _connectionProvider;

    public TransactionRepository(IDatabaseConnectionProvider connectionProvider)
    {
        _connectionProvider = connectionProvider;
    }

    public async Task<IReadOnlyList<Transaction>> GetByFilterAsync(DateTime start, DateTime end, TransactionType? type = null, string? categoria = null, string? search = null, bool includePending = true)
    {
        var connection = _connectionProvider.GetConnection();
        var query = connection.Table<Transaction>().Where(t => t.Data >= start && t.Data <= end);

        if (type.HasValue)
        {
            query = query.Where(t => t.Type == type.Value);
        }

        if (!string.IsNullOrWhiteSpace(categoria))
        {
            query = query.Where(t => t.Categoria.Contains(categoria));
        }

        if (!string.IsNullOrWhiteSpace(search))
        {
            query = query.Where(t => t.Descricao.Contains(search) || t.Categoria.Contains(search));
        }

        if (!includePending)
        {
            query = query.Where(t => t.Status == TransactionStatus.Pago);
        }

        var items = await query.OrderByDescending(t => t.Data).ToListAsync();
        return items;
    }

    public async Task<Transaction?> GetByIdAsync(int id)
    {
        var connection = _connectionProvider.GetConnection();
        return await connection.Table<Transaction>().FirstOrDefaultAsync(t => t.Id == id);
    }

    public async Task<int> InsertAsync(Transaction transaction)
    {
        var connection = _connectionProvider.GetConnection();
        transaction.CreatedAt = DateTime.UtcNow;
        transaction.UpdatedAt = DateTime.UtcNow;
        await connection.InsertAsync(transaction);
        return transaction.Id;
    }

    public async Task UpdateAsync(Transaction transaction)
    {
        var connection = _connectionProvider.GetConnection();
        transaction.UpdatedAt = DateTime.UtcNow;
        await connection.UpdateAsync(transaction);
    }

    public async Task DeleteAsync(Transaction transaction)
    {
        var connection = _connectionProvider.GetConnection();
        await connection.DeleteAsync(transaction);
    }

    public async Task<decimal> SumByTypeAsync(TransactionType type, DateTime start, DateTime end, bool includePending)
    {
        var connection = _connectionProvider.GetConnection();
        var query = connection.Table<Transaction>().Where(t => t.Type == type && t.Data >= start && t.Data <= end);

        if (!includePending)
        {
            query = query.Where(t => t.Status == TransactionStatus.Pago);
        }

        var items = await query.ToListAsync();
        return items.Sum(t => t.Valor);
    }
}
