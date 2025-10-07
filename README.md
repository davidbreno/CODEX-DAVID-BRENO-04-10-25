# FINACE DAVID (Android)

Aplicativo financeiro pessoal desenvolvido em Kotlin com Jetpack Compose e Room, preparado para abertura direta no Android Studio.

## Onde executar

O fluxo recomendado é abrir o projeto diretamente no **Android Studio** (IDE gratuita da Google) e usar o emulador oficial que vem com ele. Esse combo oferece a melhor experiência sem custos adicionais e garante compatibilidade total com os recursos do Android.

Caso prefira um aparelho físico, conecte um dispositivo Android com depuração USB habilitada; o Android Studio detectará o telefone automaticamente.

## Pré-requisitos

- Android Studio Iguana ou superior
- Android SDK 34
- Java 17 (instalado automaticamente com o Android Studio)

## Como abrir no Android Studio

1. Abra o Android Studio.
2. Escolha **Open an Existing Project** e selecione a pasta deste repositório.
3. Aguarde a sincronização do Gradle.
4. Crie (ou selecione) um dispositivo virtual em **Device Manager** se quiser usar o emulador gratuito integrado.

## Build e execução

```bash
./gradlew assembleDebug
./gradlew installDebug
```

Para executar no emulador ou dispositivo, utilize o botão **Run** do Android Studio ou o comando `./gradlew connectedDebugAndroidTest`.

### Como gerar o APK final (release)

1. No Android Studio, acesse **Build > Generate Signed Bundle / APK...** e siga o assistente para criar uma chave ou usar uma existente.
2. Escolha **APK**, selecione o módulo `app` e finalize o assistente. O Android Studio fará o build e exibirá o caminho do arquivo no final do processo.

> Alternativa via terminal: `./gradlew assembleRelease` (o arquivo ficará em `app/build/outputs/apk/release/`).

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

---

# FINACE DAVID (Desktop Windows)

Aplicativo desktop WPF (.NET 8) pensado para execução direta no Visual Studio Code ou Visual Studio, com armazenamento local em SQLite e interface escura inspirada em painéis financeiros.

## Pré-requisitos

- Windows 10 ou superior
- .NET 8 SDK com workload `windowsdesktop`
- Visual Studio Code (com extensão C# Dev Kit) ou Visual Studio 2022

> Verifique se o SDK está instalado com `dotnet --list-sdks`.

## Como executar no VS Code

1. Instale a extensão **C# Dev Kit**.
2. Abra a pasta `desktop/` deste repositório no VS Code.
3. Quando solicitado, aceite a restauração dos pacotes NuGet.
4. Utilize o botão **Run and Debug** para iniciar o alvo `FinaceDavid.Desktop` ou execute o comando abaixo:
   ```bash
   dotnet build FinaceDavid.Desktop.sln
   dotnet run --project FinaceDavid.Desktop/FinaceDavid.Desktop.csproj
   ```
5. O app abre na tela de login. Credenciais padrão: usuário `admin`, senha `1234` (alterável em Configurações).

## Publicação

Para gerar um instalador `.msix` ou build otimizado:
```bash
dotnet publish FinaceDavid.Desktop/FinaceDavid.Desktop.csproj -c Release -r win10-x64 --self-contained false
```
O artefato ficará em `FinaceDavid.Desktop/bin/Release/net8.0-windows10.0.19041.0/win10-x64/`.

## Estrutura resumida

- `FinaceDavid.Desktop/` — projeto WPF com MVVM, gráficos (OxyPlot), serviços e persistência local
- `FinaceDavid.Desktop.Tests/` — testes unitários xUnit validando utilidades de domínio
- `FinaceDavid.Desktop/Data` — contexto EF Core, inicialização e opções de banco
- `FinaceDavid.Desktop/Services` — autenticação, transações, contas a pagar, preferências e backup
- `FinaceDavid.Desktop/ViewModels` — view models com CommunityToolkit.Mvvm
- `FinaceDavid.Desktop/Views` — telas XAML (Login, Home, Transações, Contas a Pagar, Calendário, Configurações)

## Funcionalidades destaque

- Login local seguro com hash PBKDF2 (usuário/senha configuráveis)
- Dashboard com gráficos de pizza e área, saldo e atalhos rápidos
- Lista de transações com filtros por tipo, período e busca textual
- Gestão de contas a pagar com agrupamentos (vencidas, hoje, próximas) e geração opcional de saída paga
- Calendário mensal com totais diários de entradas/saídas
- Preferências locais (tema claro/escuro) e exportação/importação de backup JSON

## Testes

```bash
dotnet test FinaceDavid.Desktop.Tests/FinaceDavid.Desktop.Tests.csproj
```

> Todos os dados são persistidos localmente em `%LocalAppData%/FinaceDavid/`.
