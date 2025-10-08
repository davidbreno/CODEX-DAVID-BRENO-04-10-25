const currencyFormatter = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
  minimumFractionDigits: 2,
});

const percentFormatter = new Intl.NumberFormat('pt-BR', {
  style: 'percent',
  minimumFractionDigits: 1,
  maximumFractionDigits: 1,
});

function formatCurrency(value) {
  return currencyFormatter.format(value);
}

function formatPercent(value) {
  const formatted = percentFormatter.format(Math.abs(value));
  return `${value >= 0 ? '+' : '-'}${formatted}`;
}

function createMonthlyLabels(count) {
  const labels = [];
  const base = new Date();
  for (let i = count - 1; i >= 0; i -= 1) {
    const date = new Date(base.getFullYear(), base.getMonth() - i, 1);
    labels.push(
      new Intl.DateTimeFormat('pt-BR', { month: 'short' })
        .format(date)
        .replace('.', '')
    );
  }
  return labels;
}

const monthlyLabels = createMonthlyLabels(12);

const financeData = {
  resumo: {
    highlightCard: null,
    titles: {
      lineTitle: 'Evolução de entradas e saídas',
      lineSubtitle: 'Últimos 12 meses',
      donutTitle: 'Composição de receitas e despesas',
      donutSubtitle: 'Distribuição consolidada',
    },
    cards: {
      saldoValue: { type: 'currency', value: 185420.32 },
      saldoVariation: { type: 'percent', value: 0.083 },
      entradaValue: { type: 'currency', value: 72900 },
      entradaVariation: { type: 'percent', value: 0.124 },
      entradaAverage: { type: 'currency', value: 3040 },
      saidaValue: { type: 'currency', value: 38450 },
      saidaVariation: { type: 'percent', value: -0.041 },
      saidaTop: { type: 'text', value: 'Marketing, Operações' },
      contasValue: { type: 'currency', value: 24230 },
      contasDue: { type: 'text', value: '3 vencendo' },
      contasNext: { type: 'text', value: 'Folha, Software, Energia' },
    },
    line: {
      labels: monthlyLabels,
      datasets: [
        {
          label: 'Entradas',
          data: [42000, 43200, 44500, 46800, 48120, 50210, 51980, 53870, 55240, 56910, 58900, 61240],
          borderColor: 'rgba(16, 185, 129, 1)',
          backgroundColor: 'rgba(16, 185, 129, 0.2)',
          tension: 0.4,
          fill: true,
        },
        {
          label: 'Saídas',
          data: [27500, 29200, 30550, 32100, 33450, 34900, 35220, 36110, 36840, 37250, 37800, 38450],
          borderColor: 'rgba(248, 113, 113, 1)',
          backgroundColor: 'rgba(248, 113, 113, 0.18)',
          tension: 0.4,
          fill: true,
        },
      ],
    },
    donut: {
      labels: ['Consultorias', 'Investimentos', 'Produtos digitais', 'Serviços recorrentes'],
      data: [32000, 19000, 14500, 7400],
      colors: ['#10b981', '#6366f1', '#f97316', '#ec4899'],
    },
    events: [
      { date: '2025-04-02', type: 'entrada', title: 'Consultoria XPTO', amount: 8200 },
      { date: '2025-04-03', type: 'contas', title: 'Folha de pagamento', amount: 15800 },
      { date: '2025-04-04', type: 'saida', title: 'Campanha de mídia', amount: 6200 },
      { date: '2025-04-08', type: 'entrada', title: 'Licenças anuais', amount: 12400 },
      { date: '2025-04-09', type: 'contas', title: 'Software ERP', amount: 3400 },
      { date: '2025-04-10', type: 'saida', title: 'Infraestrutura cloud', amount: 4100 },
      { date: '2025-04-15', type: 'entrada', title: 'Mentoria executiva', amount: 5300 },
      { date: '2025-04-17', type: 'contas', title: 'Energia elétrica', amount: 1870 },
      { date: '2025-04-18', type: 'saida', title: 'Treinamento time', amount: 2200 },
      { date: '2025-04-22', type: 'entrada', title: 'Royalties plataforma', amount: 9100 },
      { date: '2025-04-24', type: 'saida', title: 'Serviços jurídicos', amount: 1800 },
      { date: '2025-04-26', type: 'contas', title: 'Impostos municipais', amount: 4600 },
    ],
    timeline: [
      { title: 'Entrada confirmada', detail: 'Consultoria XPTO', type: 'entrada', amount: 8200, time: 'há 8 minutos' },
      { title: 'Conta paga', detail: 'Energia elétrica', type: 'contas', amount: -1870, time: 'há 34 minutos' },
      { title: 'Revisão de orçamento', detail: 'Marketing atualizado', type: 'saida', amount: -2100, time: 'há 1 hora' },
      { title: 'Projeção mensal', detail: 'Saldo previsto ajustado', type: 'entrada', amount: 0, time: 'há 2 horas' },
    ],
  },
  entrada: {
    highlightCard: 'entradas',
    titles: {
      lineTitle: 'Entradas confirmadas',
      lineSubtitle: 'Fluxo de recebimentos',
      donutTitle: 'Receitas por origem',
      donutSubtitle: 'Top 4 contratos',
    },
    cards: {
      saldoValue: { type: 'currency', value: 185420.32 },
      saldoVariation: { type: 'percent', value: 0.083 },
      entradaValue: { type: 'currency', value: 61240 },
      entradaVariation: { type: 'percent', value: 0.162 },
      entradaAverage: { type: 'currency', value: 5120 },
      saidaValue: { type: 'currency', value: 38450 },
      saidaVariation: { type: 'percent', value: -0.041 },
      saidaTop: { type: 'text', value: 'Campanhas, Operações' },
      contasValue: { type: 'currency', value: 21200 },
      contasDue: { type: 'text', value: '2 vencendo' },
      contasNext: { type: 'text', value: 'Folha, Fornecedores' },
    },
    line: {
      labels: monthlyLabels,
      datasets: [
        {
          label: 'Entradas',
          data: [32000, 33800, 35100, 36500, 37900, 39400, 41200, 42500, 43800, 45200, 48000, 61240],
          borderColor: 'rgba(16, 185, 129, 1)',
          backgroundColor: 'rgba(16, 185, 129, 0.2)',
          tension: 0.45,
          fill: true,
        },
      ],
    },
    donut: {
      labels: ['Consultorias premium', 'Mentorias', 'Royalties', 'Produtos digitais'],
      data: [22000, 13500, 15800, 9940],
      colors: ['#10b981', '#22d3ee', '#6366f1', '#f472b6'],
    },
    events: [
      { date: '2025-04-02', type: 'entrada', title: 'Consultoria XPTO', amount: 8200 },
      { date: '2025-04-08', type: 'entrada', title: 'Licenças anuais', amount: 12400 },
      { date: '2025-04-15', type: 'entrada', title: 'Mentoria executiva', amount: 5300 },
      { date: '2025-04-22', type: 'entrada', title: 'Royalties plataforma', amount: 9100 },
      { date: '2025-04-27', type: 'entrada', title: 'Investidor estratégico', amount: 13200 },
    ],
    timeline: [
      { title: 'Contrato fechado', detail: 'Mentoria scale-up', type: 'entrada', amount: 6800, time: 'há 12 minutos' },
      { title: 'Recorrência processada', detail: 'Produtos digitais', type: 'entrada', amount: 4200, time: 'há 48 minutos' },
      { title: 'Entrada confirmada', detail: 'Investidor estratégico', type: 'entrada', amount: 13200, time: 'há 2 horas' },
      { title: 'Previsão ajustada', detail: 'Projeção trimestral', type: 'entrada', amount: 0, time: 'há 5 horas' },
    ],
  },
  saida: {
    highlightCard: 'saidas',
    titles: {
      lineTitle: 'Saídas controladas',
      lineSubtitle: 'Compromissos liquidados',
      donutTitle: 'Saídas por centro de custo',
      donutSubtitle: 'Visão do mês corrente',
    },
    cards: {
      saldoValue: { type: 'currency', value: 172980.12 },
      saldoVariation: { type: 'percent', value: 0.052 },
      entradaValue: { type: 'currency', value: 61240 },
      entradaVariation: { type: 'percent', value: 0.098 },
      entradaAverage: { type: 'currency', value: 4580 },
      saidaValue: { type: 'currency', value: 34220 },
      saidaVariation: { type: 'percent', value: -0.068 },
      saidaTop: { type: 'text', value: 'Operações, Marketing, Pessoas' },
      contasValue: { type: 'currency', value: 19890 },
      contasDue: { type: 'text', value: '1 vencendo' },
      contasNext: { type: 'text', value: 'Infra, Jurídico, Tributos' },
    },
    line: {
      labels: monthlyLabels,
      datasets: [
        {
          label: 'Saídas',
          data: [26800, 27900, 28800, 29640, 30500, 31420, 32200, 33100, 33820, 34220, 34980, 35200],
          borderColor: 'rgba(248, 113, 113, 1)',
          backgroundColor: 'rgba(248, 113, 113, 0.18)',
          tension: 0.45,
          fill: true,
        },
      ],
    },
    donut: {
      labels: ['Operações', 'Marketing', 'Pessoas', 'Tecnologia'],
      data: [11800, 8200, 7600, 5620],
      colors: ['#f97316', '#fb7185', '#facc15', '#6366f1'],
    },
    events: [
      { date: '2025-04-04', type: 'saida', title: 'Campanha de mídia', amount: 6200 },
      { date: '2025-04-10', type: 'saida', title: 'Infraestrutura cloud', amount: 4100 },
      { date: '2025-04-12', type: 'saida', title: 'Equipe de produto', amount: 5200 },
      { date: '2025-04-18', type: 'saida', title: 'Treinamento time', amount: 2200 },
      { date: '2025-04-24', type: 'saida', title: 'Serviços jurídicos', amount: 1800 },
    ],
    timeline: [
      { title: 'Pagamento efetuado', detail: 'Infraestrutura cloud', type: 'saida', amount: -4100, time: 'há 18 minutos' },
      { title: 'Equipe bonificada', detail: 'Programa de talentos', type: 'saida', amount: -5200, time: 'há 1 hora' },
      { title: 'Campanha aprovada', detail: 'Marketing performance', type: 'saida', amount: -6200, time: 'há 3 horas' },
      { title: 'Auditoria concluída', detail: 'Compliance mensal', type: 'saida', amount: 0, time: 'há 5 horas' },
    ],
  },
  contas: {
    highlightCard: 'contas',
    titles: {
      lineTitle: 'Contas programadas',
      lineSubtitle: 'Previsão de pagamentos',
      donutTitle: 'Status das contas',
      donutSubtitle: 'Situação desta semana',
    },
    cards: {
      saldoValue: { type: 'currency', value: 162300.55 },
      saldoVariation: { type: 'percent', value: 0.037 },
      entradaValue: { type: 'currency', value: 58900 },
      entradaVariation: { type: 'percent', value: 0.074 },
      entradaAverage: { type: 'currency', value: 4080 },
      saidaValue: { type: 'currency', value: 31200 },
      saidaVariation: { type: 'percent', value: -0.031 },
      saidaTop: { type: 'text', value: 'Infra, Pessoas, Serviços' },
      contasValue: { type: 'currency', value: 24230 },
      contasDue: { type: 'text', value: '3 vencendo' },
      contasNext: { type: 'text', value: 'Folha, ERP, Tributos' },
    },
    line: {
      labels: monthlyLabels,
      datasets: [
        {
          label: 'Contas previstas',
          data: [18200, 19400, 20100, 21900, 22500, 23100, 24000, 24600, 25500, 26200, 23800, 24230],
          borderColor: 'rgba(250, 204, 21, 1)',
          backgroundColor: 'rgba(250, 204, 21, 0.18)',
          tension: 0.4,
          fill: true,
        },
      ],
    },
    donut: {
      labels: ['Vencidas', 'Vencendo', 'Programadas', 'Provisionadas'],
      data: [6400, 8200, 5200, 3430],
      colors: ['#f87171', '#facc15', '#60a5fa', '#a855f7'],
    },
    events: [
      { date: '2025-04-03', type: 'contas', title: 'Folha de pagamento', amount: 15800 },
      { date: '2025-04-09', type: 'contas', title: 'Software ERP', amount: 3400 },
      { date: '2025-04-17', type: 'contas', title: 'Energia elétrica', amount: 1870 },
      { date: '2025-04-21', type: 'contas', title: 'Serviços contábeis', amount: 920 },
      { date: '2025-04-26', type: 'contas', title: 'Impostos municipais', amount: 4600 },
    ],
    timeline: [
      { title: 'Conta programada', detail: 'Serviços contábeis', type: 'contas', amount: -920, time: 'há 20 minutos' },
      { title: 'Pagamento agendado', detail: 'Folha de pagamento', type: 'contas', amount: -15800, time: 'há 2 horas' },
      { title: 'Alerta gerado', detail: 'Impostos municipais', type: 'contas', amount: -4600, time: 'há 4 horas' },
      { title: 'Negociação concluída', detail: 'Software ERP', type: 'contas', amount: -3400, time: 'há 6 horas' },
    ],
  },
};

