"""Janela de cadastro de novo usuário."""

from __future__ import annotations

from PyQt6.QtCore import Qt
from PyQt6.QtWidgets import (
    QDialog,
    QDialogButtonBox,
    QFormLayout,
    QLabel,
    QLineEdit,
    QMessageBox,
    QPushButton,
    QVBoxLayout,
)

from .database import DatabaseManager


class RegisterDialog(QDialog):
    """Janela modal para cadastro de novos usuários."""

    def __init__(self, database: DatabaseManager, parent=None) -> None:
        super().__init__(parent)
        self.database = database
        self.setWindowTitle("Registrar nova conta")
        self.setModal(True)
        self.setMinimumWidth(360)
        self._build_ui()

    def _build_ui(self) -> None:
        layout = QVBoxLayout(self)
        title = QLabel("Criar nova conta")
        title.setAlignment(Qt.AlignmentFlag.AlignHCenter)
        title.setObjectName("DialogTitle")
        subtitle = QLabel("Informe seus dados para começar a usar o Minhas Finanças")
        subtitle.setWordWrap(True)
        subtitle.setAlignment(Qt.AlignmentFlag.AlignHCenter)

        form = QFormLayout()
        self.name_field = QLineEdit()
        self.name_field.setPlaceholderText("Seu nome completo")
        self.email_field = QLineEdit()
        self.email_field.setPlaceholderText("E-mail")
        self.password_field = QLineEdit()
        self.password_field.setPlaceholderText("Senha")
        self.password_field.setEchoMode(QLineEdit.EchoMode.Password)
        self.confirm_field = QLineEdit()
        self.confirm_field.setPlaceholderText("Confirmar senha")
        self.confirm_field.setEchoMode(QLineEdit.EchoMode.Password)

        form.addRow("Nome", self.name_field)
        form.addRow("E-mail", self.email_field)
        form.addRow("Senha", self.password_field)
        form.addRow("Confirmar senha", self.confirm_field)

        buttons = QDialogButtonBox()
        register_button = QPushButton("Registrar")
        cancel_button = QPushButton("Cancelar")
        buttons.addButton(register_button, QDialogButtonBox.ButtonRole.AcceptRole)
        buttons.addButton(cancel_button, QDialogButtonBox.ButtonRole.RejectRole)

        register_button.clicked.connect(self._handle_register)  # type: ignore[arg-type]
        cancel_button.clicked.connect(self.reject)  # type: ignore[arg-type]

        layout.addWidget(title)
        layout.addWidget(subtitle)
        layout.addLayout(form)
        layout.addWidget(buttons)

    # ------------------------------------------------------------------
    # Ações
    # ------------------------------------------------------------------
    def _handle_register(self) -> None:
        name = self.name_field.text().strip()
        email = self.email_field.text().strip().lower()
        password = self.password_field.text()
        confirm = self.confirm_field.text()

        if not name or not email or not password:
            QMessageBox.warning(self, "Campos obrigatórios", "Preencha todos os campos para continuar.")
            return

        if "@" not in email:
            QMessageBox.warning(self, "E-mail inválido", "Informe um e-mail válido.")
            return

        if password != confirm:
            QMessageBox.warning(self, "Confirmação inválida", "As senhas não coincidem.")
            return

        if len(password) < 6:
            QMessageBox.warning(
                self,
                "Senha fraca",
                "Escolha uma senha com pelo menos 6 caracteres para garantir maior segurança.",
            )
            return

        if not self.database.register_user(name=name, email=email, password=password):
            QMessageBox.critical(
                self,
                "E-mail já cadastrado",
                "Já existe uma conta associada a esse e-mail.",
            )
            return

        QMessageBox.information(self, "Cadastro concluído", "Sua conta foi criada com sucesso!")
        self.accept()


__all__ = ["RegisterDialog"]
