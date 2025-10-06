using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;
using FinaceDavid.Services;
using FinaceDavid.Services.Models;
using FinaceDavid.ViewModels;

namespace FinaceDavid.Tests;

public class TransactionsViewModelTests
{
    [Fact]
    public async Task LoadAsync_WithEntradaFilter_ShouldReturnOnlyIncome()
    {
        var transactions = new List<Transaction>
        {
            new() { Id = 1, Type = TransactionType.Entrada, Valor = 100m, Data = DateTime.Today, Categoria = "Salário" },
            new() { Id = 2, Type = TransactionType.Saida, Valor = 50m, Data = DateTime.Today, Categoria = "Mercado" }
        };

        var service = new FakeTransactionService(transactions);
        var filterState = new StubFilterStateService(new DateRange(DateTime.Today.AddDays(-1), DateTime.Today.AddDays(1)));
        var viewModel = new TransactionsViewModel(service, filterState)
        {
            SelectedType = TransactionType.Entrada
        };

        await viewModel.LoadAsync();

        Assert.Single(viewModel.Transactions);
        Assert.Equal(TransactionType.Entrada, viewModel.Transactions.First().Type);
    }

    private sealed class FakeTransactionService : ITransactionService
    {
        private readonly IReadOnlyList<Transaction> _transactions;

        public FakeTransactionService(IReadOnlyList<Transaction> transactions)
        {
            _transactions = transactions;
        }

        public Task DeleteAsync(int id) => Task.CompletedTask;

        public Task<Transaction?> GetByIdAsync(int id) => Task.FromResult(_transactions.FirstOrDefault(t => t.Id == id));

        public Task<decimal> GetBalanceAsync(TransactionFilter filter)
            => Task.FromResult(_transactions.Where(t => t.Data >= filter.Range.Start && t.Data <= filter.Range.End)
                .Sum(t => t.Type == TransactionType.Entrada ? t.Valor : -t.Valor));

        public Task<IReadOnlyDictionary<string, decimal>> GetTotalsByCategoryAsync(TransactionFilter filter)
            => Task.FromResult((IReadOnlyDictionary<string, decimal>)new Dictionary<string, decimal>());

        public Task<decimal> GetTotalAsync(TransactionType type, TransactionFilter filter)
            => Task.FromResult(_transactions.Where(t => t.Type == type && t.Data >= filter.Range.Start && t.Data <= filter.Range.End).Sum(t => t.Valor));

        public Task<IReadOnlyList<Transaction>> GetTransactionsAsync(TransactionFilter filter)
        {
            var items = _transactions.Where(t => t.Data >= filter.Range.Start && t.Data <= filter.Range.End);
            if (filter.Type.HasValue)
            {
                items = items.Where(t => t.Type == filter.Type);
            }

            return Task.FromResult((IReadOnlyList<Transaction>)items.ToList());
        }

        public Task<IReadOnlyList<TrendPoint>> GetTrendAsync(TransactionFilter filter)
            => Task.FromResult((IReadOnlyList<TrendPoint>)Array.Empty<TrendPoint>());

        public Task<int> SaveAsync(Transaction transaction) => Task.FromResult(transaction.Id);
    }

    private sealed class StubFilterStateService : IFilterStateService
    {
        public StubFilterStateService(DateRange range)
        {
            CurrentRange = range;
        }

        public PeriodFilter CurrentPeriod { get; private set; } = PeriodFilter.MesAtual;

        public DateRange CurrentRange { get; private set; }

        public event EventHandler? FilterChanged;

        public void SetPeriod(PeriodFilter period, DateTime? start = null, DateTime? end = null)
        {
            CurrentPeriod = period;
            CurrentRange = DateRange.FromPeriod(period, start, end);
            FilterChanged?.Invoke(this, EventArgs.Empty);
        }
    }
}
