"""Tela de autenticação do aplicativo Minhas Finanças."""

from __future__ import annotations

from typing import Optional

from PyQt6.QtCore import Qt
from PyQt6.QtGui import QFont
from PyQt6.QtWidgets import (
    QApplication,
    QGridLayout,
    QHBoxLayout,
    QLabel,
    QLineEdit,
    QMessageBox,
    QPushButton,
    QVBoxLayout,
    QWidget,
)

from .dashboard import DashboardWindow
from .database import DatabaseManager, bootstrap_demo_data
from .register_window import RegisterDialog
from .theme_utils import ThemeLoader


class LoginWindow(QWidget):
    """Janela principal exibida ao iniciar o aplicativo."""

    def __init__(self, database: Optional[DatabaseManager] = None) -> None:
        super().__init__()
        self.database = database or DatabaseManager()
        self.theme_loader = ThemeLoader()
        self.setWindowTitle("Minhas Finanças - Login")
        self.setMinimumSize(640, 480)
        self._build_ui()
        self.apply_theme("dark_neon")

    def _build_ui(self) -> None:
        layout = QHBoxLayout(self)

        hero = QLabel("Minhas\nFinanças")
        hero.setAlignment(Qt.AlignmentFlag.AlignCenter)
        hero.setFont(QFont("Segoe UI", 42, QFont.Weight.Bold))
        hero.setObjectName("HeroTitle")

        form_container = QWidget()
        form_container.setObjectName("FormContainer")
        form_layout = QVBoxLayout(form_container)
        form_layout.setContentsMargins(32, 32, 32, 32)
        form_layout.setSpacing(16)

        title = QLabel("Bem-vindo de volta!")
        title.setFont(QFont("Segoe UI", 20, QFont.Weight.Medium))

        subtitle = QLabel("Gerencie suas finanças pessoais com um painel completo.")
        subtitle.setWordWrap(True)

        grid = QGridLayout()
        self.user_field = QLineEdit()
        self.user_field.setPlaceholderText("E-mail")
        self.password_field = QLineEdit()
        self.password_field.setPlaceholderText("Senha")
        self.password_field.setEchoMode(QLineEdit.EchoMode.Password)

        grid.addWidget(QLabel("Usuário"), 0, 0)
        grid.addWidget(self.user_field, 0, 1)
        grid.addWidget(QLabel("Senha"), 1, 0)
        grid.addWidget(self.password_field, 1, 1)

        buttons = QHBoxLayout()
        self.login_button = QPushButton("Entrar")
        self.register_button = QPushButton("Registrar")
        buttons.addWidget(self.login_button)
        buttons.addWidget(self.register_button)

        self.login_button.clicked.connect(self._handle_login)  # type: ignore[arg-type]
        self.register_button.clicked.connect(self._open_register_dialog)  # type: ignore[arg-type]

        form_layout.addWidget(title)
        form_layout.addWidget(subtitle)
        form_layout.addLayout(grid)
        form_layout.addLayout(buttons)
        form_layout.addStretch()

        layout.addWidget(hero, 1)
        layout.addWidget(form_container, 2)

    # ------------------------------------------------------------------
    # Temas
    # ------------------------------------------------------------------
    def apply_theme(self, theme_key: str) -> None:
        theme = self.theme_loader.get_theme(theme_key)
        palette = theme["palette"]
        self.setStyleSheet(
            f"""
            QWidget {{
                background-color: {palette['window']};
                color: {palette['text']};
            }}
            QLabel#HeroTitle {{
                color: {palette['accent']};
            }}
            QWidget#FormContainer {{
                background-color: {palette['card']};
                border-radius: 18px;
            }}
            QPushButton {{
                padding: 10px 18px;
                border-radius: 12px;
                background-color: {palette['primary']};
                color: {palette['card_text']};
            }}
            QPushButton:hover {{
                background-color: {palette['accent']};
            }}
            QLineEdit {{
                padding: 10px 12px;
                border-radius: 10px;
                background-color: rgba(255, 255, 255, 0.05);
                border: 1px solid rgba(255, 255, 255, 0.05);
                color: {palette['card_text']};
            }}
            """
        )

    # ------------------------------------------------------------------
    # Ações
    # ------------------------------------------------------------------
    def _handle_login(self) -> None:
        email = self.user_field.text().strip().lower()
        password = self.password_field.text()

        user = self.database.authenticate_user(email, password)
        if not user:
            QMessageBox.critical(self, "Login inválido", "Usuário ou senha incorretos.")
            return

        bootstrap_demo_data(self.database, user_id=user["id"])

        dashboard = DashboardWindow(
            database=self.database,
            user_id=user["id"],
            theme_loader=self.theme_loader,
        )
        dashboard.show()
        self.close()

    def _open_register_dialog(self) -> None:
        dialog = RegisterDialog(database=self.database, parent=self)
        if dialog.exec():
            QMessageBox.information(
                self,
                "Cadastro realizado",
                "Utilize seu e-mail e senha para entrar no aplicativo.",
            )


def main() -> None:
    import sys

    app = QApplication(sys.argv)
    window = LoginWindow()
    window.show()
    sys.exit(app.exec())


__all__ = ["LoginWindow", "main"]