const quickLinks = document.querySelectorAll('.quick-link');
const cards = document.querySelectorAll('.card');
const chips = document.querySelectorAll('.chip');
const lineChartTitle = document.getElementById('lineChartTitle');
const lineChartSubtitle = document.getElementById('lineChartSubtitle');
const donutChartTitle = document.getElementById('donutChartTitle');
const donutChartSubtitle = document.getElementById('donutChartSubtitle');
const calendarMonth = document.getElementById('calendarMonth');
const calendarGrid = document.querySelector('.calendar-grid');
const calendarDetails = document.getElementById('calendarDetails');
const activityTimeline = document.getElementById('activityTimeline');
const themeToggle = document.getElementById('themeToggle');
const lineCanvas = document.getElementById('lineChart');
const donutCanvas = document.getElementById('donutChart');
const chartTooltip = document.getElementById('chartTooltip');

let currentView = 'resumo';
let currentRange = 12;
let currentDate = new Date(financeData.resumo.events[0].date);

const lineChartState = {
  labels: [],
  datasets: [],
  hitPoints: [],
  activePointKey: null,
  scheduled: false,
};

const donutChartState = {
  labels: [],
  data: [],
  colors: [],
  total: 0,
  segments: [],
  activeIndex: null,
  scheduled: false,
  geometry: null,
};

