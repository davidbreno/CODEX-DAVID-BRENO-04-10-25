import { NavLink } from 'react-router-dom';
import {
  BanknotesIcon,
  ChartPieIcon,
  Cog6ToothIcon,
  HomeModernIcon,
  Squares2X2Icon,
  SwatchIcon,
  TableCellsIcon
} from '@heroicons/react/24/outline';

const navItems = [
  { to: '/dashboard', label: 'Dashboard', icon: HomeModernIcon },
  { to: '/entrada', label: 'Entrada', icon: Squares2X2Icon },
  { to: '/saida', label: 'Saída', icon: BanknotesIcon },
  { to: '/bills', label: 'Contas', icon: TableCellsIcon },
  { to: '/pie', label: 'Pie', icon: ChartPieIcon },
  { to: '/themes', label: 'Temas', icon: SwatchIcon },
  { to: '/settings', label: 'Configurações', icon: Cog6ToothIcon }
];

const Sidebar = () => {
  return (
    <aside className="fixed inset-y-0 left-0 w-60 bg-surface-elevated/90 backdrop-blur border-r border-white/5 flex flex-col p-6 gap-8">
      <div>
        <h1 className="text-2xl font-semibold text-white tracking-tight">Finance</h1>
        <p className="text-sm text-muted mt-1">Controle total das suas finanças</p>
      </div>
      <nav className="flex-1 space-y-1">
        {navItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) =>
              `flex items-center gap-3 px-3 py-2 rounded-xl text-sm font-medium transition border border-transparent ${
                isActive ? 'bg-primary/20 text-white border-primary/50' : 'text-muted hover:text-white hover:bg-white/5'
              }`
            }
          >
            <item.icon className="h-5 w-5" />
            {item.label}
          </NavLink>
        ))}
      </nav>
      <div className="text-xs text-muted">
        <p>Versão 1.0</p>
        <p>© 2025 Finance Intelligence</p>
      </div>
    </aside>
  );
};

export default Sidebar;
