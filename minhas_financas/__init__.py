"""Pacote principal do aplicativo Minhas Finanças."""

from .login_window import LoginWindow
from .register_window import RegisterDialog
from .dashboard import DashboardWindow
from .database import DatabaseManager

__all__ = [
    "LoginWindow",
    "RegisterDialog",
    "DashboardWindow",
    "DatabaseManager",
]
