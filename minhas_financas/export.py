"""Geração de relatórios para o aplicativo Minhas Finanças."""

from __future__ import annotations

import csv
from datetime import date
from pathlib import Path
from typing import Iterable

from .database import DatabaseManager


def export_monthly_report(
    database: DatabaseManager, user_id: int, month: date, destination: Path
) -> Path:
    """Exporta um relatório mensal em formato CSV."""

    destination = Path(destination)
    destination.parent.mkdir(parents=True, exist_ok=True)

    transactions = database.get_transactions(user_id, month)
    accounts = database.get_accounts_payable(user_id)
    summary = database.get_monthly_summary(user_id, month)

    with destination.open("w", newline="", encoding="utf-8") as handle:
        writer = csv.writer(handle, delimiter=";")
        writer.writerow(["Minhas Finanças - Relatório Mensal"])
        writer.writerow(["Usuário", database.get_user(user_id)["name"]])
        writer.writerow(["Mês", month.strftime("%B/%Y")])
        writer.writerow([])

        writer.writerow(["Resumo"])
        for key, value in summary.items():
            writer.writerow([key.replace("_", " ").title(), f"R$ {value:,.2f}".replace(",", "_").replace(".", ",").replace("_", ".")])
        writer.writerow([])

        writer.writerow(["Transações"])
        writer.writerow(["Data", "Tipo", "Categoria", "Descrição", "Valor", "Status"])
        for transaction in transactions:
            writer.writerow(
                [
                    transaction.occurred_at.strftime("%d/%m/%Y"),
                    transaction.kind,
                    transaction.category,
                    transaction.description,
                    f"R$ {transaction.amount:,.2f}".replace(",", "_").replace(".", ",").replace("_", "."),
                    transaction.status,
                ]
            )
        writer.writerow([])

        writer.writerow(["Contas a pagar"])
        writer.writerow(["Título", "Vencimento", "Valor", "Status"])
        for account in accounts:
            writer.writerow(
                [
                    account.title,
                    account.due_date.strftime("%d/%m/%Y"),
                    f"R$ {account.amount:,.2f}".replace(",", "_").replace(".", ",").replace("_", "."),
                    account.status,
                ]
            )

    return destination


def export_multiple_months(
    database: DatabaseManager, user_id: int, months: Iterable[date], destination: Path
) -> Path:
    """Exporta vários relatórios em um único arquivo CSV."""

    destination = Path(destination)
    destination.parent.mkdir(parents=True, exist_ok=True)

    with destination.open("w", newline="", encoding="utf-8") as handle:
        writer = csv.writer(handle, delimiter=";")
        writer.writerow(["Minhas Finanças - Relatório Consolidado"])
        writer.writerow(["Usuário", database.get_user(user_id)["name"]])
        writer.writerow([])
        writer.writerow(["Mês", "Entradas", "Saídas", "Saldo", "Previsão"])

        for month in months:
            summary = database.get_monthly_summary(user_id, month)
            writer.writerow(
                [
                    month.strftime("%B/%Y"),
                    f"R$ {summary['entradas']:,.2f}".replace(",", "_").replace(".", ",").replace("_", "."),
                    f"R$ {summary['saidas']:,.2f}".replace(",", "_").replace(".", ",").replace("_", "."),
                    f"R$ {summary['saldo_atual']:,.2f}".replace(",", "_").replace(".", ",").replace("_", "."),
                    f"R$ {summary['previsao']:,.2f}".replace(",", "_").replace(".", ",").replace("_", "."),
                ]
            )

    return destination
