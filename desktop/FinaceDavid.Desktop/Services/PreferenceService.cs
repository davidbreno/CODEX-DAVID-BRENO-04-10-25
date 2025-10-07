using System;
using System.IO;
using System.Text.Json;
using System.Threading.Tasks;

namespace FinaceDavid.Desktop.Services;

public class PreferenceService
{
    private readonly string _filePath;

    public PreferenceService()
    {
        var folder = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), "FinaceDavid");
        Directory.CreateDirectory(folder);
        _filePath = Path.Combine(folder, "preferences.json");
    }

    public async Task<string> GetThemeAsync()
    {
        if (!File.Exists(_filePath))
        {
            return "Dark";
        }

        await using var stream = File.OpenRead(_filePath);
        var data = await JsonSerializer.DeserializeAsync<Preferences>(stream) ?? new Preferences();
        return data.Theme ?? "Dark";
    }

    public async Task SaveThemeAsync(string theme)
    {
        var data = new Preferences { Theme = theme };
        await using var stream = File.Create(_filePath);
        await JsonSerializer.SerializeAsync(stream, data, new JsonSerializerOptions { WriteIndented = true });
    }

    private class Preferences
    {
        public string? Theme { get; set; }
    }
}
