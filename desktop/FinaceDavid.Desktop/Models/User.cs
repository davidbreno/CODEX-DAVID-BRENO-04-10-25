using System;
using System.ComponentModel.DataAnnotations;

namespace FinaceDavid.Desktop.Models;

public class User
{
    [Key]
    public int Id { get; set; }
    [Required]
    public string Username { get; set; } = string.Empty;
    [Required]
    public string PasswordHash { get; set; } = string.Empty;
    public DateTime CreatedAt { get; set; }
}
