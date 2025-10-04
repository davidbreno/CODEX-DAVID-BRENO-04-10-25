import { Navigate, Route, Routes } from 'react-router-dom';
import MainLayout from './layouts/MainLayout';
import Dashboard from './pages/Dashboard';
import Entrada from './pages/Entrada';
import Saida from './pages/Saida';
import Bills from './pages/Bills';
import Pie from './pages/Pie';
import Login from './pages/Login';
import Themes from './pages/Themes';
import Settings from './pages/Settings';

const App = () => {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route element={<MainLayout />}>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/entrada" element={<Entrada />} />
        <Route path="/saida" element={<Saida />} />
        <Route path="/bills" element={<Bills />} />
        <Route path="/pie" element={<Pie />} />
        <Route path="/themes" element={<Themes />} />
        <Route path="/settings" element={<Settings />} />
      </Route>
    </Routes>
  );
};

export default App;
