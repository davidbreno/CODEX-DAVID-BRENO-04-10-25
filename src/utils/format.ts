import dayjs from 'dayjs';
import 'dayjs/locale/pt-br';

dayjs.locale('pt-br');

const currencyFormatter = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL'
});

export const formatCurrency = (value: number) => currencyFormatter.format(value);
export const formatDate = (isoDate: string) => dayjs(isoDate).format('DD MMM YYYY');
export const formatMonth = (isoDate: string) => dayjs(isoDate).format('MMM YY');
