import { MagnifyingGlassIcon } from '@heroicons/react/24/outline';
import ThemeToggle from './ThemeToggle';

interface TopbarProps {
  title: string;
  onSearch?: (value: string) => void;
}

const Topbar = ({ title, onSearch }: TopbarProps) => {
  return (
    <header className="flex items-center justify-between gap-4">
      <div>
        <h2 className="text-3xl font-semibold text-white font-display">{title}</h2>
        <p className="text-sm text-muted">Seu copiloto financeiro inteligente</p>
      </div>
      <div className="flex items-center gap-3">
        <div className="relative">
          <MagnifyingGlassIcon className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-muted" />
          <input
            className="pl-10 pr-4 py-2 rounded-full bg-surface-elevated border border-white/5 text-sm focus:border-primary/60 focus:outline-none focus:ring-2 focus:ring-primary/30"
            placeholder="Buscar"
            onChange={(event) => onSearch?.(event.target.value)}
          />
        </div>
        <ThemeToggle />
      </div>
    </header>
  );
};

export default Topbar;
