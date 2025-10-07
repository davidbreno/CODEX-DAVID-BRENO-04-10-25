namespace FinaceDavid.Services;

public interface IAuthenticationService
{
    Task<bool> HasCredentialAsync();
    Task<bool> RegisterAsync(string passwordOrPin);
    Task<bool> ValidateAsync(string passwordOrPin);
    Task<bool> ChangePasswordAsync(string currentPassword, string newPassword);
}
