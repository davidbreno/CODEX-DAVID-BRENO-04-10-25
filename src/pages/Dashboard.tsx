import { useEffect, useMemo } from 'react';
import dayjs from 'dayjs';
import {
  Area,
  AreaChart,
  Bar,
  BarChart,
  CartesianGrid,
  Cell,
  Pie,
  PieChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis
} from 'recharts';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import ptBrLocale from '@fullcalendar/core/locales/pt-br';
import '@fullcalendar/core/index.css';
import '@fullcalendar/daygrid/index.css';
import { formatCurrency, formatDate, formatMonth } from '../utils/format';
import { useFinanceStore } from '../store/financeStore';

const COLORS = ['#8a2be2', '#00eaff', '#38bdf8', '#22c55e'];

const Dashboard = () => {
  const transactions = useFinanceStore((state) => state.transactions);
  const bills = useFinanceStore((state) => state.bills);
  const loadData = useFinanceStore((state) => state.loadData);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const summary = useMemo(() => {
    const income = transactions
      .filter((transaction) => transaction.type === 'income')
      .reduce((acc, transaction) => acc + transaction.amount, 0);
    const expense = transactions
      .filter((transaction) => transaction.type === 'expense')
      .reduce((acc, transaction) => acc + transaction.amount, 0);
    const balance = income - expense;
    const monthly = transactions.reduce<Record<string, { income: number; expense: number }>>((acc, transaction) => {
      const key = dayjs(transaction.date).format('YYYY-MM');
      if (!acc[key]) acc[key] = { income: 0, expense: 0 };
      acc[key][transaction.type === 'income' ? 'income' : 'expense'] += transaction.amount;
      return acc;
    }, {});
    const monthlyData = Object.entries(monthly)
      .sort(([a], [b]) => (dayjs(a).isAfter(dayjs(b)) ? 1 : -1))
      .map(([month, value]) => ({
        month: formatMonth(`${month}-01`),
        income: value.income,
        expense: value.expense
      }));

    const kpiData = [
      {
        title: 'Receita mensal',
        value: income,
        display: formatCurrency(income),
        change: '+12% vs. mês anterior',
        data: monthlyData.map((item) => ({ name: item.month, value: item.income }))
      },
      {
        title: 'Despesas',
        value: expense,
        display: formatCurrency(expense),
        change: '-4% vs. mês anterior',
        data: monthlyData.map((item) => ({ name: item.month, value: item.expense }))
      },
      {
        title: 'Fluxo de caixa',
        value: balance,
        display: formatCurrency(balance),
        change: balance > 0 ? 'Positivo' : 'Negativo',
        data: monthlyData.map((item) => ({ name: item.month, value: item.income - item.expense }))
      },
      {
        title: 'Contas pendentes',
        value: bills.filter((bill) => bill.status !== 'paid').length,
        display: `${bills.filter((bill) => bill.status !== 'paid').length} contas`,
        change: 'Próximos 7 dias',
        data: bills.map((bill) => ({ name: bill.description, value: dayjs(bill.dueDate).diff(dayjs(), 'day') }))
      }
    ];

    const goalTotal = 15000;
    const progress = Math.min(100, Math.round((income / goalTotal) * 100));

    const progressBars = [
      {
        title: 'Marketing',
        value: 65
      },
      {
        title: 'Operações',
        value: 48
      },
      {
        title: 'Tecnologia',
        value: 78
      }
    ];

    return { income, expense, balance, monthlyData, kpiData, progress, progressBars };
  }, [transactions, bills]);

  const calendarEvents = useMemo(
    () =>
      bills.map((bill) => ({
        id: bill.id,
        title: `${bill.description} • ${formatCurrency(bill.amount)}`,
        date: dayjs(bill.dueDate).format('YYYY-MM-DD'),
        color: bill.status === 'paid' ? '#22c55e' : bill.status === 'overdue' ? '#ef4444' : '#8a2be2'
      })),
    [bills]
  );

  const categoryDistribution = useMemo(() => {
    const entries = transactions.reduce<Record<string, number>>((acc, transaction) => {
      const key = transaction.category || 'Outros';
      acc[key] = (acc[key] ?? 0) + transaction.amount;
      return acc;
    }, {});
    return Object.entries(entries).map(([name, value]) => ({ name, value }));
  }, [transactions]);

  return (
    <div className="space-y-8">
      <section className="grid gap-6 md:grid-cols-2 xl:grid-cols-4">
        {summary.kpiData.map((kpi, index) => (
          <div key={kpi.title} className="card space-y-4">
            <div className="flex items-center justify-between">
              <p className="text-sm text-muted">{kpi.title}</p>
              <span className="text-xs text-accent">{kpi.change}</span>
            </div>
            <h3 className="text-2xl font-semibold text-white">{kpi.display}</h3>
            <div className="h-16">
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={kpi.data}>
                  <defs>
                    <linearGradient id={`color${index}`} x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor={COLORS[index % COLORS.length]} stopOpacity={0.8} />
                      <stop offset="95%" stopColor={COLORS[index % COLORS.length]} stopOpacity={0} />
                    </linearGradient>
                  </defs>
                  <Area
                    type="monotone"
                    dataKey="value"
                    stroke={COLORS[index % COLORS.length]}
                    fill={`url(#color${index})`}
                    strokeWidth={2}
                  />
                </AreaChart>
              </ResponsiveContainer>
            </div>
          </div>
        ))}
      </section>

      <section className="grid gap-6 lg:grid-cols-3">
        <div className="card col-span-1 space-y-6">
          <header>
            <h3 className="text-xl font-semibold text-white">Progresso da meta</h3>
            <p className="text-sm text-muted">Meta anual de receita: {formatCurrency(15000)}</p>
          </header>
          <div className="h-56">
            <ResponsiveContainer>
              <PieChart>
                <Pie data={[{ name: 'Progresso', value: summary.progress }, { name: 'Restante', value: 100 - summary.progress }]} dataKey="value" innerRadius={60} outerRadius={80} paddingAngle={4}>
                  {[summary.progress, 100 - summary.progress].map((value, index) => (
                    <Cell key={`cell-${index}`} fill={index === 0 ? COLORS[0] : '#1e293b'} />
                  ))}
                </Pie>
              </PieChart>
            </ResponsiveContainer>
            <div className="text-center mt-4">
              <span className="text-3xl font-semibold text-white">{summary.progress}%</span>
              <p className="text-sm text-muted">{summary.progress < 100 ? 'Continue avançando' : 'Meta alcançada!'}</p>
            </div>
          </div>
        </div>
        <div className="card col-span-1 space-y-4">
          <h3 className="text-xl font-semibold text-white">Distribuição por categoria</h3>
          <div className="h-56">
            <ResponsiveContainer>
              <PieChart>
                <Pie data={categoryDistribution} dataKey="value" nameKey="name" outerRadius={80} innerRadius={50}>
                  {categoryDistribution.map((entry, index) => (
                    <Cell key={entry.name} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip formatter={(value: number) => formatCurrency(value)} />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>
        <div className="card col-span-1 space-y-6">
          <h3 className="text-xl font-semibold text-white">Acompanhamento</h3>
          <div className="space-y-4">
            {summary.progressBars.map((item) => (
              <div key={item.title}>
                <div className="flex justify-between text-sm text-muted mb-1">
                  <span>{item.title}</span>
                  <span>{item.value}%</span>
                </div>
                <div className="h-2 rounded-full bg-white/10 overflow-hidden">
                  <div className="h-full bg-primary" style={{ width: `${item.value}%` }} />
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="grid gap-6 lg:grid-cols-3">
        <div className="card lg:col-span-2 h-[360px]">
          <h3 className="text-xl font-semibold text-white mb-4">Fluxo mensal</h3>
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={summary.monthlyData}>
              <CartesianGrid strokeDasharray="3 3" opacity={0.2} />
              <XAxis dataKey="month" stroke="#94a3b8" tickLine={false} axisLine={false} />
              <YAxis stroke="#94a3b8" tickLine={false} axisLine={false} tickFormatter={(value) => formatCurrency(value).replace('R$', '')} />
              <Tooltip formatter={(value: number) => formatCurrency(value)} />
              <Bar dataKey="income" fill={COLORS[1]} radius={[8, 8, 0, 0]} />
              <Bar dataKey="expense" fill={COLORS[0]} radius={[8, 8, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
        <div className="card lg:col-span-1">
          <h3 className="text-xl font-semibold text-white mb-4">Contas a pagar</h3>
          <FullCalendar
            plugins={[dayGridPlugin, interactionPlugin]}
            initialView="dayGridMonth"
            height="100%"
            events={calendarEvents}
            headerToolbar={{ left: 'title', right: 'prev,next' }}
            locales={[ptBrLocale]}
            locale="pt-br"
          />
          <div className="mt-4 space-y-2 text-sm text-muted">
            {bills.map((bill) => (
              <div key={bill.id} className="flex justify-between">
                <span>{bill.description}</span>
                <span>{formatDate(bill.dueDate)}</span>
              </div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
};

export default Dashboard;
