using FinaceDavid.Domain.Entities;

namespace FinaceDavid.Data;

public interface IUserRepository
{
    Task<User?> GetFirstAsync();
    Task<int> InsertAsync(User user);
    Task UpdateAsync(User user);
}
