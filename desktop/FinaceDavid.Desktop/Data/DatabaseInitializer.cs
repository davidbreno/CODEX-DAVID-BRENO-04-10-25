using FinaceDavid.Desktop.Models;
using System;
using System.Linq;

namespace FinaceDavid.Desktop.Data;

public class DatabaseInitializer
{
    private readonly AppDbContext _dbContext;
    private readonly Services.PasswordHasher _passwordHasher;

    public DatabaseInitializer(AppDbContext dbContext, Services.PasswordHasher passwordHasher)
    {
        _dbContext = dbContext;
        _passwordHasher = passwordHasher;
    }

    public void Initialize()
    {
        _dbContext.Database.EnsureCreated();

        if (!_dbContext.Users.Any())
        {
            var user = new User
            {
                Username = "admin",
                PasswordHash = _passwordHasher.Hash("1234"),
                CreatedAt = DateTime.UtcNow
            };

            _dbContext.Users.Add(user);
            _dbContext.SaveChanges();
        }
    }
}
