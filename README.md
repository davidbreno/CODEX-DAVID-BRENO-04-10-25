# FINACE DAVID

Aplicativo financeiro pessoal construído com .NET 8 e .NET MAUI para Windows e Android. O projeto segue a proposta visual de um dashboard bancário escuro com cartões arredondados, gráficos interativos e calendário financeiro, permitindo o controle local de entradas, saídas e contas a pagar.

## Pré-requisitos

- [.NET SDK 8.0](https://dotnet.microsoft.com/download)
- Workload .NET MAUI instalada (`dotnet workload install maui`)
- Windows 10 19041 ou superior para o target desktop
- Android SDK configurado (API 34 recomendada) para o target móvel

## Estrutura do projeto

```
FinaceDavid.sln
├── src/FinaceDavid.App/       # Aplicativo MAUI com MVVM, serviços e persistência SQLite
└── tests/FinaceDavid.Tests/   # Testes de unidade e de fluxo de viewmodels
```

### Principais camadas

- **Domain**: entidades e enumerações principais.
- **Data**: repositórios SQLite com sqlite-net.
- **Services**: regras de negócio, autenticação, relatórios, calendário e filtros compartilhados.
- **ViewModels**: implementação MVVM com CommunityToolkit.
- **Views**: páginas XAML com gráficos desenhados manualmente (`GraphicsView`).

## Executando o projeto

### Build rápido

```bash
dotnet build FinaceDavid.sln
```

### Executar no Windows

```bash
dotnet maui run -t windows -f net8.0-windows10.0.19041.0 --project src/FinaceDavid.App/FinaceDavid.App.csproj
```

### Executar no Android (emulador/dispositivo configurado)

```bash
dotnet maui run -t android --project src/FinaceDavid.App/FinaceDavid.App.csproj
```

### Publicação

- **Windows (.msix)**

  ```bash
  dotnet publish src/FinaceDavid.App/FinaceDavid.App.csproj -f net8.0-windows10.0.19041.0 -c Release -o publish/windows
  ```

- **Android (.apk)**

  ```bash
  dotnet publish src/FinaceDavid.App/FinaceDavid.App.csproj -f net8.0-android -c Release -o publish/android
  ```

## Testes

Execute todos os testes com:

```bash
dotnet test FinaceDavid.sln
```

Os testes de unidade cobrem cálculos de saldo e agregações de relatórios; o teste de fluxo valida a criação de transações via viewmodel.

## Funcionalidades implementadas

- Login local com criação de PIN/senha no primeiro uso utilizando `SecureStorage`.
- Dashboard com saldo, distribuição por gráfico de rosca, tendência de entradas/saídas e atalhos rápidos.
- Lista de transações com filtros por tipo, período, texto e status (pagas/pendentes), além de cadastro/edição.
- Contas a pagar agrupadas por situação, com marcação como paga e opção de gerar saída.
- Calendário mensal interativo com totais diários e integração com filtros globais.
- Tema escuro/claro com persistência de preferência.
- Persistência 100% local em SQLite com dados seed para demonstração.
- Recursos de acessibilidade: rótulos claros, áreas de toque ≥48dp e contraste reforçado.

## Backup local

O arquivo `Resources/Raw/appsettings.json` serve como ponto central para preferências locais. As bases SQLite são criadas em `FileSystem.AppDataDirectory` com o nome `finacedavid.db3`, permitindo cópia manual para backup.

## Convenções

- Código organizado em MVVM utilizando `CommunityToolkit.Mvvm` (atributos `[ObservableProperty]`, `[RelayCommand]`).
- Estilos em `Resources/Styles`, com suporte a temas e cores dinâmicas.
- Locale padrão pt-BR e formato monetário BRL.

---

Para dúvidas ou melhorias, abra uma issue descrevendo o contexto e os passos para reproduzir.
