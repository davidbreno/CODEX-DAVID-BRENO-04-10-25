namespace FinaceDavid.Data;

public class DatabasePathProvider : IDatabasePathProvider
{
    private const string DatabaseName = "finacedavid.db3";

    public string GetDatabasePath()
    {
        var path = Path.Combine(FileSystem.AppDataDirectory, DatabaseName);
        return path;
    }
}
