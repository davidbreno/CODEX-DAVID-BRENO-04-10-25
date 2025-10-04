const Settings = () => {
  return (
    <div className="card space-y-4">
      <h3 className="text-xl font-semibold text-white">Configurações</h3>
      <p className="text-sm text-muted">
        Personalize integrações, notificações e preferências da sua conta. As opções abaixo são mockadas para demonstração.
      </p>
      <ul className="space-y-3 text-sm text-muted">
        <li>• Integrar com bancos e cartões.</li>
        <li>• Configurar alertas de vencimento por e-mail.</li>
        <li>• Definir limites por categoria de gasto.</li>
      </ul>
    </div>
  );
};

export default Settings;
