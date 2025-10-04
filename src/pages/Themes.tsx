import ThemeToggle from '../components/ThemeToggle';

const Themes = () => {
  return (
    <div className="card space-y-6">
      <div>
        <h3 className="text-xl font-semibold text-white">Temas</h3>
        <p className="text-sm text-muted">Gerencie a aparência da aplicação entre os modos claro e escuro.</p>
      </div>
      <ThemeToggle />
      <div className="space-y-4 text-sm text-muted">
        <p>
          O tema escuro é o padrão, pensado para ambientes com pouca luz e para destacar os elementos principais da
          interface.
        </p>
        <p>
          Você pode alternar para o modo claro a qualquer momento. Sua escolha fica salva no navegador e será aplicada
          automaticamente na próxima visita.
        </p>
      </div>
    </div>
  );
};

export default Themes;
