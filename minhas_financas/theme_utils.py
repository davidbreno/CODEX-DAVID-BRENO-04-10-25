"""Utilidades para carregamento e aplicação de temas."""

from __future__ import annotations

import json
from pathlib import Path
from typing import Dict

THEMES_FILE = Path(__file__).resolve().parent / "themes.json"


class ThemeLoader:
    """Carrega os temas disponíveis a partir de um arquivo JSON."""

    def __init__(self, themes_path: Path = THEMES_FILE) -> None:
        self.themes_path = Path(themes_path)
        with self.themes_path.open("r", encoding="utf-8") as handle:
            self._themes: Dict[str, Dict[str, Dict[str, str]]] = json.load(handle)

    def get_theme(self, key: str) -> Dict[str, Dict[str, str]]:
        return self._themes[key]

    def list_themes(self) -> Dict[str, str]:
        return {key: value["name"] for key, value in self._themes.items()}


__all__ = ["ThemeLoader", "THEMES_FILE"]
