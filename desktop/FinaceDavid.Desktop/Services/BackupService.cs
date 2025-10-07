using FinaceDavid.Desktop.Data;
using FinaceDavid.Desktop.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text.Json;
using System.Threading.Tasks;

namespace FinaceDavid.Desktop.Services;

public class BackupService
{
    private readonly AppDbContext _dbContext;

    public BackupService(AppDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public async Task<string> ExportAsync()
    {
        var export = new BackupModel
        {
            Users = await _dbContext.Users.AsNoTracking().ToListAsync(),
            Transactions = await _dbContext.Transactions.AsNoTracking().ToListAsync(),
            Payables = await _dbContext.Payables.AsNoTracking().ToListAsync()
        };

        var folder = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments), "FinaceDavid");
        Directory.CreateDirectory(folder);
        var path = Path.Combine(folder, $"backup_{DateTime.Now:yyyyMMddHHmmss}.json");
        await File.WriteAllTextAsync(path, JsonSerializer.Serialize(export, new JsonSerializerOptions { WriteIndented = true }));
        return path;
    }

    public async Task ImportAsync(string path)
    {
        if (!File.Exists(path))
        {
            return;
        }

        var json = await File.ReadAllTextAsync(path);
        var model = JsonSerializer.Deserialize<BackupModel>(json);
        if (model is null)
        {
            return;
        }

        await using var transaction = await _dbContext.Database.BeginTransactionAsync();
        _dbContext.Users.RemoveRange(_dbContext.Users);
        _dbContext.Transactions.RemoveRange(_dbContext.Transactions);
        _dbContext.Payables.RemoveRange(_dbContext.Payables);
        await _dbContext.SaveChangesAsync();

        await _dbContext.Users.AddRangeAsync(model.Users);
        await _dbContext.Transactions.AddRangeAsync(model.Transactions);
        await _dbContext.Payables.AddRangeAsync(model.Payables);
        await _dbContext.SaveChangesAsync();
        await transaction.CommitAsync();
    }

    private class BackupModel
    {
        public List<User> Users { get; set; } = new();
        public List<Transaction> Transactions { get; set; } = new();
        public List<Payable> Payables { get; set; } = new();
    }
}
