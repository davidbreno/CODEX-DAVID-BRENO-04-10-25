const Pie = () => {
  return (
    <div className="card">
      <h3 className="text-xl font-semibold text-white mb-4">Distribuição de despesas</h3>
      <p className="text-sm text-muted">
        Explore diferentes cenários de distribuição de gastos, combinando análises do gráfico de pizza com insights de
        categoria.
      </p>
      <ul className="mt-6 space-y-3 text-sm text-muted">
        <li>• Ajuste as categorias no dashboard para ver o impacto na distribuição.</li>
        <li>• Compare despesas fixas versus variáveis ao longo dos meses.</li>
        <li>• Utilize o calendário de contas para planejar períodos de maior gasto.</li>
      </ul>
    </div>
  );
};

export default Pie;
