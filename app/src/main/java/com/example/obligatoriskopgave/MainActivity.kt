package com.example.obligatoriskopgave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.obligatoriskopgave.models.AuthViewModel
import com.example.obligatoriskopgave.models.Shopping
import com.example.obligatoriskopgave.models.ShoppingViewModelState
import com.example.obligatoriskopgave.screens.AddScreen
import com.example.obligatoriskopgave.screens.DetailScreen
import com.example.obligatoriskopgave.screens.ListScreen
import com.example.obligatoriskopgave.screens.LoginDialog
import com.example.obligatoriskopgave.ui.theme.ObligatoriskOpgaveTheme
import androidx.compose.runtime.LaunchedEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ObligatoriskOpgaveTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: ShoppingViewModelState = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val shoppingListvar = viewModel.shoppingListvar.value
    val errorMessage = viewModel.errorMessage.value
    val currentUser = authViewModel.user.value
    if (authViewModel.isDialogShown) {
        LoginDialog(
            authViewModel = authViewModel,
            onDismiss = { authViewModel.onDismissDialog() }
        )
    }
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            navController.navigate(NavRoutes.AddScreen.route) {
                popUpTo(NavRoutes.ListScreen.route)
            }
        } else {
            navController.popBackStack(
                route = NavRoutes.ListScreen.route,
                inclusive = false
            )
        }
    }
    NavHost(
        navController = navController,
        startDestination = NavRoutes.ListScreen.route
    ) {
        composable(NavRoutes.ListScreen.route) {
            ListScreen(
                shoppingListvar = shoppingListvar,
                errorMessage = errorMessage,
                userEmail = currentUser?.email,
                authViewModel = authViewModel,
                onShoppingSelected = { shopping ->
                    navController.navigate(NavRoutes.DetailScreen.route + "/${shopping.id}")
                },
                onLoginClick = { authViewModel.onLogin() },
                onLogoutClick = { authViewModel.signOut() },
                onSortByItemTitle = { ascending -> viewModel.sortByItemTitle(ascending) },
                onSortByItemPrice = { ascending -> viewModel.sortByItemPrice(ascending) },
                onFilter = { query, byPrice -> viewModel.filterItems(query, byPrice) },
                onAddClick = { navController.navigate(NavRoutes.AddScreen.route) }
            )

        }
        composable(NavRoutes.AddScreen.route) {
            AddScreen(
                addItem = { newItem -> viewModel.add(newItem) },
                onItemDeleted = { item -> viewModel.remove(item.id) },
                userEmail = currentUser?.email ?: "unknown@example.com",
                navigateBack = { navController.popBackStack() },
                shoppingListvar = shoppingListvar,
            )
        }

        composable(NavRoutes.DetailScreen.route + "/{item}") { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item") ?: "unknown"

            DetailScreen(
                item = item,
                onItemDeleted = { item -> viewModel.remove(item.id) },
                navController = navController,
                userEmail = currentUser?.email,
                viewModel = viewModel
            )

        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ObligatoriskOpgaveTheme {

    }
}