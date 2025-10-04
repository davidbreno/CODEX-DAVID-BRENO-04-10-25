import { create } from 'zustand';
import { api } from '../services/api';
import dayjs from 'dayjs';

export type TransactionType = 'income' | 'expense';

export interface Transaction {
  id: string;
  type: TransactionType;
  description: string;
  amount: number;
  category: string;
  date: string; // ISO
}

export type BillStatus = 'pending' | 'paid' | 'overdue';

export interface Bill {
  id: string;
  description: string;
  amount: number;
  dueDate: string;
  status: BillStatus;
  transactionId?: string;
}

interface FinanceState {
  transactions: Transaction[];
  bills: Bill[];
  isLoading: boolean;
  loadData: () => Promise<void>;
  addTransaction: (transaction: Omit<Transaction, 'id'>) => Promise<void>;
  updateBill: (bill: Bill) => Promise<void>;
  markBillAsPaid: (billId: string) => Promise<void>;
  deleteTransaction: (id: string) => Promise<void>;
}

const randomId = () => Math.random().toString(36).slice(2, 11);

export const useFinanceStore = create<FinanceState>((set, get) => ({
  transactions: [],
  bills: [],
  isLoading: false,
  loadData: async () => {
    set({ isLoading: true });
    const [transactions, bills] = await Promise.all([api.getTransactions(), api.getBills()]);
    const enhancedBills = bills.map((bill) => ({
      ...bill,
      status: bill.status === 'pending' && dayjs(bill.dueDate).isBefore(dayjs(), 'day') ? 'overdue' : bill.status
    }));
    set({ transactions, bills: enhancedBills, isLoading: false });
  },
  addTransaction: async (transaction) => {
    const newTransaction: Transaction = { id: randomId(), ...transaction };
    set((state) => ({ transactions: [...state.transactions, newTransaction] }));
    await api.createTransaction(newTransaction);
  },
  updateBill: async (bill) => {
    set((state) => ({ bills: state.bills.map((item) => (item.id === bill.id ? bill : item)) }));
    await api.saveBills(get().bills);
  },
  markBillAsPaid: async (billId) => {
    const { bills } = get();
    const bill = bills.find((item) => item.id === billId);
    if (!bill) return;
    const transaction: Transaction = {
      id: randomId(),
      type: 'expense',
      description: `Pagamento: ${bill.description}`,
      amount: bill.amount,
      category: 'Contas',
      date: dayjs().toISOString()
    };
    set((state) => ({
      transactions: [...state.transactions, transaction],
      bills: state.bills.map((item) =>
        item.id === billId ? { ...item, status: 'paid', transactionId: transaction.id } : item
      )
    }));
    await Promise.all([api.createTransaction(transaction), api.saveBills(get().bills)]);
  },
  deleteTransaction: async (id) => {
    set((state) => ({ transactions: state.transactions.filter((item) => item.id !== id) }));
    await api.deleteTransaction(id);
  }
}));
