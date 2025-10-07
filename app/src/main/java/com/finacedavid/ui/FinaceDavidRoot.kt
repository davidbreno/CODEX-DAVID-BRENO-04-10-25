package com.finacedavid.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.finacedavid.FinaceDavidApp
import com.finacedavid.features.home.HomeUiState
import com.finacedavid.features.home.HomeViewModel
import com.finacedavid.features.login.LoginUiState
import com.finacedavid.features.login.LoginViewModel
import com.finacedavid.features.payables.PayablesViewModel
import com.finacedavid.features.settings.SettingsViewModel
import com.finacedavid.features.transactions.TransactionsViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed class Destinations(val route: String) {
    object Login : Destinations("login")
    object Home : Destinations("home")
    object Transactions : Destinations("transactions")
    object TransactionForm : Destinations("transaction_form")
    object Payables : Destinations("payables")
    object PayableForm : Destinations("payable_form")
    object Calendar : Destinations("calendar")
    object Settings : Destinations("settings")
}

@Composable
fun FinaceDavidRoot(
    navController: NavHostController,
    app: FinaceDavidApp
) {
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.factory(app.authRepository))
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory(app.transactionRepository, app.payableRepository))
    val transactionsViewModel: TransactionsViewModel = viewModel(factory = TransactionsViewModel.factory(app.transactionRepository))
    val payablesViewModel: PayablesViewModel = viewModel(factory = PayablesViewModel.factory(app.payableRepository))
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.factory(app.themeRepository))

    val loginState by loginViewModel.state.collectAsState()
    val homeState by homeViewModel.state.collectAsState()
    val transactionsState by transactionsViewModel.state.collectAsState()
    val payablesState by payablesViewModel.state.collectAsState()
    val isDark by settingsViewModel.isDark.collectAsState()
    val editingTransaction by transactionsViewModel.editing.collectAsState()
    val editingPayable by payablesViewModel.editing.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = Destinations.Login.route) {
        composable(Destinations.Login.route) {
            when (val state = loginState) {
                LoginUiState.Authenticated -> navController.navigate(Destinations.Home.route) {
                    popUpTo(Destinations.Login.route) { inclusive = true }
                }
                LoginUiState.Loading -> Unit
                is LoginUiState.Login -> LoginScreen(
                    state = state,
                    onRegister = { secret, isPin -> loginViewModel.register(secret, isPin) },
                    onAuthenticate = { loginViewModel.authenticate(it) },
                    onSwitchMode = { loginViewModel.reset() }
                )
                is LoginUiState.Setup -> LoginScreen(
                    state = state,
                    onRegister = { secret, isPin -> loginViewModel.register(secret, isPin) },
                    onAuthenticate = { loginViewModel.authenticate(it) },
                    onSwitchMode = { loginViewModel.reset() }
                )
            }
        }
        composable(Destinations.Home.route) {
            HomeScreen(
                state = homeState,
                onSelectFilter = { homeViewModel.updateFilter(it) },
                onNavigateToTransactions = {
                    transactionsViewModel.edit(null)
                    navController.navigate(Destinations.Transactions.route)
                },
                onNavigateToPayables = {
                    payablesViewModel.edit(null)
                    navController.navigate(Destinations.Payables.route)
                },
                onToggleTheme = { settingsViewModel.toggleTheme() },
                onShowCalendar = { navController.navigate(Destinations.Calendar.route) },
                formatCurrency = homeViewModel::formatCurrency
            )
        }
        composable(Destinations.Transactions.route) {
            TransactionsScreen(
                state = transactionsState,
                onTabSelected = { transactionsViewModel.updateTab(it) },
                onFilterChanged = { filter -> transactionsViewModel.setFilter(filter) },
                onCreate = {
                    transactionsViewModel.edit(null)
                    navController.navigate(Destinations.TransactionForm.route)
                },
                onOpen = { transaction ->
                    transactionsViewModel.edit(transaction)
                    navController.navigate(Destinations.TransactionForm.route)
                },
                formatCurrency = homeViewModel::formatCurrency
            )
        }
        composable(Destinations.TransactionForm.route) {
            TransactionFormScreen(initial = editingTransaction, onSubmit = { transaction ->
                coroutineScope.launch {
                    transactionsViewModel.save(transaction)
                    navController.popBackStack()
                }
            })
        }
        composable(Destinations.Payables.route) {
            PayablesScreen(
                state = payablesState,
                onCreate = {
                    payablesViewModel.edit(null)
                    navController.navigate(Destinations.PayableForm.route)
                },
                onOpen = { payable ->
                    payablesViewModel.edit(payable)
                    navController.navigate(Destinations.PayableForm.route)
                },
                onMarkPaid = { payable ->
                    coroutineScope.launch {
                        payablesViewModel.markPaid(payable)
                    }
                },
                formatCurrency = homeViewModel::formatCurrency
            )
        }
        composable(Destinations.PayableForm.route) {
            PayableFormScreen(initial = editingPayable, onSubmit = { payable ->
                coroutineScope.launch {
                    payablesViewModel.save(payable)
                    navController.popBackStack()
                }
            })
        }
        composable(Destinations.Calendar.route) {
            val summary = when (val state = homeState) {
                is HomeUiState.Loaded -> state.summary
                else -> null
            }
            CalendarScreen(
                currentMonth = LocalDate.now(),
                summaries = summary?.calendar ?: emptyList(),
                onDaySelected = { }
            )
        }
        composable(Destinations.Settings.route) {
            SettingsScreen(isDark = isDark, onToggleTheme = { settingsViewModel.toggleTheme() })
        }
    }
}
