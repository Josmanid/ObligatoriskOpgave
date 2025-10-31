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

//Create composable to manage navigation
@Composable
fun MainScreen(
    viewModel: ShoppingViewModelState = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    //Hold controlleren højt i hierarkiet, så den kan deles.
    val navController = rememberNavController()
    // eneste viewmodel så vi deler den på siderne
    val shoppingListvar = viewModel.shoppingListvar.value
    //Det er her du forbinder routes til dine screens
    val errorMessage = viewModel.errorMessage.value
    // en variabel til at gemme den nuværende brugeren( her opdatering sker når nogen logger ind)
    val currentUser = authViewModel.user.value
    // Show login dialog when isDialogShown is true
    if (authViewModel.isDialogShown) {
        LoginDialog(
            authViewModel = authViewModel,
            onDismiss = { authViewModel.onDismissDialog() }
        )
    }
    LaunchedEffect(currentUser) {
        //When logged in navigate to addscreen
        if (currentUser != null){
            navController.navigate(NavRoutes.AddScreen.route){
                popUpTo(NavRoutes.ListScreen.route)
            }
        } else{
            // when logged out navigate back to Listscreen
            navController.popBackStack(route = NavRoutes.ListScreen.route,
                inclusive = false)
        }
    }
    NavHost(
        navController = navController,
        startDestination = NavRoutes.ListScreen.route //the flow starts here
    ) {
        composable(NavRoutes.ListScreen.route) {
            //Navigate to details on a specific item
            //TODO: Login dialog?
            ListScreen(
                shoppingListvar = shoppingListvar,
                errorMessage = errorMessage,
                userEmail = currentUser?.email,
                authViewModel = authViewModel,
                onShoppingSelected = { shopping ->
                    navController.navigate(NavRoutes.DetailScreen.route + "/${shopping.id}")
                },
                onLoginClick = { authViewModel.onLogin() },
                onSortByItemTitle = { ascending -> viewModel.sortByItemTitle(ascending) },
                onSortByItemPrice = { ascending -> viewModel.sortByItemPrice(ascending) },
                onFilter = {query,byPrice -> viewModel.filterItems(query,byPrice)},
                onAddClick = {navController.navigate(NavRoutes.AddScreen.route)}
            ) // here we pass the constructor

        }
        composable(NavRoutes.AddScreen.route) {
            AddScreen(
                addItem = { newItem -> viewModel.add(newItem) },
                onItemDeleted = { item -> viewModel.remove(item.id) },
                userEmail = currentUser?.email ?: "unknown@example.com",
                navigateBack = {navController.popBackStack()},
                shoppingListvar = shoppingListvar,
            )
        }
        //We define a screen route that expects a value {item}. to come to it
        composable(NavRoutes.DetailScreen.route + "/{item}") { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item") ?: "unknown"
            //Show the DetailScreen
            DetailScreen(
                item = item,
                navController = navController,
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