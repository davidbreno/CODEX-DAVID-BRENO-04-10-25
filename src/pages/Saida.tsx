import { FormEvent, useMemo, useState } from 'react';
import dayjs from 'dayjs';
import { useFinanceStore } from '../store/financeStore';
import { formatCurrency } from '../utils/format';
import { TrashIcon } from '@heroicons/react/24/outline';

const Saida = () => {
  const addTransaction = useFinanceStore((state) => state.addTransaction);
  const transactions = useFinanceStore((state) => state.transactions);
  const deleteTransaction = useFinanceStore((state) => state.deleteTransaction);
  const [description, setDescription] = useState('');
  const [amount, setAmount] = useState('');
  const [category, setCategory] = useState('Operacional');

  const expenseTransactions = useMemo(
    () => transactions.filter((transaction) => transaction.type === 'expense'),
    [transactions]
  );

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    if (!description || !amount) return;
    await addTransaction({
      type: 'expense',
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
          <h3 className="text-xl font-semibold text-white">Nova saída</h3>
          <p className="text-sm text-muted">Controle gastos, investimentos e custos fixos.</p>
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
              <option value="Operacional">Operacional</option>
              <option value="Investimentos">Investimentos</option>
              <option value="Pessoal">Pessoal</option>
            </select>
          </div>
          <button type="submit" className="btn-primary w-full justify-center">
            Registrar saída
          </button>
        </form>
      </div>
      <div className="lg:col-span-2 card">
        <h3 className="text-xl font-semibold text-white mb-4">Gastos recentes</h3>
        <div className="space-y-4">
          {expenseTransactions.length === 0 && <p className="text-sm text-muted">Nenhuma saída registrada.</p>}
          {expenseTransactions.map((transaction) => (
            <div key={transaction.id} className="flex items-center justify-between bg-white/5 rounded-xl px-4 py-3">
              <div>
                <p className="font-medium text-white">{transaction.description}</p>
                <p className="text-xs text-muted">{dayjs(transaction.date).format('DD/MM/YYYY HH:mm')} • {transaction.category}</p>
              </div>
              <div className="flex items-center gap-3">
                <span className="text-sm font-semibold text-danger">{formatCurrency(transaction.amount)}</span>
                <button
                  onClick={() => deleteTransaction(transaction.id)}
                  className="p-2 rounded-full bg-surface border border-white/5 hover:border-danger text-muted hover:text-danger transition"
                  aria-label="Remover saída"
                >
                  <TrashIcon className="h-4 w-4" />
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Saida;
