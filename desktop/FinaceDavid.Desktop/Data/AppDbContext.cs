using FinaceDavid.Desktop.Models;
using Microsoft.EntityFrameworkCore;

namespace FinaceDavid.Desktop.Data;

public class AppDbContext : DbContext
{
    private readonly DatabaseOptions _options;

    public AppDbContext(DatabaseOptions options)
    {
        _options = options;
    }

    public DbSet<User> Users => Set<User>();
    public DbSet<Transaction> Transactions => Set<Transaction>();
    public DbSet<Payable> Payables => Set<Payable>();

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        optionsBuilder.UseSqlite($"Data Source={_options.DatabasePath}");
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<User>().HasIndex(u => u.Username).IsUnique();
        modelBuilder.Entity<Transaction>().HasIndex(t => t.Date);
        modelBuilder.Entity<Payable>().HasIndex(p => p.DueDate);
    }
}