const donutRotation = -Math.PI / 2;

function prepareCanvas(canvas) {
  const rect = canvas.getBoundingClientRect();
  const width = rect.width || canvas.width;
  const height = rect.height || canvas.height;
  const dpr = window.devicePixelRatio || 1;
  const targetWidth = Math.max(1, Math.round(width * dpr));
  const targetHeight = Math.max(1, Math.round(height * dpr));
  if (canvas.width !== targetWidth || canvas.height !== targetHeight) {
    canvas.width = targetWidth;
    canvas.height = targetHeight;
  }
  const ctx = canvas.getContext('2d');
  ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
  ctx.clearRect(0, 0, width, height);
  return { ctx, width, height };
}

function getThemeColor(variable) {
  return getComputedStyle(document.body).getPropertyValue(variable).trim() || '#94a3b8';
}

function positionTooltip(clientX, clientY) {
  if (!chartTooltip) return;
  const tooltipRect = chartTooltip.getBoundingClientRect();
  const offset = 16;
  let left = clientX + offset;
  let top = clientY + offset;
  if (left + tooltipRect.width > window.innerWidth - 12) {
    left = clientX - tooltipRect.width - offset;
  }
  if (top + tooltipRect.height > window.innerHeight - 12) {
    top = clientY - tooltipRect.height - offset;
  }
  chartTooltip.style.transform = `translate(${Math.max(12, left)}px, ${Math.max(12, top)}px)`;
}

