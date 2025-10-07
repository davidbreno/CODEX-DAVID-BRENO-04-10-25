using FinaceDavid.Domain.Entities;
using SQLite;

namespace FinaceDavid.Data;

public class DatabaseInitializer : IDatabaseInitializer, IDatabaseConnectionProvider
{
    private readonly IDatabasePathProvider _pathProvider;
    private SQLiteAsyncConnection? _connection;
    private readonly SemaphoreSlim _semaphore = new(1, 1);

    public DatabaseInitializer(IDatabasePathProvider pathProvider)
    {
        _pathProvider = pathProvider;
    }

    public async Task InitializeAsync()
    {
        await _semaphore.WaitAsync();
        try
        {
            if (_connection is not null)
            {
                return;
            }

            var path = _pathProvider.GetDatabasePath();
            _connection = new SQLiteAsyncConnection(path, SQLiteOpenFlags.Create | SQLiteOpenFlags.ReadWrite | SQLiteOpenFlags.SharedCache);
            await _connection.CreateTableAsync<User>();
            await _connection.CreateTableAsync<Transaction>();
            await _connection.CreateTableAsync<Payable>();

            if (await _connection.Table<Transaction>().CountAsync() == 0)
            {
                await SeedAsync(_connection);
            }
        }
        finally
        {
            _semaphore.Release();
        }
    }

    public SQLiteAsyncConnection GetConnection()
    {
        if (_connection is null)
        {
            var path = _pathProvider.GetDatabasePath();
            _connection = new SQLiteAsyncConnection(path, SQLiteOpenFlags.Create | SQLiteOpenFlags.ReadWrite | SQLiteOpenFlags.SharedCache);
        }

        return _connection;
    }

    private static async Task SeedAsync(SQLiteAsyncConnection connection)
    {
        var now = DateTime.Now;
        var sampleTransactions = new List<Transaction>
        {
            new()
            {
                Type = Domain.Enums.TransactionType.Entrada,
                Valor = 3500.75m,
                Data = now.AddDays(-5),
                Categoria = "Salário",
                Descricao = "Pagamento mensal",
                Status = Domain.Enums.TransactionStatus.Pago
            },
            new()
            {
                Type = Domain.Enums.TransactionType.Saida,
                Valor = 450.30m,
                Data = now.AddDays(-2),
                Categoria = "Aluguel",
                Descricao = "Apartamento",
                Status = Domain.Enums.TransactionStatus.Pago
            },
            new()
            {
                Type = Domain.Enums.TransactionType.Saida,
                Valor = 120.00m,
                Data = now.AddDays(-1),
                Categoria = "Mercado",
                Descricao = "Compras semanais",
                Status = Domain.Enums.TransactionStatus.Pendente
            }
        };

        await connection.InsertAllAsync(sampleTransactions);

        var payables = new List<Payable>
        {
            new()
            {
                Titulo = "Internet",
                Valor = 99.90m,
                Vencimento = now.AddDays(3).Date,
                Status = Domain.Enums.PayableStatus.Pendente,
                Nota = "Plano 500Mbps"
            },
            new()
            {
                Titulo = "Energia",
                Valor = 230.45m,
                Vencimento = now.AddDays(-1).Date,
                Status = Domain.Enums.PayableStatus.Pendente,
                Nota = "Conta atrasada"
            }
        };

        await connection.InsertAllAsync(payables);
    }
}
