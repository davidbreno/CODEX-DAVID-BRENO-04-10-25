using FinaceDavid.Data;
using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;
using FinaceDavid.Services;
using FinaceDavid.Services.Models;

namespace FinaceDavid.Tests;

public class ReportServiceTests
{
    [Fact]
    public async Task BuildDonutAsync_ShouldReturnTotals()
    {
        var repo = new FakeTransactionRepository(new[]
        {
            new Transaction { Id = 1, Type = TransactionType.Entrada, Valor = 100m, Data = DateTime.Today },
            new Transaction { Id = 2, Type = TransactionType.Saida, Valor = 40m, Data = DateTime.Today },
            new Transaction { Id = 3, Type = TransactionType.Entrada, Valor = 60m, Data = DateTime.Today }
        });

        var service = new TransactionService(repo);
        var reportService = new ReportService(service);
        var filter = new TransactionFilter(new DateRange(DateTime.Today.AddDays(-1), DateTime.Today.AddDays(1)));

        var donut = await reportService.BuildDonutAsync(filter);

        Assert.Equal(2, donut.Count);
        Assert.Equal(160m, donut.First(s => s.Label == "Entradas").Value);
        Assert.Equal(40m, donut.First(s => s.Label == "Saídas").Value);
    }

    [Fact]
    public async Task GetTrendAsync_ShouldOrderByDate()
    {
        var repo = new FakeTransactionRepository(new[]
        {
            new Transaction { Id = 1, Type = TransactionType.Saida, Valor = 50m, Data = DateTime.Today.AddDays(-1) },
            new Transaction { Id = 2, Type = TransactionType.Entrada, Valor = 150m, Data = DateTime.Today }
        });

        var service = new TransactionService(repo);
        var filter = new TransactionFilter(new DateRange(DateTime.Today.AddDays(-5), DateTime.Today.AddDays(1)));

        var trend = await service.GetTrendAsync(filter);

        Assert.Equal(2, trend.Count);
        Assert.True(trend[0].Date < trend[1].Date);
        Assert.Equal(-50m, trend[0].Saldo);
        Assert.Equal(150m, trend[1].Entrada);
    }

    private sealed class FakeTransactionRepository : ITransactionRepository
    {
        private readonly List<Transaction> _transactions;

        public FakeTransactionRepository(IEnumerable<Transaction> transactions)
        {
            _transactions = transactions.ToList();
        }

        public Task DeleteAsync(Transaction transaction)
        {
            _transactions.RemoveAll(t => t.Id == transaction.Id);
            return Task.CompletedTask;
        }

        public Task<Transaction?> GetByIdAsync(int id)
            => Task.FromResult(_transactions.FirstOrDefault(t => t.Id == id));

        public Task<IReadOnlyList<Transaction>> GetByFilterAsync(DateTime start, DateTime end, TransactionType? type = null, string? categoria = null, string? search = null, bool includePending = true)
        {
            var query = _transactions.Where(t => t.Data >= start && t.Data <= end);
            if (type.HasValue)
            {
                query = query.Where(t => t.Type == type);
            }

            return Task.FromResult((IReadOnlyList<Transaction>)query.ToList());
        }

        public Task<int> InsertAsync(Transaction transaction)
        {
            transaction.Id = _transactions.Count + 1;
            _transactions.Add(transaction);
            return Task.FromResult(transaction.Id);
        }

        public Task UpdateAsync(Transaction transaction)
        {
            var index = _transactions.FindIndex(t => t.Id == transaction.Id);
            if (index >= 0)
            {
                _transactions[index] = transaction;
            }

            return Task.CompletedTask;
        }

        public Task<decimal> SumByTypeAsync(TransactionType type, DateTime start, DateTime end, bool includePending)
        {
            var sum = _transactions
                .Where(t => t.Type == type && t.Data >= start && t.Data <= end)
                .Sum(t => t.Valor);
            return Task.FromResult(sum);
        }
    }
}
