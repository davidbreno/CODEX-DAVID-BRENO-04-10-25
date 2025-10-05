"""Módulo de acesso a dados para o aplicativo Minhas Finanças."""

from __future__ import annotations

import hashlib
import sqlite3
from dataclasses import dataclass
from datetime import date, datetime
from pathlib import Path
from typing import Dict, Iterable, List, Optional, Tuple

DB_PATH = Path(__file__).resolve().parent / "minhas_financas.db"


@dataclass
class Transaction:
    """Representa uma transação financeira genérica."""

    id: int
    user_id: int
    kind: str
    amount: float
    category: str
    occurred_at: date
    description: str
    status: str


@dataclass
class AccountPayable:
    """Representa uma conta a pagar cadastrada pelo usuário."""

    id: int
    user_id: int
    title: str
    amount: float
    due_date: date
    status: str


class DatabaseManager:
    """Responsável por encapsular toda a lógica de acesso ao SQLite."""

    def __init__(self, database_path: Path = DB_PATH) -> None:
        self.database_path = Path(database_path)
        self.database_path.parent.mkdir(parents=True, exist_ok=True)
        self._connection = sqlite3.connect(self.database_path)
        self._connection.row_factory = sqlite3.Row
        self._create_tables()

    # ------------------------------------------------------------------
    # Utilidades internas
    # ------------------------------------------------------------------
    def _create_tables(self) -> None:
        cursor = self._connection.cursor()
        cursor.execute(
            """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                password_hash TEXT NOT NULL,
                created_at TEXT NOT NULL
            )
            """
        )
        cursor.execute(
            """
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                kind TEXT NOT NULL,
                amount REAL NOT NULL,
                category TEXT NOT NULL,
                occurred_at TEXT NOT NULL,
                description TEXT,
                status TEXT NOT NULL DEFAULT 'normal',
                FOREIGN KEY(user_id) REFERENCES users(id)
            )
            """
        )
        cursor.execute(
            """
            CREATE TABLE IF NOT EXISTS accounts_payable (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                title TEXT NOT NULL,
                amount REAL NOT NULL,
                due_date TEXT NOT NULL,
                status TEXT NOT NULL DEFAULT 'pendente',
                FOREIGN KEY(user_id) REFERENCES users(id)
            )
            """
        )
        self._connection.commit()

    @staticmethod
    def _hash_password(raw_password: str) -> str:
        return hashlib.sha256(raw_password.encode("utf-8")).hexdigest()

    # ------------------------------------------------------------------
    # Operações de usuário
    # ------------------------------------------------------------------
    def register_user(self, name: str, email: str, password: str) -> bool:
        cursor = self._connection.cursor()
        try:
            cursor.execute(
                "INSERT INTO users (name, email, password_hash, created_at) VALUES (?, ?, ?, ?)",
                (name, email, self._hash_password(password), datetime.utcnow().isoformat()),
            )
            self._connection.commit()
            return True
        except sqlite3.IntegrityError:
            return False

    def authenticate_user(self, email: str, password: str) -> Optional[sqlite3.Row]:
        cursor = self._connection.cursor()
        cursor.execute("SELECT * FROM users WHERE email = ?", (email,))
        row = cursor.fetchone()
        if row and row["password_hash"] == self._hash_password(password):
            return row
        return None

    def get_user(self, user_id: int) -> Optional[sqlite3.Row]:
        cursor = self._connection.cursor()
        cursor.execute("SELECT * FROM users WHERE id = ?", (user_id,))
        return cursor.fetchone()

    # ------------------------------------------------------------------
    # Operações de transações e contas
    # ------------------------------------------------------------------
    def add_transaction(
        self,
        user_id: int,
        kind: str,
        amount: float,
        category: str,
        occurred_at: date,
        description: str = "",
        status: str = "normal",
    ) -> int:
        cursor = self._connection.cursor()
        cursor.execute(
            """
            INSERT INTO transactions (user_id, kind, amount, category, occurred_at, description, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """,
            (
                user_id,
                kind,
                float(amount),
                category,
                occurred_at.isoformat(),
                description,
                status,
            ),
        )
        self._connection.commit()
        return int(cursor.lastrowid)

    def get_transactions(self, user_id: int, month: date) -> List[Transaction]:
        cursor = self._connection.cursor()
        start = month.replace(day=1)
        if start.month == 12:
            end = start.replace(year=start.year + 1, month=1, day=1)
        else:
            end = start.replace(month=start.month + 1, day=1)
        cursor.execute(
            """
            SELECT * FROM transactions
            WHERE user_id = ? AND occurred_at >= ? AND occurred_at < ?
            ORDER BY occurred_at ASC
            """,
            (user_id, start.isoformat(), end.isoformat()),
        )
        rows = cursor.fetchall()
        return [
            Transaction(
                id=row["id"],
                user_id=row["user_id"],
                kind=row["kind"],
                amount=row["amount"],
                category=row["category"],
                occurred_at=datetime.fromisoformat(row["occurred_at"]).date(),
                description=row["description"] or "",
                status=row["status"],
            )
            for row in rows
        ]

    def add_account_payable(
        self, user_id: int, title: str, amount: float, due_date: date, status: str = "pendente"
    ) -> int:
        cursor = self._connection.cursor()
        cursor.execute(
            """
            INSERT INTO accounts_payable (user_id, title, amount, due_date, status)
            VALUES (?, ?, ?, ?, ?)
            """,
            (user_id, title, float(amount), due_date.isoformat(), status),
        )
        self._connection.commit()
        return int(cursor.lastrowid)

    def update_account_status(self, account_id: int, status: str) -> None:
        cursor = self._connection.cursor()
        cursor.execute(
            "UPDATE accounts_payable SET status = ? WHERE id = ?",
            (status, account_id),
        )
        self._connection.commit()

    def get_accounts_payable(self, user_id: int) -> List[AccountPayable]:
        cursor = self._connection.cursor()
        cursor.execute(
            "SELECT * FROM accounts_payable WHERE user_id = ? ORDER BY due_date ASC",
            (user_id,),
        )
        rows = cursor.fetchall()
        return [
            AccountPayable(
                id=row["id"],
                user_id=row["user_id"],
                title=row["title"],
                amount=row["amount"],
                due_date=datetime.fromisoformat(row["due_date"]).date(),
                status=row["status"],
            )
            for row in rows
        ]

    # ------------------------------------------------------------------
    # Relatórios
    # ------------------------------------------------------------------
    def get_monthly_summary(self, user_id: int, month: date) -> Dict[str, float]:
        transactions = self.get_transactions(user_id, month)
        saldo_inicial = 0.0
        entradas = sum(t.amount for t in transactions if t.kind == "entrada")
        saidas = sum(t.amount for t in transactions if t.kind == "saida")
        saldo_atual = saldo_inicial + entradas - saidas
        previsao = saldo_atual - sum(
            a.amount for a in self.get_accounts_payable(user_id) if a.due_date.month == month.month
        )
        return {
            "saldo_inicial": round(saldo_inicial, 2),
            "saldo_atual": round(saldo_atual, 2),
            "previsao": round(previsao, 2),
            "entradas": round(entradas, 2),
            "saidas": round(saidas, 2),
        }

    def get_category_summary(self, user_id: int, month: date) -> Dict[str, float]:
        transactions = self.get_transactions(user_id, month)
        summary: Dict[str, float] = {}
        for transaction in transactions:
            if transaction.kind != "saida":
                continue
            summary.setdefault(transaction.category, 0.0)
            summary[transaction.category] += transaction.amount
        return {category: round(value, 2) for category, value in summary.items()}

    def get_balance_progress(self, user_id: int, month: date) -> List[Tuple[date, float]]:
        transactions = sorted(self.get_transactions(user_id, month), key=lambda t: t.occurred_at)
        balance = 0.0
        progress: List[Tuple[date, float]] = []
        for transaction in transactions:
            if transaction.kind == "entrada":
                balance += transaction.amount
            else:
                balance -= transaction.amount
            progress.append((transaction.occurred_at, round(balance, 2)))
        return progress

    def iter_monthly_comparison(self, user_id: int, months: Iterable[date]) -> List[Tuple[str, float, float]]:
        data: List[Tuple[str, float, float]] = []
        for month in months:
            summary = self.get_monthly_summary(user_id, month)
            data.append(
                (
                    month.strftime("%b/%Y"),
                    summary["entradas"],
                    summary["saidas"],
                )
            )
        return data

    # ------------------------------------------------------------------
    # Utilidades gerais
    # ------------------------------------------------------------------
    def close(self) -> None:
        self._connection.close()


def bootstrap_demo_data(database: DatabaseManager, user_id: int) -> None:
    """Cria alguns dados de demonstração para facilitar os testes manuais."""

    today = date.today()
    if database.get_transactions(user_id, today):
        return

    categories = ["Moradia", "Alimentação", "Transporte", "Lazer", "Investimentos"]
    amounts = [1200.0, 600.0, 250.0, 180.0, 400.0]
    for category, amount in zip(categories, amounts):
        database.add_transaction(
            user_id=user_id,
            kind="saida",
            amount=amount,
            category=category,
            occurred_at=today.replace(day=min(today.day, 20)),
            description=f"Despesa de {category.lower()}",
        )
    database.add_transaction(
        user_id=user_id,
        kind="entrada",
        amount=4200.0,
        category="Salário",
        occurred_at=today.replace(day=5),
        description="Recebimento mensal",
    )
    database.add_account_payable(
        user_id=user_id,
        title="Conta de energia",
        amount=280.0,
        due_date=today.replace(day=min(today.day, 25)),
        status="pendente",
    )
    database.add_account_payable(
        user_id=user_id,
        title="Internet",
        amount=120.0,
        due_date=today.replace(day=min(today.day, 10)),
        status="paga",
    )
