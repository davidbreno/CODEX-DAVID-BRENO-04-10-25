"""Janela principal do aplicativo Minhas Finanças."""

from __future__ import annotations

from datetime import date, datetime, timedelta
from typing import Dict, List, Optional

from PyQt6.QtChart import (
    QBarCategoryAxis,
    QBarSeries,
    QBarSet,
    QChart,
    QChartView,
    QLineSeries,
    QPieSeries,
    QValueAxis,
)
from PyQt6.QtCore import QEasingCurve, QPointF, Qt
from PyQt6.QtGui import QColor, QFont, QTextCharFormat
from PyQt6.QtWidgets import (
    QCalendarWidget,
    QComboBox,
    QDialog,
    QDialogButtonBox,
    QFormLayout,
    QFrame,
    QGridLayout,
    QHBoxLayout,
    QLabel,
    QLineEdit,
    QMainWindow,
    QMenu,
    QMessageBox,
    QPushButton,
    QSizePolicy,
    QSpinBox,
    QTableWidget,
    QTableWidgetItem,
    QTextEdit,
    QVBoxLayout,
    QWidget,
)

from .database import AccountPayable, DatabaseManager, Transaction
from .theme_utils import ThemeLoader


class DashboardWindow(QMainWindow):
    """Janela com o painel principal do sistema."""

    def __init__(
        self,
        database: DatabaseManager,
        user_id: int,
        theme_loader: Optional[ThemeLoader] = None,
        parent=None,
    ) -> None:
        super().__init__(parent)
        self.database = database
        self.user_id = user_id
        self.theme_loader = theme_loader or ThemeLoader()
        self.current_theme_key = "dark_neon"
        self.current_month = date.today().replace(day=1)
        self.setWindowTitle("Minhas Finanças - Dashboard")
        self.setMinimumSize(1280, 720)
        self._build_ui()
        self.apply_theme(self.current_theme_key)
        self.refresh_data()

    # ------------------------------------------------------------------
    # Interface
    # ------------------------------------------------------------------
    def _build_ui(self) -> None:
        central = QWidget()
        self.setCentralWidget(central)
        layout = QVBoxLayout(central)
        layout.setContentsMargins(24, 24, 24, 24)
        layout.setSpacing(24)

        # Topo
        top_bar = QHBoxLayout()
        self.title_label = QLabel("Minhas Finanças")
        self.title_label.setFont(QFont("Segoe UI", 24, QFont.Weight.Bold))
        top_bar.addWidget(self.title_label)

        top_bar.addStretch()

        self.month_label = QLabel(self.current_month.strftime("%B").title())
        self.month_label.setFont(QFont("Segoe UI", 18, QFont.Weight.Medium))
        top_bar.addWidget(self.month_label)

        self.add_transaction_button = QPushButton("+")
        self.add_transaction_button.setFixedSize(44, 44)
        self.add_transaction_button.clicked.connect(self._open_transaction_dialog)  # type: ignore[arg-type]
        top_bar.addWidget(self.add_transaction_button)

        self.theme_button = QPushButton("Tema")
        self.theme_button.clicked.connect(self._show_theme_menu)  # type: ignore[arg-type]
        top_bar.addWidget(self.theme_button)

        self.profile_button = QPushButton("Perfil")
        self.profile_button.clicked.connect(self._show_profile_info)  # type: ignore[arg-type]
        top_bar.addWidget(self.profile_button)

        layout.addLayout(top_bar)

        # Cartões resumo
        cards_layout = QHBoxLayout()
        cards_layout.setSpacing(16)
        self.cards: Dict[str, QFrame] = {}
        for key, label in (
            ("saldo_inicial", "Saldo inicial"),
            ("saldo_atual", "Saldo atual"),
            ("previsao", "Previsão"),
            ("entradas", "Receitas"),
            ("saidas", "Despesas"),
        ):
            card = self._create_summary_card(label)
            self.cards[key] = card
            cards_layout.addWidget(card)
        layout.addLayout(cards_layout)

        # Seção central com gráficos e tabelas
        center_layout = QGridLayout()
        center_layout.setSpacing(24)

        self.line_chart_view = QChartView()
        self.line_chart_view.setRenderHint(self.line_chart_view.renderHints())
        center_layout.addWidget(self.line_chart_view, 0, 0, 1, 2)

        self.pie_chart_view = QChartView()
        center_layout.addWidget(self.pie_chart_view, 0, 2, 2, 1)

        self.bar_chart_view = QChartView()
        center_layout.addWidget(self.bar_chart_view, 1, 0, 1, 2)

        layout.addLayout(center_layout)

        # Seções detalhadas
        detail_layout = QHBoxLayout()
        detail_layout.setSpacing(24)

        self.income_table = self._create_table(["Data", "Categoria", "Descrição", "Valor"])
        self.income_table.setObjectName("IncomeTable")
        self.expense_table = self._create_table(["Data", "Categoria", "Descrição", "Valor"])
        self.expense_table.setObjectName("ExpenseTable")
        self.accounts_table = self._create_table(["Conta", "Vencimento", "Valor", "Status"])
        self.accounts_table.setObjectName("AccountsTable")

        detail_layout.addWidget(self._wrap_with_label("Entradas", self.income_table))
        detail_layout.addWidget(self._wrap_with_label("Saídas", self.expense_table))
        detail_layout.addWidget(self._wrap_with_label("Contas a pagar", self.accounts_table))

        layout.addLayout(detail_layout)

        # Calendário
        self.calendar = FinanceCalendar()
        layout.addWidget(self._wrap_with_label("Calendário de contas", self.calendar))

    def _create_summary_card(self, title: str) -> QFrame:
        card = QFrame()
        card.setObjectName("SummaryCard")
        card_layout = QVBoxLayout(card)
        card_layout.setContentsMargins(18, 18, 18, 18)
        card_layout.setSpacing(6)

        label = QLabel(title)
        label.setFont(QFont("Segoe UI", 12, QFont.Weight.Medium))
        value = QLabel("R$ 0,00")
        value.setFont(QFont("Segoe UI", 20, QFont.Weight.Bold))
        value.setObjectName("CardValue")

        card_layout.addWidget(label)
        card_layout.addWidget(value)
        card_layout.addStretch()

        return card

    def _create_table(self, headers: List[str]) -> QTableWidget:
        table = QTableWidget()
        table.setColumnCount(len(headers))
        table.setHorizontalHeaderLabels(headers)
        table.horizontalHeader().setStretchLastSection(True)
        table.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding)
        table.verticalHeader().setVisible(False)
        table.setEditTriggers(QTableWidget.EditTrigger.NoEditTriggers)
        table.setSelectionBehavior(QTableWidget.SelectionBehavior.SelectRows)
        table.setAlternatingRowColors(True)
        return table

    def _wrap_with_label(self, title: str, widget: QWidget) -> QWidget:
        wrapper = QFrame()
        wrapper.setObjectName("SectionCard")
        layout = QVBoxLayout(wrapper)
        layout.setContentsMargins(16, 16, 16, 16)
        layout.setSpacing(12)
        label = QLabel(title)
        label.setFont(QFont("Segoe UI", 14, QFont.Weight.Medium))
        layout.addWidget(label)
        layout.addWidget(widget)
        return wrapper

    # ------------------------------------------------------------------
    # Ações
    # ------------------------------------------------------------------
    def _show_theme_menu(self) -> None:
        menu = QMenu(self)
        for key, name in self.theme_loader.list_themes().items():
            action = menu.addAction(name)
            action.triggered.connect(lambda checked=False, k=key: self.apply_theme(k))  # type: ignore[arg-type]
        menu.exec(self.theme_button.mapToGlobal(self.theme_button.rect().bottomLeft()))

    def _show_profile_info(self) -> None:
        user = self.database.get_user(self.user_id)
        if not user:
            return
        created_at = datetime.fromisoformat(user["created_at"]).strftime("%d/%m/%Y %H:%M")
        QMessageBox.information(
            self,
            "Perfil",
            f"Usuário: {user['name']}\nE-mail: {user['email']}\nConta criada em: {created_at}",
        )

    def _open_transaction_dialog(self) -> None:
        dialog = TransactionDialog(self.database, self.user_id, parent=self)
        if dialog.exec():
            self.refresh_data()

    # ------------------------------------------------------------------
    # Atualização de dados
    # ------------------------------------------------------------------
    def refresh_data(self) -> None:
        summary = self.database.get_monthly_summary(self.user_id, self.current_month)
        for key, card in self.cards.items():
            value_label = card.findChild(QLabel, "CardValue")
            if value_label:
                amount = summary.get(key, 0.0)
                value_label.setText(f"R$ {amount:,.2f}".replace(",", "_").replace(".", ",").replace("_", "."))

        transactions = self.database.get_transactions(self.user_id, self.current_month)
        incomes = [t for t in transactions if t.kind == "entrada"]
        expenses = [t for t in transactions if t.kind == "saida"]
        self._populate_transactions(self.income_table, incomes)
        self._populate_transactions(self.expense_table, expenses)

        accounts = self.database.get_accounts_payable(self.user_id)
        self._populate_accounts(accounts)
        self.calendar.update_accounts(accounts)

        self._update_line_chart(expenses)
        self._update_pie_chart(expenses)
        self._update_bar_chart()

    def _populate_transactions(self, table: QTableWidget, transactions: List[Transaction]) -> None:
        table.setRowCount(len(transactions))
        for row, transaction in enumerate(transactions):
            table.setItem(row, 0, QTableWidgetItem(transaction.occurred_at.strftime("%d/%m")))
            table.setItem(row, 1, QTableWidgetItem(transaction.category))
            table.setItem(row, 2, QTableWidgetItem(transaction.description))
            table.setItem(
                row,
                3,
                QTableWidgetItem(
                    f"R$ {transaction.amount:,.2f}".replace(",", "_").replace(".", ",").replace("_", ".")
                ),
            )

    def _populate_accounts(self, accounts: List[AccountPayable]) -> None:
        self.accounts_table.setRowCount(len(accounts))
        for row, account in enumerate(accounts):
            self.accounts_table.setItem(row, 0, QTableWidgetItem(account.title))
            self.accounts_table.setItem(row, 1, QTableWidgetItem(account.due_date.strftime("%d/%m")))
            self.accounts_table.setItem(
                row,
                2,
                QTableWidgetItem(
                    f"R$ {account.amount:,.2f}".replace(",", "_").replace(".", ",").replace("_", ".")
                ),
            )
            self.accounts_table.setItem(row, 3, QTableWidgetItem(account.status.title()))

    def _update_line_chart(self, expenses: List[Transaction]) -> None:
        chart = QChart()
        chart.setTitle("Evolução das despesas")
        series = QLineSeries()

        if not expenses:
            series.append(QPointF(0, 0))
        else:
            totals: Dict[int, float] = {}
            for expense in expenses:
                totals.setdefault(expense.occurred_at.day, 0.0)
                totals[expense.occurred_at.day] += expense.amount
            cumulative = 0.0
            for day in sorted(totals):
                cumulative += totals[day]
                series.append(QPointF(day, cumulative))

        chart.addSeries(series)
        axis_x = QValueAxis()
        axis_x.setLabelFormat("%d")
        axis_x.setTitleText("Dia")
        axis_y = QValueAxis()
        axis_y.setLabelFormat("R$ %.0f")
        axis_y.setTitleText("Valor acumulado")
        chart.addAxis(axis_x, Qt.AlignmentFlag.AlignBottom)
        chart.addAxis(axis_y, Qt.AlignmentFlag.AlignLeft)
        series.attachAxis(axis_x)
        series.attachAxis(axis_y)
        chart.legend().hide()
        chart.setAnimationOptions(QChart.AnimationOption.SeriesAnimations)
        chart.setAnimationEasingCurve(QEasingCurve.Type.OutCubic)
        self.line_chart_view.setChart(chart)

    def _update_pie_chart(self, expenses: List[Transaction]) -> None:
        chart = QChart()
        chart.setTitle("Despesas por categoria")
        series = QPieSeries()
        totals: Dict[str, float] = {}
        for expense in expenses:
            totals.setdefault(expense.category, 0.0)
            totals[expense.category] += expense.amount

        for category, amount in totals.items():
            slice_ = series.append(category, amount)
            slice_.setLabelVisible(True)
            slice_.setExploded(True)

        chart.addSeries(series)
        chart.legend().setVisible(True)
        chart.legend().setAlignment(Qt.AlignmentFlag.AlignBottom)
        self.pie_chart_view.setChart(chart)

    def _update_bar_chart(self) -> None:
        chart = QChart()
        chart.setTitle("Comparativo mensal")
        months = [self.current_month - timedelta(days=30 * i) for i in reversed(range(4))]
        data = self.database.iter_monthly_comparison(self.user_id, months)

        entries_set = QBarSet("Entradas")
        exits_set = QBarSet("Saídas")

        categories = []
        for label, entradas, saidas in data:
            categories.append(label)
            entries_set.append(entradas)
            exits_set.append(saidas)

        series = QBarSeries()
        series.append(entries_set)
        series.append(exits_set)

        chart.addSeries(series)
        axis_x = QBarCategoryAxis()
        axis_x.append(categories)
        chart.addAxis(axis_x, Qt.AlignmentFlag.AlignBottom)
        series.attachAxis(axis_x)

        axis_y = QValueAxis()
        axis_y.setLabelFormat("R$ %.0f")
        chart.addAxis(axis_y, Qt.AlignmentFlag.AlignLeft)
        series.attachAxis(axis_y)

        chart.legend().setVisible(True)
        chart.legend().setAlignment(Qt.AlignmentFlag.AlignBottom)
        chart.setAnimationOptions(QChart.AnimationOption.SeriesAnimations)
        self.bar_chart_view.setChart(chart)

    # ------------------------------------------------------------------
    # Temas
    # ------------------------------------------------------------------
    def apply_theme(self, key: str) -> None:
        self.current_theme_key = key
        theme = self.theme_loader.get_theme(key)
        palette = theme["palette"]
        stylesheet = f"""
            QMainWindow {{
                background-color: {palette['window']};
                color: {palette['text']};
            }}
            QLabel {{
                color: {palette['text']};
            }}
            QFrame#SummaryCard, QFrame#SectionCard {{
                background-color: {palette['card']};
                border-radius: 18px;
                border: 1px solid rgba(255, 255, 255, 0.05);
            }}
            QLabel#CardValue {{
                color: {palette['accent']};
            }}
            QTableWidget {{
                background-color: transparent;
                color: {palette['card_text']};
                gridline-color: rgba(255, 255, 255, 0.1);
                alternate-background-color: rgba(255, 255, 255, 0.04);
            }}
            QPushButton {{
                background-color: {palette['primary']};
                color: {palette['card_text']};
                padding: 10px 18px;
                border-radius: 14px;
            }}
            QPushButton:hover {{
                background-color: {palette['accent']};
            }}
        """
        self.setStyleSheet(stylesheet)
        self.calendar.set_palette_colors(
            paid_color=QColor(palette["positive"]),
            pending_color=QColor(palette["warning"]),
            overdue_color=QColor(palette["negative"]),
        )
        self.refresh_data()