function showTooltip(content, clientX, clientY) {
  if (!chartTooltip) return;
  chartTooltip.innerHTML = content;
  chartTooltip.classList.add('visible');
  chartTooltip.setAttribute('aria-hidden', 'false');
  positionTooltip(clientX, clientY);
}

function hideTooltip() {
  if (!chartTooltip) return;
  chartTooltip.classList.remove('visible');
  chartTooltip.setAttribute('aria-hidden', 'true');
  chartTooltip.style.transform = 'translate(-9999px, -9999px)';
}

function renderLineChart() {
  if (!lineCanvas) return;
  const { ctx, width, height } = prepareCanvas(lineCanvas);
  const padding = { top: 32, right: 28, bottom: 52, left: 68 };
  const chartWidth = Math.max(0, width - padding.left - padding.right);
  const chartHeight = Math.max(0, height - padding.top - padding.bottom);
  const chartLeft = padding.left;
  const chartBottom = height - padding.bottom;
  const chartTop = padding.top;

  const labels = lineChartState.labels;
  const datasets = lineChartState.datasets;
  lineChartState.hitPoints = [];

  if (!labels.length || !datasets.length) {
    ctx.fillStyle = getThemeColor('--text-soft');
    ctx.font = '500 14px "Inter", system-ui';
    ctx.textAlign = 'center';
    ctx.fillText('Sem dados suficientes para exibir o gráfico.', width / 2, height / 2);
    return;
  }

  const allValues = datasets.flatMap((dataset) => dataset.data);
  const maxValue = Math.max(...allValues);
  const minValue = Math.min(...allValues);
  const valueRange = maxValue - minValue || maxValue || 1;
  const upperBound = maxValue + valueRange * 0.1;
  const lowerBound = Math.max(0, minValue - valueRange * 0.1);
  const span = Math.max(upperBound - lowerBound, 1);

  const gridColor = 'rgba(148, 163, 184, 0.16)';
  const axisColor = getThemeColor('--text-soft');

  const steps = 4;
  ctx.strokeStyle = gridColor;
  ctx.lineWidth = 1;
  ctx.setLineDash([4, 6]);
  for (let step = 0; step <= steps; step += 1) {
    const ratio = step / steps;
    const y = chartBottom - ratio * chartHeight;
    ctx.beginPath();
    ctx.moveTo(chartLeft, y);
    ctx.lineTo(chartLeft + chartWidth, y);
    ctx.stroke();
    const value = lowerBound + (upperBound - lowerBound) * ratio;
    ctx.save();
    ctx.setLineDash([]);
    ctx.fillStyle = axisColor;
    ctx.font = '500 12px "Inter", system-ui';
    ctx.textAlign = 'right';
    ctx.fillText(formatCurrency(value), chartLeft - 12, y + 4);
    ctx.restore();
  }
  ctx.setLineDash([]);

  const labelCount = labels.length;
  const stepX = labelCount > 1 ? chartWidth / (labelCount - 1) : 0;
  ctx.fillStyle = axisColor;
  ctx.font = '500 12px "Inter", system-ui';
  ctx.textAlign = 'center';
  labels.forEach((label, index) => {
    const x = chartLeft + (labelCount > 1 ? index * stepX : chartWidth / 2);
    ctx.fillText(label, x, chartBottom + 32);
  });

  const datasetPoints = [];
  datasets.forEach((dataset, datasetIndex) => {
    const points = dataset.data.map((value, index) => {
      const ratio = (value - lowerBound) / span;
      const x = chartLeft + (labelCount > 1 ? index * stepX : chartWidth / 2);
      const y = chartBottom - ratio * chartHeight;
      const point = {
        x,
        y,
        value,
        datasetIndex,
        labelIndex: index,
        label: labels[index],
        datasetLabel: dataset.label,
        color: dataset.borderColor,
        key: `${datasetIndex}-${index}`,
      };
      lineChartState.hitPoints.push(point);
      return point;
    });
    datasetPoints.push({ dataset, points });
  });

  datasetPoints.forEach(({ dataset, points }) => {
    if (dataset.fill && dataset.backgroundColor && points.length) {
      ctx.beginPath();
      ctx.moveTo(points[0].x, chartBottom);
      points.forEach((point) => ctx.lineTo(point.x, point.y));
      ctx.lineTo(points[points.length - 1].x, chartBottom);
      ctx.closePath();
      ctx.fillStyle = dataset.backgroundColor;
      ctx.fill();
    }

    ctx.beginPath();
    points.forEach((point, index) => {
      if (index === 0) {
        ctx.moveTo(point.x, point.y);
      } else {
        ctx.lineTo(point.x, point.y);
      }
    });
    ctx.lineWidth = 2.5;
    ctx.strokeStyle = dataset.borderColor || '#7c3aed';
    ctx.stroke();
  });

  ctx.fillStyle = axisColor;
  ctx.fillRect(chartLeft, chartBottom, chartWidth, 1);

  if (lineChartState.activePointKey) {
    const active = lineChartState.hitPoints.find((point) => point.key === lineChartState.activePointKey);
    if (active) {
      ctx.save();
      ctx.strokeStyle = 'rgba(148, 163, 184, 0.35)';
      ctx.setLineDash([4, 6]);
      ctx.beginPath();
      ctx.moveTo(active.x, chartTop);
      ctx.lineTo(active.x, chartBottom);
      ctx.stroke();
      ctx.restore();

      ctx.save();
      ctx.fillStyle = active.color || '#7c3aed';
      ctx.beginPath();
      ctx.arc(active.x, active.y, 6, 0, Math.PI * 2);
      ctx.fill();
      ctx.lineWidth = 2;
      ctx.strokeStyle = 'rgba(15, 23, 42, 0.85)';
      ctx.stroke();
      ctx.restore();
    }
  }
}

