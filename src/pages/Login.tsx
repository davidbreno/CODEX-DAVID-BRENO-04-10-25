import { Link } from 'react-router-dom';

const Login = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-surface">
      <div className="card max-w-md w-full space-y-6">
        <div>
          <h2 className="text-2xl font-semibold text-white">Acessar Finance</h2>
          <p className="text-muted text-sm">Entre para acompanhar suas finanças em tempo real.</p>
        </div>
        <form className="space-y-4">
          <div className="space-y-2">
            <label className="block text-sm font-medium text-muted">E-mail</label>
            <input className="w-full bg-surface border border-white/5 rounded-xl px-4 py-2 focus:outline-none focus:ring-2 focus:ring-primary/40" />
          </div>
          <div className="space-y-2">
            <label className="block text-sm font-medium text-muted">Senha</label>
            <input type="password" className="w-full bg-surface border border-white/5 rounded-xl px-4 py-2 focus:outline-none focus:ring-2 focus:ring-primary/40" />
          </div>
          <Link to="/dashboard" className="btn-primary w-full justify-center">Entrar</Link>
        </form>
      </div>
    </div>
  );
};

export default Login;
