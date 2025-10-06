using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;

namespace FinaceDavid.Data;

public interface IPayableRepository
{
    Task<IReadOnlyList<Payable>> GetByRangeAsync(DateTime start, DateTime end, PayableStatus? status = null, string? search = null);
    Task<IReadOnlyList<Payable>> GetAllAsync();
    Task<int> InsertAsync(Payable payable);
    Task UpdateAsync(Payable payable);
    Task DeleteAsync(Payable payable);
    Task<decimal> SumPendingAsync(DateTime start, DateTime end);
}