class TransactionDialog(QDialog):
    """Formulário para criação de transações e contas."""

    def __init__(self, database: DatabaseManager, user_id: int, parent=None) -> None:
        super().__init__(parent)
        self.database = database
        self.user_id = user_id
        self.setWindowTitle("Nova transação")
        self.setModal(True)
        self.setMinimumWidth(420)
        layout = QVBoxLayout(self)
        layout.setSpacing(12)

        self.type_selector = QComboBox()
        self.type_selector.addItems(["Entrada", "Saída", "Conta a pagar"])

        form = QFormLayout()
        self.amount_field = QLineEdit()
        self.amount_field.setPlaceholderText("Valor em reais")
        self.category_field = QLineEdit()
        self.category_field.setPlaceholderText("Categoria")
        self.description_field = QTextEdit()
        self.description_field.setPlaceholderText("Descrição opcional")
        self.day_field = QSpinBox()
        self.day_field.setRange(1, 31)
        self.day_field.setValue(date.today().day)

        form.addRow("Tipo", self.type_selector)
        form.addRow("Valor", self.amount_field)
        form.addRow("Categoria / Conta", self.category_field)
        form.addRow("Descrição", self.description_field)
        form.addRow("Dia", self.day_field)

        layout.addLayout(form)
        buttons = QDialogButtonBox(QDialogButtonBox.StandardButton.Ok | QDialogButtonBox.StandardButton.Cancel)
        buttons.button(QDialogButtonBox.StandardButton.Ok).setText("Salvar")
        buttons.button(QDialogButtonBox.StandardButton.Cancel).setText("Cancelar")
        buttons.accepted.connect(self._save)  # type: ignore[arg-type]
        buttons.rejected.connect(self.reject)  # type: ignore[arg-type]
        layout.addWidget(buttons)

    def _save(self) -> None:
        try:
            amount = float(self.amount_field.text().replace(",", "."))
        except ValueError:
            QMessageBox.warning(self.dialog, "Valor inválido", "Informe um valor numérico válido.")
            return

        description = self.description_field.toPlainText().strip()
        category = self.category_field.text().strip() or "Outros"
        selected_day = self.day_field.value()
        event_date = self._resolve_date(selected_day)
        type_key = self.type_selector.currentText().lower()

        if type_key == "conta a pagar":
            self.database.add_account_payable(
                user_id=self.user_id,
                title=category,
                amount=amount,
                due_date=event_date,
            )
        else:
            kind = "entrada" if type_key == "entrada" else "saida"
            self.database.add_transaction(
                user_id=self.user_id,
                kind=kind,
                amount=amount,
                category=category,
                occurred_at=event_date,
                description=description,
            )
        self.accept()

    @staticmethod
    def _resolve_date(day: int) -> date:
        today = date.today()
        try:
            return today.replace(day=day)
        except ValueError:
            # Ajuste para meses com menos dias
            while True:
                try:
                    today = today.replace(day=day)
                    return today
                except ValueError:
                    day -= 1


