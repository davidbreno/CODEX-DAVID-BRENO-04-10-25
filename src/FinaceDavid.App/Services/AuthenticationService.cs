using System.Security.Cryptography;
using System.Text;
using FinaceDavid.Data;
using FinaceDavid.Domain.Entities;

namespace FinaceDavid.Services;

public class AuthenticationService : IAuthenticationService
{
    private const string SecureStorageKey = "finacedavid_credential";
    private readonly IUserRepository _userRepository;

    public AuthenticationService(IUserRepository userRepository)
    {
        _userRepository = userRepository;
    }

    public async Task<bool> HasCredentialAsync()
    {
        var stored = await GetStoredHashAsync();
        if (!string.IsNullOrEmpty(stored))
        {
            return true;
        }

        var user = await _userRepository.GetFirstAsync();
        return user is not null;
    }

    public async Task<bool> RegisterAsync(string passwordOrPin)
    {
        if (string.IsNullOrWhiteSpace(passwordOrPin))
        {
            return false;
        }

        if (await HasCredentialAsync())
        {
            return false;
        }

        var hash = ComputeHash(passwordOrPin);
        await SetSecureHashAsync(hash);
        var user = new User { PasswordHash = hash, CreatedAt = DateTime.UtcNow };
        await _userRepository.InsertAsync(user);
        return true;
    }

    public async Task<bool> ValidateAsync(string passwordOrPin)
    {
        var hash = ComputeHash(passwordOrPin);
        var stored = await GetStoredHashAsync();
        if (!string.IsNullOrEmpty(stored))
        {
            return string.Equals(hash, stored, StringComparison.Ordinal);
        }

        var user = await _userRepository.GetFirstAsync();
        return user is not null && string.Equals(hash, user.PasswordHash, StringComparison.Ordinal);
    }

    public async Task<bool> ChangePasswordAsync(string currentPassword, string newPassword)
    {
        if (!await ValidateAsync(currentPassword))
        {
            return false;
        }

        var hash = ComputeHash(newPassword);
        await SetSecureHashAsync(hash);
        var user = await _userRepository.GetFirstAsync();
        if (user is null)
        {
            user = new User { PasswordHash = hash, CreatedAt = DateTime.UtcNow };
            await _userRepository.InsertAsync(user);
        }
        else
        {
            user.PasswordHash = hash;
            await _userRepository.UpdateAsync(user);
        }

        return true;
    }

    private static string ComputeHash(string value)
    {
        using var sha = SHA256.Create();
        var bytes = Encoding.UTF8.GetBytes(value);
        var hash = sha.ComputeHash(bytes);
        return Convert.ToHexString(hash);
    }

    private static async Task<string?> GetStoredHashAsync()
    {
        try
        {
            return await SecureStorage.GetAsync(SecureStorageKey);
        }
        catch
        {
            return null;
        }
    }

    private static async Task SetSecureHashAsync(string hash)
    {
        try
        {
            await SecureStorage.SetAsync(SecureStorageKey, hash);
        }
        catch
        {
            // Ignorado: plataformas sem suporte a SecureStorage.
        }
    }
}
