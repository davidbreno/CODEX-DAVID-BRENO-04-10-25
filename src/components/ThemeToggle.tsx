import { MoonIcon, SunIcon } from '@heroicons/react/24/outline';
import { useTheme } from '../utils/ThemeProvider';

const ThemeToggle = () => {
  const { theme, toggleTheme } = useTheme();

  return (
    <button
      onClick={toggleTheme}
      className="inline-flex items-center gap-2 rounded-full px-3 py-2 bg-surface-elevated text-content border border-white/10 hover:border-accent transition"
      aria-label="Alternar tema"
    >
      {theme === 'dark' ? <MoonIcon className="h-4 w-4" /> : <SunIcon className="h-4 w-4" />}
      <span className="text-sm font-medium">{theme === 'dark' ? 'Dark' : 'Light'}</span>
    </button>
  );
};

export default ThemeToggle;