class FinanceCalendar(QCalendarWidget):
    """Calendário customizado para exibir as contas a pagar."""

    def __init__(self) -> None:
        super().__init__()
        self.paid_color = QColor("#4caf50")
        self.pending_color = QColor("#ffca28")
        self.overdue_color = QColor("#ef5350")
        self.accounts: List[AccountPayable] = []
        self._highlighted_dates: List[date] = []

    def set_palette_colors(self, paid_color: QColor, pending_color: QColor, overdue_color: QColor) -> None:
        self.paid_color = paid_color
        self.pending_color = pending_color
        self.overdue_color = overdue_color
        self._refresh_highlights()

    def update_accounts(self, accounts: List[AccountPayable]) -> None:
        self.accounts = accounts
        self._refresh_highlights()

    def _refresh_highlights(self) -> None:
        for highlighted in self._highlighted_dates:
            self.setDateTextFormat(highlighted, QTextCharFormat())
        self._highlighted_dates.clear()

        default_format = QTextCharFormat()
        self.setWeekdayTextFormat(Qt.DayOfWeek.Monday, default_format)
        self.setWeekdayTextFormat(Qt.DayOfWeek.Tuesday, default_format)
        self.setWeekdayTextFormat(Qt.DayOfWeek.Wednesday, default_format)
        self.setWeekdayTextFormat(Qt.DayOfWeek.Thursday, default_format)
        self.setWeekdayTextFormat(Qt.DayOfWeek.Friday, default_format)
        self.setWeekdayTextFormat(Qt.DayOfWeek.Saturday, default_format)
        self.setWeekdayTextFormat(Qt.DayOfWeek.Sunday, default_format)

        today = date.today()
        for account in self.accounts:
            text_format = QTextCharFormat()
            if account.status.lower() == "paga":
                text_format.setBackground(self.paid_color)
            else:
                due_date = account.due_date
                if due_date < today:
                    text_format.setBackground(self.overdue_color)
                else:
                    text_format.setBackground(self.pending_color)
            self.setDateTextFormat(account.due_date, text_format)
            self._highlighted_dates.append(account.due_date)


__all__ = ["DashboardWindow", "TransactionDialog", "FinanceCalendar"]