function renderDonutChart() {
  if (!donutCanvas) return;
  const { ctx, width, height } = prepareCanvas(donutCanvas);
  const centerX = width / 2;
  const centerY = height / 2;
  const radius = Math.max(0, Math.min(width, height) / 2 - 16);
  const innerRadius = radius * 0.58;
  donutChartState.geometry = { centerX, centerY, radius, innerRadius };

  const { labels, data, colors, activeIndex, total } = donutChartState;
  donutChartState.segments = [];

  if (!data.length || total === 0) {
    ctx.fillStyle = getThemeColor('--text-soft');
    ctx.font = '500 14px "Inter", system-ui';
    ctx.textAlign = 'center';
    ctx.fillText('Sem dados suficientes para exibir o gráfico.', centerX, centerY);
    return;
  }

  let startAngle = 0;
  data.forEach((value, index) => {
    const portion = value / total;
    const segment = {
      start: startAngle,
      end: startAngle + portion * Math.PI * 2,
      label: labels[index],
      value,
      color: colors[index],
      index,
    };
    donutChartState.segments.push(segment);
    startAngle = segment.end;
  });

  donutChartState.segments.forEach((segment) => {
    const isActive = segment.index === activeIndex;
    const displayRadius = isActive ? radius + 6 : radius;
    const start = donutRotation + segment.start;
    const end = donutRotation + segment.end;

    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.arc(centerX, centerY, displayRadius, start, end);
    ctx.arc(centerX, centerY, innerRadius, end, start, true);
    ctx.closePath();
    ctx.fillStyle = segment.color;
    ctx.globalAlpha = isActive ? 0.95 : 0.82;
    ctx.fill();
    ctx.globalAlpha = 1;
    ctx.lineWidth = 1;
    ctx.strokeStyle = 'rgba(15, 23, 42, 0.35)';
    ctx.stroke();
  });

  ctx.fillStyle = getThemeColor('--text');
  ctx.font = '700 18px "Inter", system-ui';
  ctx.textAlign = 'center';
  ctx.fillText(formatCurrency(total), centerX, centerY - 4);

  ctx.fillStyle = getThemeColor('--text-soft');
  ctx.font = '500 12px "Inter", system-ui';
  ctx.fillText('Total movimentado', centerX, centerY + 18);
}

