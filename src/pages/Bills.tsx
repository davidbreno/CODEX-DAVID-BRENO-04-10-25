import { useEffect, useMemo } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import '@fullcalendar/core/index.css';
import '@fullcalendar/daygrid/index.css';
import dayjs from 'dayjs';
import { formatCurrency, formatDate } from '../utils/format';
import { CheckIcon } from '@heroicons/react/24/outline';
import { useFinanceStore } from '../store/financeStore';
import ptBrLocale from '@fullcalendar/core/locales/pt-br';

const Bills = () => {
  const bills = useFinanceStore((state) => state.bills);
  const loadData = useFinanceStore((state) => state.loadData);
  const markBillAsPaid = useFinanceStore((state) => state.markBillAsPaid);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const events = useMemo(
    () =>
      bills.map((bill) => ({
        id: bill.id,
        title: `${bill.description} • ${formatCurrency(bill.amount)}`,
        date: dayjs(bill.dueDate).format('YYYY-MM-DD'),
        color: bill.status === 'paid' ? '#22c55e' : bill.status === 'overdue' ? '#ef4444' : '#00eaff'
      })),
    [bills]
  );

  return (
    <div className="grid gap-8 lg:grid-cols-5">
      <div className="lg:col-span-3 card overflow-hidden">
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-xl font-semibold text-white">Contas a pagar</h3>
          <span className="text-sm text-muted">Total: {formatCurrency(bills.reduce((acc, bill) => acc + bill.amount, 0))}</span>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full text-sm">
            <thead>
              <tr className="text-left text-muted uppercase text-xs">
                <th className="py-2">Descrição</th>
                <th className="py-2">Valor</th>
                <th className="py-2">Vencimento</th>
                <th className="py-2">Status</th>
                <th className="py-2 text-right">Ações</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-white/5">
              {bills.map((bill) => (
                <tr key={bill.id} className="text-white/90">
                  <td className="py-3">
                    <div>
                      <p className="font-medium text-white">{bill.description}</p>
                      {bill.transactionId && <span className="text-[11px] text-muted">Transação: {bill.transactionId}</span>}
                    </div>
                  </td>
                  <td className="py-3">{formatCurrency(bill.amount)}</td>
                  <td className="py-3">{formatDate(bill.dueDate)}</td>
                  <td className="py-3">
                    <span
                      className={`px-3 py-1 rounded-full text-xs font-medium ${
                        bill.status === 'paid'
                          ? 'bg-success/20 text-success'
                          : bill.status === 'overdue'
                          ? 'bg-danger/20 text-danger'
                          : 'bg-primary/20 text-accent'
                      }`}
                    >
                      {bill.status === 'paid' ? 'Pago' : bill.status === 'overdue' ? 'Vencido' : 'Pendente'}
                    </span>
                  </td>
                  <td className="py-3 text-right">
                    {bill.status !== 'paid' && (
                      <button
                        onClick={() => markBillAsPaid(bill.id)}
                        className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary/20 text-accent border border-primary/30 hover:bg-primary/30 transition"
                      >
                        <CheckIcon className="h-4 w-4" />
                        Marcar pago
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
      <div className="lg:col-span-2 card">
        <h3 className="text-xl font-semibold text-white mb-4">Calendário</h3>
        <FullCalendar
          plugins={[dayGridPlugin, interactionPlugin]}
          initialView="dayGridMonth"
          events={events}
          height="100%"
          locales={[ptBrLocale]}
          locale="pt-br"
          headerToolbar={{ left: 'title', right: 'prev,next' }}
        />
      </div>
    </div>
  );
};

export default Bills;
