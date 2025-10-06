using System.Linq;
using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;

namespace FinaceDavid.Data;

public class PayableRepository : IPayableRepository
{
    private readonly IDatabaseConnectionProvider _connectionProvider;

    public PayableRepository(IDatabaseConnectionProvider connectionProvider)
    {
        _connectionProvider = connectionProvider;
    }

    public async Task<IReadOnlyList<Payable>> GetByRangeAsync(DateTime start, DateTime end, PayableStatus? status = null, string? search = null)
    {
        var connection = _connectionProvider.GetConnection();
        var query = connection.Table<Payable>().Where(p => p.Vencimento >= start && p.Vencimento <= end);

        if (status.HasValue)
        {
            query = query.Where(p => p.Status == status);
        }

        if (!string.IsNullOrWhiteSpace(search))
        {
            query = query.Where(p => p.Titulo.Contains(search) || p.Nota.Contains(search));
        }

        var items = await query.OrderBy(p => p.Vencimento).ToListAsync();
        return items;
    }

    public async Task<IReadOnlyList<Payable>> GetAllAsync()
    {
        var connection = _connectionProvider.GetConnection();
        return await connection.Table<Payable>().OrderBy(p => p.Vencimento).ToListAsync();
    }

    public async Task<int> InsertAsync(Payable payable)
    {
        var connection = _connectionProvider.GetConnection();
        payable.CreatedAt = DateTime.UtcNow;
        payable.UpdatedAt = DateTime.UtcNow;
        await connection.InsertAsync(payable);
        return payable.Id;
    }

    public async Task UpdateAsync(Payable payable)
    {
        var connection = _connectionProvider.GetConnection();
        payable.UpdatedAt = DateTime.UtcNow;
        await connection.UpdateAsync(payable);
    }

    public async Task DeleteAsync(Payable payable)
    {
        var connection = _connectionProvider.GetConnection();
        await connection.DeleteAsync(payable);
    }

    public async Task<decimal> SumPendingAsync(DateTime start, DateTime end)
    {
        var connection = _connectionProvider.GetConnection();
        var items = await connection.Table<Payable>()
            .Where(p => p.Status == PayableStatus.Pendente && p.Vencimento >= start && p.Vencimento <= end)
            .ToListAsync();

        return items.Sum(p => p.Valor);
    }
}