function scheduleLineChartRender() {
  if (lineChartState.scheduled) return;
  lineChartState.scheduled = true;
  requestAnimationFrame(() => {
    lineChartState.scheduled = false;
    renderLineChart();
  });
}

function scheduleDonutChartRender() {
  if (donutChartState.scheduled) return;
  donutChartState.scheduled = true;
  requestAnimationFrame(() => {
    donutChartState.scheduled = false;
    renderDonutChart();
  });
}

function handleLineHover(event) {
  if (!lineCanvas) return;
  const rect = lineCanvas.getBoundingClientRect();
  const x = event.clientX - rect.left;
  const y = event.clientY - rect.top;
  const tolerance = 18;
  let nearest = null;

  lineChartState.hitPoints.forEach((point) => {
    const distance = Math.hypot(point.x - x, point.y - y);
    if (distance <= tolerance && (!nearest || distance < nearest.distance)) {
      nearest = { ...point, distance };
    }
  });

  if (nearest) {
    if (lineChartState.activePointKey !== nearest.key) {
      lineChartState.activePointKey = nearest.key;
      scheduleLineChartRender();
    }
    showTooltip(
      `<strong>${nearest.datasetLabel}</strong><span>${nearest.label}</span><span>${formatCurrency(nearest.value)}</span>`,
      event.clientX,
      event.clientY
    );
  } else {
    if (lineChartState.activePointKey) {
      lineChartState.activePointKey = null;
      scheduleLineChartRender();
    }
    hideTooltip();
  }
}

function handleLineLeave() {
  if (lineChartState.activePointKey) {
    lineChartState.activePointKey = null;
    scheduleLineChartRender();
  }
  hideTooltip();
}

function handleDonutHover(event) {
  if (!donutCanvas || !donutChartState.geometry) return;
  const rect = donutCanvas.getBoundingClientRect();
  const x = event.clientX - rect.left;
  const y = event.clientY - rect.top;
  const { centerX, centerY, radius, innerRadius } = donutChartState.geometry;
  const dx = x - centerX;
  const dy = y - centerY;
  const distance = Math.sqrt(dx * dx + dy * dy);

  if (distance < innerRadius || distance > radius + 8) {
    if (donutChartState.activeIndex !== null) {
      donutChartState.activeIndex = null;
      scheduleDonutChartRender();
    }
    hideTooltip();
    return;
  }

  let angle = Math.atan2(dy, dx) - donutRotation;
  if (angle < 0) angle += Math.PI * 2;

  const segment = donutChartState.segments.find((item) => angle >= item.start && angle < item.end);
  if (segment) {
    if (donutChartState.activeIndex !== segment.index) {
      donutChartState.activeIndex = segment.index;
      scheduleDonutChartRender();
    }
    const percentage = donutChartState.total ? ((segment.value / donutChartState.total) * 100).toFixed(1) : '0.0';
    showTooltip(
      `<strong>${segment.label}</strong><span>${formatCurrency(segment.value)} · ${percentage}%</span>`,
      event.clientX,
      event.clientY
    );
  } else {
    if (donutChartState.activeIndex !== null) {
      donutChartState.activeIndex = null;
      scheduleDonutChartRender();
    }
    hideTooltip();
  }
}

function handleDonutLeave() {
  if (donutChartState.activeIndex !== null) {
    donutChartState.activeIndex = null;
    scheduleDonutChartRender();
  }
  hideTooltip();
}

function applyRange(data, range) {
  return data.slice(-range);
}

function updateCards(viewKey) {
  const data = financeData[viewKey].cards;
  Object.entries(data).forEach(([key, descriptor]) => {
    const target = document.querySelector(`[data-field="${key}"]`);
    if (!target) return;
    let text = descriptor.value;
    switch (descriptor.type) {
      case 'currency':
        text = formatCurrency(descriptor.value);
        break;
      case 'percent':
        text = formatPercent(descriptor.value);
        break;
      default:
        text = descriptor.value;
    }
    target.textContent = text;
  });

  cards.forEach((card) => {
    const key = financeData[viewKey].highlightCard;
    card.classList.toggle('focus', key && card.dataset.card === key);
  });
}

