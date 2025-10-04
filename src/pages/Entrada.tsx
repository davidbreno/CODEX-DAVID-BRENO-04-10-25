import { FormEvent, useMemo, useState } from 'react';
import dayjs from 'dayjs';
import { useFinanceStore } from '../store/financeStore';
import { formatCurrency } from '../utils/format';

const Entrada = () => {
  const addTransaction = useFinanceStore((state) => state.addTransaction);
  const transactions = useFinanceStore((state) => state.transactions);
  const [description, setDescription] = useState('');
  const [amount, setAmount] = useState('');
  const [category, setCategory] = useState('Vendas');

  const incomeTransactions = useMemo(
    () => transactions.filter((transaction) => transaction.type === 'income'),
    [transactions]
  );

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    if (!description || !amount) return;
    await addTransaction({
      type: 'income',
      description,
      amount: parseFloat(amount),
      category,
      date: dayjs().toISOString()
    });
    setDescription('');
    setAmount('');
  };

  return (
    <div className="grid gap-8 lg:grid-cols-3">
      <div className="card lg:col-span-1 space-y-6">
        <header>
          <h3 className="text-xl font-semibold text-white">Nova entrada</h3>
          <p className="text-sm text-muted">Cadastre receitas, vendas e aportes.</p>
        </header>
        <form className="space-y-4" onSubmit={handleSubmit}>
          <div className="space-y-2">
            <label className="text-sm text-muted">Descrição</label>
            <input
              value={description}
              onChange={(event) => setDescription(event.target.value)}
              className="w-full bg-surface border border-white/5 rounded-xl px-4 py-2 focus:outline-none focus:ring-2 focus:ring-primary/40"
            />
          </div>
          <div className="space-y-2">
            <label className="text-sm text-muted">Valor</label>
            <input
              type="number"
              min="0"
              step="0.01"
              value={amount}
              onChange={(event) => setAmount(event.target.value)}
              className="w-full bg-surface border border-white/5 rounded-xl px-4 py-2 focus:outline-none focus:ring-2 focus:ring-primary/40"
            />
          </div>
          <div className="space-y-2">
            <label className="text-sm text-muted">Categoria</label>
            <select
              value={category}
              onChange={(event) => setCategory(event.target.value)}
              className="w-full bg-surface border border-white/5 rounded-xl px-4 py-2 focus:outline-none focus:ring-2 focus:ring-primary/40"
            >
              <option value="Vendas">Vendas</option>
              <option value="Serviços">Serviços</option>
              <option value="Investimentos">Investimentos</option>
            </select>
          </div>
          <button type="submit" className="btn-primary w-full justify-center">
            Registrar entrada
          </button>
        </form>
      </div>
      <div className="lg:col-span-2 card">
        <h3 className="text-xl font-semibold text-white mb-4">Histórico recente</h3>
        <div className="space-y-4">
          {incomeTransactions.length === 0 && <p className="text-sm text-muted">Nenhuma entrada registrada.</p>}
          {incomeTransactions.map((transaction) => (
            <div key={transaction.id} className="flex items-center justify-between bg-white/5 rounded-xl px-4 py-3">
              <div>
                <p className="font-medium text-white">{transaction.description}</p>
                <p className="text-xs text-muted">{dayjs(transaction.date).format('DD/MM/YYYY HH:mm')}</p>
              </div>
              <span className="text-sm font-semibold text-success">{formatCurrency(transaction.amount)}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Entrada;
