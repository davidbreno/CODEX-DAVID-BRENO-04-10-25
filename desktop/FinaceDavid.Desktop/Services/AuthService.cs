using FinaceDavid.Desktop.Data;
using FinaceDavid.Desktop.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Threading.Tasks;

namespace FinaceDavid.Desktop.Services;

public class AuthService
{
    private readonly AppDbContext _dbContext;
    private readonly PasswordHasher _passwordHasher;

    public AuthService(AppDbContext dbContext, PasswordHasher passwordHasher)
    {
        _dbContext = dbContext;
        _passwordHasher = passwordHasher;
    }

    public async Task<bool> RegisterAsync(string username, string password)
    {
        if (string.IsNullOrWhiteSpace(username) || string.IsNullOrWhiteSpace(password))
        {
            return false;
        }

        if (await _dbContext.Users.AnyAsync(u => u.Username == username))
        {
            return false;
        }

        var user = new User
        {
            Username = username,
            PasswordHash = _passwordHasher.Hash(password),
            CreatedAt = DateTime.UtcNow
        };

        _dbContext.Users.Add(user);
        await _dbContext.SaveChangesAsync();
        return true;
    }

    public async Task<bool> LoginAsync(string username, string password)
    {
        var user = await _dbContext.Users.FirstOrDefaultAsync(u => u.Username == username);
        if (user is null)
        {
            return false;
        }

        return _passwordHasher.Verify(password, user.PasswordHash);
    }
}