function updateLineChart(viewKey) {
  const { line } = financeData[viewKey];
  const labels = applyRange(line.labels, currentRange);
  const datasets = line.datasets.map((dataset) => ({
    ...dataset,
    data: applyRange(dataset.data, currentRange),
  }));

  lineChartState.labels = labels;
  lineChartState.datasets = datasets;
  lineChartState.activePointKey = null;
  scheduleLineChartRender();
}

function updateDonutChart(viewKey) {
  const { donut } = financeData[viewKey];
  donutChartState.labels = donut.labels;
  donutChartState.data = donut.data;
  donutChartState.colors = donut.colors;
  donutChartState.total = donut.data.reduce((acc, value) => acc + value, 0);
  donutChartState.activeIndex = null;
  scheduleDonutChartRender();
}

function renderTimeline(viewKey) {
  const items = financeData[viewKey].timeline;
  activityTimeline.innerHTML = '';
  items.forEach((item) => {
    const li = document.createElement('li');
    li.classList.add('timeline-item');
    const amountText =
      item.amount === 0
        ? '—'
        : `${item.amount > 0 ? '+' : '-'}${formatCurrency(Math.abs(item.amount))}`;
    li.innerHTML = `
      <strong>${item.title}</strong>
      <span class="meta">${item.detail} · ${item.time}</span>
      <span class="amount ${item.type}">${amountText}</span>
    `;
    activityTimeline.appendChild(li);
  });
}

function sameDay(a, b) {
  return a.getFullYear() === b.getFullYear() && a.getMonth() === b.getMonth() && a.getDate() === b.getDate();
}

function renderCalendar(viewKey) {
  const events = financeData[viewKey].events;
  const monthFormat = new Intl.DateTimeFormat('pt-BR', { month: 'long', year: 'numeric' });
  calendarMonth.textContent = monthFormat
    .format(currentDate)
    .replace(/^./, (c) => c.toUpperCase());

  const year = currentDate.getFullYear();
  const month = currentDate.getMonth();
  const start = new Date(year, month, 1);
  const startOffset = (start.getDay() + 6) % 7; // Monday-first week
  const totalDays = new Date(year, month + 1, 0).getDate();
  const totalCells = Math.ceil((startOffset + totalDays) / 7) * 7;

  calendarGrid.innerHTML = '';
  let firstActiveCell = null;

  for (let cellIndex = 0; cellIndex < totalCells; cellIndex += 1) {
    const dayNumber = cellIndex - startOffset + 1;
    const cellDate = new Date(year, month, dayNumber);
    const isCurrentMonth = dayNumber > 0 && dayNumber <= totalDays;

    const button = document.createElement('button');
    button.type = 'button';
    button.className = 'calendar-cell';
    button.setAttribute('role', 'gridcell');

    if (!isCurrentMonth) {
      button.classList.add('is-out');
      button.innerHTML = '<span class="date">&nbsp;</span>';
      calendarGrid.appendChild(button);
      continue;
    }

    const eventsForDay = events.filter((event) => {
      const eventDate = new Date(event.date);
      return sameDay(eventDate, cellDate);
    });

    const net = eventsForDay.reduce((acc, event) => {
      const direction = event.type === 'entrada' ? 1 : -1;
      return acc + direction * event.amount;
    }, 0);
    const amountLabel = eventsForDay.length
      ? `${net >= 0 ? '+' : '-'}${formatCurrency(Math.abs(net))}`
      : '—';

    button.innerHTML = `
      <span class="date">${String(dayNumber).padStart(2, '0')}</span>
      <span class="amount">${amountLabel}</span>
    `;

    const badges = document.createElement('div');
    badges.className = 'badges';
    const types = Array.from(new Set(eventsForDay.map((event) => event.type)));
    types.forEach((type) => {
      const dot = document.createElement('span');
      dot.className = `dot ${type === 'contas' ? 'warning' : type === 'entrada' ? 'positive' : 'negative'}`;
      badges.appendChild(dot);
    });
    if (types.length) {
      button.appendChild(badges);
    }

    button.addEventListener('click', () => {
      document.querySelectorAll('.calendar-cell.active').forEach((cell) => cell.classList.remove('active'));
      button.classList.add('active');
      renderCalendarDetails(eventsForDay, cellDate);
    });

    calendarGrid.appendChild(button);

    if (!firstActiveCell && eventsForDay.length) {
      firstActiveCell = { button, events: eventsForDay, date: cellDate };
    }
  }

  if (firstActiveCell) {
    firstActiveCell.button.classList.add('active');
    renderCalendarDetails(firstActiveCell.events, firstActiveCell.date);
  } else {
    renderCalendarDetails([], currentDate);
  }
}

