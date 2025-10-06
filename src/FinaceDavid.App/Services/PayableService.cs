using System.Linq;
using FinaceDavid.Data;
using FinaceDavid.Domain.Entities;
using FinaceDavid.Domain.Enums;

namespace FinaceDavid.Services;

public class PayableService : IPayableService
{
    private readonly IPayableRepository _payableRepository;
    private readonly ITransactionService _transactionService;

    public PayableService(IPayableRepository payableRepository, ITransactionService transactionService)
    {
        _payableRepository = payableRepository;
        _transactionService = transactionService;
    }

    public async Task<IReadOnlyList<Payable>> GetUpcomingAsync()
    {
        var today = DateTime.Today;
        return await _payableRepository.GetByRangeAsync(today.AddDays(-7), today.AddDays(30));
    }

    public async Task<IReadOnlyDictionary<string, IReadOnlyList<Payable>>> GetGroupedAsync()
    {
        var items = await _payableRepository.GetAllAsync();
        var today = DateTime.Today;
        var grouped = new Dictionary<string, IReadOnlyList<Payable>>
        {
            ["Vencidas"] = items.Where(p => p.Vencimento.Date < today && p.Status == PayableStatus.Pendente).ToList(),
            ["Hoje"] = items.Where(p => p.Vencimento.Date == today).ToList(),
            ["Próximas"] = items.Where(p => p.Vencimento.Date > today).ToList()
        };
        return grouped;
    }

    public async Task<int> SaveAsync(Payable payable)
    {
        if (payable.Id == 0)
        {
            return await _payableRepository.InsertAsync(payable);
        }

        await _payableRepository.UpdateAsync(payable);
        return payable.Id;
    }

    public async Task DeleteAsync(int id)
    {
        var existing = (await _payableRepository.GetAllAsync()).FirstOrDefault(p => p.Id == id);
        if (existing is not null)
        {
            await _payableRepository.DeleteAsync(existing);
        }
    }

    public async Task MarkAsPaidAsync(int id, bool createTransaction)
    {
        var existing = (await _payableRepository.GetAllAsync()).FirstOrDefault(p => p.Id == id);
        if (existing is null)
        {
            return;
        }

        existing.Status = PayableStatus.Pago;
        await _payableRepository.UpdateAsync(existing);

        if (createTransaction)
        {
            var transaction = new Transaction
            {
                Type = TransactionType.Saida,
                Valor = existing.Valor,
                Data = existing.Vencimento,
                Categoria = "Contas a pagar",
                Descricao = existing.Titulo,
                Status = TransactionStatus.Pago
            };

            await _transactionService.SaveAsync(transaction);
        }
    }

    public Task<IReadOnlyList<Payable>> GetByRangeAsync(DateTime start, DateTime end, PayableStatus? status = null)
        => _payableRepository.GetByRangeAsync(start, end, status);
}
