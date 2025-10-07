using SQLite;

namespace FinaceDavid.Data;

public interface IDatabaseConnectionProvider
{
    SQLiteAsyncConnection GetConnection();
}
