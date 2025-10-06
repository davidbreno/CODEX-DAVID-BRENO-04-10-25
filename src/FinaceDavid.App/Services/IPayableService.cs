using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;

namespace FinaceDavid.Services;

public interface IPayableService
{
    Task<IReadOnlyList<Payable>> GetUpcomingAsync();
    Task<IReadOnlyDictionary<string, IReadOnlyList<Payable>>> GetGroupedAsync();
    Task<int> SaveAsync(Payable payable);
    Task DeleteAsync(int id);
    Task MarkAsPaidAsync(int id, bool createTransaction);
    Task<IReadOnlyList<Payable>> GetByRangeAsync(DateTime start, DateTime end, PayableStatus? status = null);
}
