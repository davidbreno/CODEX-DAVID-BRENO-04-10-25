using SQLite;

namespace FinaceDavid.Domain.Entities;

[Table("Users")]
public class User
{
    [PrimaryKey, AutoIncrement]
    public int Id { get; set; }

    [Column("PasswordHash"), NotNull]
    public string PasswordHash { get; set; } = string.Empty;

    [Column("CreatedAt"), NotNull]
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
}
