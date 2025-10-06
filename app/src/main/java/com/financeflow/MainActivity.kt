package com.financeflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.financeflow.authentication.AuthViewModel
import com.financeflow.transactions.TransactionViewModel
import com.financeflow.ui.FinanceFlowAppScaffold
import com.financeflow.ui.theme.FinanceFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as FinanceFlowApp
        setContent {
            val gradient by app.themeRepository.theme.collectAsState(initial = com.financeflow.ui.theme.FinanceFlowPalettes.Nebula)
            FinanceFlowTheme(gradientTheme = gradient) {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory(app.authRepository))
                val transactionViewModel: TransactionViewModel = viewModel(factory = TransactionViewModel.Factory(app.transactionRepository))
                FinanceFlowAppScaffold(
                    navController = navController,
                    authViewModel = authViewModel,
                    transactionViewModel = transactionViewModel
                )
            }
        }
    }
}
