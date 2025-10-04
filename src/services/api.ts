import dayjs from 'dayjs';
import { Bill, Transaction } from '../store/financeStore';

type Collection = 'transactions' | 'bills';

const STORAGE_KEYS: Record<Collection, string> = {
  transactions: 'finance-transactions',
  bills: 'finance-bills'
};

const inMemoryDb: Record<Collection, unknown[]> = {
  transactions: [],
  bills: []
};

const delay = (ms = 300) => new Promise((resolve) => setTimeout(resolve, ms));

const read = <T>(collection: Collection): T[] => {
  if (typeof window === 'undefined') {
    return (inMemoryDb[collection] as T[]) ?? [];
  }
  const raw = localStorage.getItem(STORAGE_KEYS[collection]);
  if (!raw) return [];
  try {
    return JSON.parse(raw) as T[];
  } catch (error) {
    console.error('Failed to parse storage', error);
    return [];
  }
};

const write = <T>(collection: Collection, data: T[]) => {
  if (typeof window === 'undefined') {
    inMemoryDb[collection] = data;
    return;
  }
  localStorage.setItem(STORAGE_KEYS[collection], JSON.stringify(data));
};

export const api = {
  async getTransactions() {
    await delay();
    const stored = read<Transaction>('transactions');
    if (stored.length > 0) return stored;
    const seed: Transaction[] = [
      {
        id: 't-1',
        type: 'income',
        description: 'Consultoria financeira',
        amount: 5200,
        category: 'Serviços',
        date: dayjs().subtract(12, 'day').toISOString()
      },
      {
        id: 't-2',
        type: 'expense',
        description: 'Assinatura de software',
        amount: 320,
        category: 'Operacional',
        date: dayjs().subtract(9, 'day').toISOString()
      },
      {
        id: 't-3',
        type: 'income',
        description: 'Licenciamento de tecnologia',
        amount: 2800,
        category: 'Vendas',
        date: dayjs().subtract(5, 'day').toISOString()
      },
      {
        id: 't-4',
        type: 'expense',
        description: 'Equipe de marketing',
        amount: 1250,
        category: 'Marketing',
        date: dayjs().subtract(3, 'day').toISOString()
      }
    ];
    write('transactions', seed);
    return seed;
  },
  async createTransaction(transaction: Transaction) {
    await delay();
    const data = read<Transaction>('transactions');
    data.push(transaction);
    write('transactions', data);
    return transaction;
  },
  async updateTransaction(transaction: Transaction) {
    await delay();
    let data = read<Transaction>('transactions');
    data = data.map((item) => (item.id === transaction.id ? transaction : item));
    write('transactions', data);
    return transaction;
  },
  async deleteTransaction(id: string) {
    await delay();
    let data = read<Transaction>('transactions');
    data = data.filter((item) => item.id !== id);
    write('transactions', data);
  },
  async getBills() {
    await delay();
    const existing = read<Bill>('bills');
    if (existing.length > 0) return existing;
    const seed: Bill[] = [
      {
        id: 'bill-1',
        description: 'Internet fibra',
        amount: 180,
        dueDate: dayjs().add(2, 'day').toISOString(),
        status: 'pending'
      },
      {
        id: 'bill-2',
        description: 'Aluguel escritório',
        amount: 3200,
        dueDate: dayjs().add(5, 'day').toISOString(),
        status: 'pending'
      },
      {
        id: 'bill-3',
        description: 'Energia elétrica',
        amount: 640,
        dueDate: dayjs().subtract(1, 'day').toISOString(),
        status: 'overdue'
      }
    ];
    write('bills', seed);
    return seed;
  },
  async saveBills(bills: Bill[]) {
    await delay();
    write('bills', bills);
    return bills;
  }
};
