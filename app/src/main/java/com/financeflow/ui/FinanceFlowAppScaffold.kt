package com.financeflow.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.financeflow.analytics.AnalyticsEngine
import com.financeflow.authentication.AuthState
import com.financeflow.authentication.AuthViewModel
import com.financeflow.transactions.BillEntity
import com.financeflow.transactions.TransactionEntity
import com.financeflow.transactions.TransactionViewModel
import com.financeflow.ui.components.GradientBackground
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object TransactionForm : Screen("transaction_form")
    object BillForm : Screen("bill_form")
    object ThemeSettings : Screen("theme_settings")
}

@Composable
fun FinanceFlowAppScaffold(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    transactionViewModel: TransactionViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val authState = authViewModel.authState
    val transactionState = transactionViewModel.dashboard.collectAsState()
    val editingTransaction = remember { mutableStateOf<TransactionEntity?>(null) }
    val editingBill = remember { mutableStateOf<BillEntity?>(null) }

    LaunchedEffect(authState.value) {
        when (val state = authState.value) {
            is AuthState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(state.message)
                }
            }
            is AuthState.Authenticated -> {
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
            AuthState.Unauthenticated -> {
                navController.navigate(Screen.Onboarding.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
            AuthState.Loading -> Unit
        }
    }

    GradientBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            androidx.compose.material3.Scaffold(
                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Onboarding.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(Screen.Onboarding.route) {
                        OnboardingScreen(
                            onLogin = { navController.navigate(Screen.Login.route) },
                            onRegister = { navController.navigate(Screen.Register.route) }
                        )
                    }
                    composable(Screen.Login.route) {
                        AuthScreen(
                            title = "Sign In",
                            submitLabel = "Login",
                            onSubmit = { email, password -> authViewModel.login(email, password) },
                            onSwitch = { navController.navigate(Screen.Register.route) },
                            isLoading = authState.value is AuthState.Loading
                        )
                    }
                    composable(Screen.Register.route) {
                        AuthScreen(
                            title = "Create Account",
                            submitLabel = "Register",
                            onSubmitWithConfirm = { email, password, confirm ->
                                authViewModel.register(email, password, confirm)
                            },
                            onSwitch = { navController.navigate(Screen.Login.route) },
                            isRegister = true,
                            isLoading = authState.value is AuthState.Loading
                        )
                    }
                    composable(Screen.Dashboard.route) {
                        val summary = transactionState.value
                        val analytics = remember(summary) { AnalyticsEngine.summarize(summary.transactions) }
                        DashboardScreen(
                            summary = summary,
                            analytics = analytics,
                            onAddTransaction = {
                                editingTransaction.value = null
                                navController.navigate(Screen.TransactionForm.route)
                            },
                            onEditTransaction = { transaction ->
                                editingTransaction.value = transaction
                                navController.navigate(Screen.TransactionForm.route)
                            },
                            onDeleteTransaction = { transaction ->
                                transactionViewModel.deleteTransaction(transaction)
                            },
                            onOpenTheme = { navController.navigate(Screen.ThemeSettings.route) },
                            onToggleBill = { bill, paid -> transactionViewModel.toggleBillPaid(bill, paid) },
                            onAddBill = {
                                editingBill.value = null
                                navController.navigate(Screen.BillForm.route)
                            },
                            onEditBill = { bill ->
                                editingBill.value = bill
                                navController.navigate(Screen.BillForm.route)
                            },
                            onDeleteBill = { bill -> transactionViewModel.deleteBill(bill) }
                        )
                    }
                    composable(Screen.TransactionForm.route) {
                        TransactionFormScreen(
                            initial = editingTransaction.value,
                            onSubmit = { id, title, amount, type, category, occurredAt ->
                                transactionViewModel.saveTransaction(
                                    id = id,
                                    title = title,
                                    amount = amount,
                                    type = type,
                                    category = category,
                                    occurredAt = occurredAt
                                )
                                editingTransaction.value = null
                                navController.popBackStack()
                            },
                            onCancel = {
                                editingTransaction.value = null
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(Screen.BillForm.route) {
                        BillFormScreen(
                            initial = editingBill.value,
                            onSubmit = { id, name, amount, dueDate, paid ->
                                transactionViewModel.saveBill(id, name, amount, dueDate, paid)
                                editingBill.value = null
                                navController.popBackStack()
                            },
                            onCancel = {
                                editingBill.value = null
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(Screen.ThemeSettings.route) {
                        ThemeSettingsScreen(
                            selectedTheme = com.financeflow.ui.theme.LocalGradientTheme.current.name,
                            onSelect = { theme ->
                                authViewModel.selectTheme(theme.name)
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
