using FinaceDavid.Desktop.Data;
using FinaceDavid.Desktop.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace FinaceDavid.Desktop.Services;

public class PayableService
{
    private readonly AppDbContext _dbContext;
    private readonly TransactionService _transactionService;

    public PayableService(AppDbContext dbContext, TransactionService transactionService)
    {
        _dbContext = dbContext;
        _transactionService = transactionService;
    }

    public async Task<List<Payable>> GetAllAsync()
    {
        return await _dbContext.Payables.AsNoTracking().ToListAsync();
    }

    public async Task SaveAsync(Payable payable)
    {
        if (payable.Id == 0)
        {
            payable.CreatedAt = DateTime.UtcNow;
            payable.UpdatedAt = DateTime.UtcNow;
            _dbContext.Payables.Add(payable);
        }
        else
        {
            payable.UpdatedAt = DateTime.UtcNow;
            _dbContext.Payables.Update(payable);
        }

        await _dbContext.SaveChangesAsync();
    }

    public async Task MarkAsPaidAsync(Payable payable)
    {
        payable.Status = PayableStatus.Pago;
        payable.UpdatedAt = DateTime.UtcNow;
        _dbContext.Payables.Update(payable);
        await _dbContext.SaveChangesAsync();

        var transaction = new Transaction
        {
            Type = TransactionType.Saida,
            Amount = payable.Amount,
            Category = "Contas a Pagar",
            Date = DateTime.Now,
            Description = payable.Title,
            Status = TransactionStatus.Pago,
            CreatedAt = DateTime.UtcNow,
            UpdatedAt = DateTime.UtcNow
        };

        await _transactionService.SaveAsync(transaction);
    }
}
