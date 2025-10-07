using FinaceDavid.Desktop.Data;
using FinaceDavid.Desktop.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FinaceDavid.Desktop.Services;

public class TransactionService
{
    private readonly AppDbContext _dbContext;

    public TransactionService(AppDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public async Task<List<Transaction>> GetTransactionsAsync(DateTime? start, DateTime? end, TransactionType? typeFilter = null, string? search = null)
    {
        var query = _dbContext.Transactions.AsQueryable();

        if (start.HasValue)
        {
            query = query.Where(t => t.Date >= start.Value);
        }

        if (end.HasValue)
        {
            query = query.Where(t => t.Date <= end.Value);
        }

        if (typeFilter.HasValue)
        {
            query = query.Where(t => t.Type == typeFilter.Value);
        }

        if (!string.IsNullOrWhiteSpace(search))
        {
            query = query.Where(t => t.Category.Contains(search) || t.Description.Contains(search));
        }

        return await query.AsNoTracking().ToListAsync();
    }

    public async Task<List<Transaction>> GetTransactionsInRangeAsync(DateTime start, DateTime end)
    {
        return await _dbContext.Transactions.AsNoTracking()
            .Where(t => t.Date >= start && t.Date <= end)
            .ToListAsync();
    }

    public async Task SaveAsync(Transaction transaction)
    {
        if (transaction.Id == 0)
        {
            transaction.CreatedAt = DateTime.UtcNow;
            transaction.UpdatedAt = DateTime.UtcNow;
            _dbContext.Transactions.Add(transaction);
        }
        else
        {
            transaction.UpdatedAt = DateTime.UtcNow;
            _dbContext.Transactions.Update(transaction);
        }

        await _dbContext.SaveChangesAsync();
    }

    public async Task DeleteAsync(Transaction transaction)
    {
        _dbContext.Transactions.Remove(transaction);
        await _dbContext.SaveChangesAsync();
    }
}
