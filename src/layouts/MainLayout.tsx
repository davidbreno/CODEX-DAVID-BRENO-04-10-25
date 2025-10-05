import { Outlet, useLocation } from 'react-router-dom';
import Sidebar from '../components/Sidebar';
import Topbar from '../components/Topbar';
import { useMemo } from 'react';

const titles: Record<string, string> = {
  '/dashboard': 'Visão geral',
  '/entrada': 'Entrada de recursos',
  '/saida': 'Saída de recursos',
  '/bills': 'Contas a pagar',
  '/pie': 'Distribuição',
  '/themes': 'Temas',
  '/settings': 'Configurações'
};

const MainLayout = () => {
  const location = useLocation();
  const title = useMemo(() => titles[location.pathname] ?? 'Finance', [location.pathname]);

  return (
    <div className="min-h-screen bg-surface text-content">
      <Sidebar />
      <main className="ml-60 p-10 space-y-8">
        <Topbar title={title} />
        <Outlet />
      </main>
    </div>
  );
};

export default MainLayout;
