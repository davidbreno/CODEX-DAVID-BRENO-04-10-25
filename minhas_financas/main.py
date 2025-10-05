"""Ponto de entrada para executar o aplicativo Minhas Finanças."""

from __future__ import annotations

from PyQt6.QtWidgets import QApplication

from .login_window import LoginWindow


def run() -> None:
    import sys

    app = QApplication(sys.argv)
    window = LoginWindow()
    window.show()
    sys.exit(app.exec())


if __name__ == "__main__":
    run()
