using FinaceDavid.Domain.Entities;

namespace FinaceDavid.Data;

public class UserRepository : IUserRepository
{
    private readonly IDatabaseConnectionProvider _connectionProvider;

    public UserRepository(IDatabaseConnectionProvider connectionProvider)
    {
        _connectionProvider = connectionProvider;
    }

    public async Task<User?> GetFirstAsync()
    {
        var connection = _connectionProvider.GetConnection();
        return await connection.Table<User>().FirstOrDefaultAsync();
    }

    public async Task<int> InsertAsync(User user)
    {
        var connection = _connectionProvider.GetConnection();
        await connection.InsertAsync(user);
        return user.Id;
    }

    public async Task UpdateAsync(User user)
    {
        var connection = _connectionProvider.GetConnection();
        await connection.UpdateAsync(user);
    }
}