function renderCalendarDetails(events, date) {
  const dayFormat = new Intl.DateTimeFormat('pt-BR', { day: '2-digit', month: 'long' });
  calendarDetails.innerHTML = '';

  if (!events.length) {
    const empty = document.createElement('li');
    empty.textContent = `${dayFormat.format(date)} • Sem movimentações`;
    calendarDetails.appendChild(empty);
    return;
  }

  events
    .sort((a, b) => b.amount - a.amount)
    .forEach((event) => {
      const item = document.createElement('li');
      const badgeClass = event.type === 'contas' ? 'warning' : event.type === 'entrada' ? 'positive' : 'negative';
      const amountText =
        event.type === 'entrada'
          ? `+${formatCurrency(event.amount)}`
          : `-${formatCurrency(event.amount)}`;
      const formattedDate = dayFormat
        .format(new Date(event.date))
        .replace(/^./, (c) => c.toUpperCase());
      item.innerHTML = `
        <span>${formattedDate} · ${event.title}</span>
        <span class="amount ${badgeClass}">${amountText}</span>
      `;
      calendarDetails.appendChild(item);
    });
}

function updateTitles(viewKey) {
  const { titles } = financeData[viewKey];
  lineChartTitle.textContent = titles.lineTitle;
  lineChartSubtitle.textContent = titles.lineSubtitle;
  donutChartTitle.textContent = titles.donutTitle;
  donutChartSubtitle.textContent = titles.donutSubtitle;
}

function updateView(viewKey) {
  currentView = viewKey;
  hideTooltip();
  updateTitles(viewKey);
  updateCards(viewKey);
  updateLineChart(viewKey);
  updateDonutChart(viewKey);
  renderTimeline(viewKey);
  renderCalendar(viewKey);
}

quickLinks.forEach((link) => {
  link.addEventListener('click', () => {
    const view = link.dataset.view;
    if (view === currentView) return;

    quickLinks.forEach((btn) => btn.classList.remove('active'));
    link.classList.add('active');
    updateView(view);
  });
});

chips.forEach((chip) => {
  chip.addEventListener('click', () => {
    const range = Number(chip.dataset.range);
    if (range === currentRange) return;

    chips.forEach((item) => item.classList.remove('active'));
    chip.classList.add('active');
    currentRange = range;
    updateLineChart(currentView);
  });
});

if (lineCanvas) {
  lineCanvas.addEventListener('mousemove', handleLineHover);
  lineCanvas.addEventListener('mouseleave', handleLineLeave);
  lineCanvas.addEventListener('touchstart', (event) => {
    if (!event.touches.length) return;
    handleLineHover(event.touches[0]);
    event.preventDefault();
  }, { passive: false });
  lineCanvas.addEventListener('touchmove', (event) => {
    if (!event.touches.length) return;
    handleLineHover(event.touches[0]);
    event.preventDefault();
  }, { passive: false });
  lineCanvas.addEventListener('touchend', handleLineLeave);
  lineCanvas.addEventListener('touchcancel', handleLineLeave);
}

if (donutCanvas) {
  donutCanvas.addEventListener('mousemove', handleDonutHover);
  donutCanvas.addEventListener('mouseleave', handleDonutLeave);
  donutCanvas.addEventListener('touchstart', (event) => {
    if (!event.touches.length) return;
    handleDonutHover(event.touches[0]);
    event.preventDefault();
  }, { passive: false });
  donutCanvas.addEventListener('touchmove', (event) => {
    if (!event.touches.length) return;
    handleDonutHover(event.touches[0]);
    event.preventDefault();
  }, { passive: false });
  donutCanvas.addEventListener('touchend', handleDonutLeave);
  donutCanvas.addEventListener('touchcancel', handleDonutLeave);
}

if (window.ResizeObserver) {
  const lineObserver = new ResizeObserver(() => scheduleLineChartRender());
  const donutObserver = new ResizeObserver(() => scheduleDonutChartRender());
  if (lineCanvas?.parentElement) lineObserver.observe(lineCanvas.parentElement);
  if (donutCanvas?.parentElement) donutObserver.observe(donutCanvas.parentElement);
}

window.addEventListener('resize', () => {
  scheduleLineChartRender();
  scheduleDonutChartRender();
});

function shiftMonth(delta) {
  currentDate = new Date(currentDate.getFullYear(), currentDate.getMonth() + delta, 1);
  renderCalendar(currentView);
}

document.getElementById('prevMonth').addEventListener('click', () => shiftMonth(-1));
document.getElementById('nextMonth').addEventListener('click', () => shiftMonth(1));

themeToggle.addEventListener('click', () => {
  document.body.classList.toggle('light');
  themeToggle.querySelector('.icon').textContent = document.body.classList.contains('light') ? '🌞' : '🌙';
  scheduleLineChartRender();
  scheduleDonutChartRender();
  hideTooltip();
});

updateView('resumo');
