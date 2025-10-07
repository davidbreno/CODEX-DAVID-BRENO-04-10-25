# FINACE DAVID (Android)

Aplicativo financeiro pessoal desenvolvido em Kotlin com Jetpack Compose e Room, preparado para abertura direta no Android Studio.

## Pré-requisitos

- Android Studio Iguana ou superior
- Android SDK 34
- Java 17 (instalado automaticamente com o Android Studio)

## Como abrir no Android Studio

1. Abra o Android Studio.
2. Escolha **Open an Existing Project** e selecione a pasta deste repositório.
3. Aguarde a sincronização do Gradle.

## Build e execução

```bash
./gradlew assembleDebug
./gradlew installDebug
```

Para executar no emulador ou dispositivo, utilize o botão **Run** do Android Studio ou o comando `./gradlew connectedDebugAndroidTest`.

### Publicação

```bash
./gradlew assembleRelease
```

Arquivo gerado em `app/build/outputs/apk/release/`.

## Estrutura principal

- `app/src/main/java/com/finacedavid` — código Kotlin (Camadas MVVM, repositórios, UI Compose)
- `app/src/main/res` — recursos visuais
- `app/src/test` — testes unitários (JUnit)
- `app/src/androidTest` — testes de interface (Compose UI test)

## Funcionalidades

- Autenticação local com PIN ou senha armazenada com `EncryptedSharedPreferences`
- Dashboard com gráficos (rosca e linha) e resumo de entradas/saídas
- Lista filtrável de transações com busca, abas e formulário de cadastro/edição
- Gestão de contas a pagar com agrupamentos (vencidas, hoje, próximas)
- Calendário mensal interativo destacando totais por dia
- Tema claro/escuro com persistência em DataStore
- Persistência 100% local com Room (SQLite)

## Testes

```bash
./gradlew test
./gradlew connectedAndroidTest
```
