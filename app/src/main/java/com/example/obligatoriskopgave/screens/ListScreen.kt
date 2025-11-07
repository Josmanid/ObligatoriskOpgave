package com.example.obligatoriskopgave.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.obligatoriskopgave.models.Shopping
import com.example.obligatoriskopgave.NavRoutes
import com.example.obligatoriskopgave.models.AuthViewModel
import com.example.obligatoriskopgave.models.ShoppingViewModelState
import com.example.obligatoriskopgave.ui.theme.ObligatoriskOpgaveTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    shoppingListvar: List<Shopping>,
    errorMessage: String = "",
    userEmail: String? = "",
    authViewModel: AuthViewModel? = null,
    onShoppingSelected: (Shopping) -> Unit = {},
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onSortByItemTitle: (Boolean) -> Unit = {},
    onSortByItemPrice: (Boolean) -> Unit = {},
    onFilter: (query: String, byPrice: Boolean) -> Unit = { _, _ -> },
    onAddClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Shopping Store",
                            style = MaterialTheme.typography.headlineLarge,
                        )
                        if (userEmail != null) {
                            Text(
                                text = "Logged in as $userEmail",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                actions = {
                    if (userEmail == null) {
                        Button(onClick = { onLoginClick() }) {
                            Text("Login")
                        }
                    } else{
                        Button(onClick = {onLogoutClick()}) {
                            Text("Logout")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (userEmail != null){
                androidx.compose.material3.FloatingActionButton(
                    onClick = {onAddClick()}
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Item"
                    )
                }
            }
        }
    ) { innerPadding ->

        ShoppingPanel(
            onShoppingSelected = onShoppingSelected, //pass down
            shoppingListvar = shoppingListvar,
            errorMessage = errorMessage,
            sortByItemTitle = onSortByItemTitle,
            sortByItemPrice = onSortByItemPrice,
            onFilter = onFilter,
            modifier = Modifier.padding(innerPadding)
        )

    }
}

@Composable
fun ShoppingPanel(
    onShoppingSelected: (Shopping) -> Unit = {},
    shoppingListvar: List<Shopping>,
    sortByItemTitle: (up: Boolean) -> Unit = {},
    sortByItemPrice: (up: Boolean) -> Unit = {},
    onFilter: (String, Boolean) -> Unit = { _, _ -> },
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {

        LazyList(
            items = shoppingListvar,
            onItemClick = { selectedItem ->
                // pass the actual selected item
                onShoppingSelected(selectedItem)
            },
            sortByItemTitle = sortByItemTitle,
            sortByItemPrice = sortByItemPrice,
            onFilter = onFilter,
            modifier = Modifier
        )
    }
}

@Composable
fun LazyList(
    items: List<Shopping>,
    onItemClick: (Shopping) -> Unit,
    sortByItemTitle: (up: Boolean) -> Unit,
    sortByItemPrice: (Boolean) -> Unit,
    onFilter: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val titleUp = "Title \u2191"
    val titleDown = "Title \u2193"
    var sortTitleAscending by remember { mutableStateOf(true) }
    val priceUp = "Price \u2191"
    val priceDown = "Price \u2193"
    var sortByItemPriceAscending by remember { mutableStateOf(value = true) }
    var filterText by remember { mutableStateOf("") }
    var filterByPrice by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {

        Button(
            onClick = {
                sortByItemTitle(sortTitleAscending)
                sortTitleAscending = !sortTitleAscending
            })
        {
            Text(text = if (sortTitleAscending) titleDown else titleUp)
        }
        Button(onClick = {
            sortByItemPrice(sortByItemPriceAscending)
            sortByItemPriceAscending = !sortByItemPriceAscending
        })
        { Text(text = if (sortByItemPriceAscending) priceDown else priceUp) }
        OutlinedTextField(
            value = filterText,
            onValueChange = { filterText = it },
            label = { Text(if (filterByPrice) "Filter by Price" else "Filter by Text") }
        )

    }
    Row {   Button(onClick = { filterByPrice = !filterByPrice },
        modifier = Modifier.testTag("modeButton")) {
        Text(if (filterByPrice) "Mode: Price" else "Mode: Title")
    }
        Button(
            onClick = { onFilter(filterText, filterByPrice) },
        ) { Text("Apply Filter") }
    }

    LazyColumn(modifier = modifier) {
        items(items, key = { it.id }) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onItemClick(item) } // send full item
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ID: ${item.id}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Description: ${item.description}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(text = "Price: ${item.price}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginDialog(
    authViewModel: AuthViewModel,
    onDismiss: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val currentUser = authViewModel.user.value
    var passwordToHide by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Login / Sign Up",
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                if (currentUser != null) {
                    Button(
                        onClick = { authViewModel.signOut() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text("Sign Out")
                    }
                }
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email") }
                )
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordToHide) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordToHide = !passwordToHide }) {
                            if (passwordToHide)
                                Icon(
                                    imageVector = Icons.Filled.Visibility,
                                    contentDescription = "Hide Password"
                                )
                            else Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "Show Password"
                            )
                        }
                    }
                )
                if (authViewModel.errorMessage.value.isNotEmpty()) {
                    Text(
                        text = authViewModel.errorMessage.value,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                if (currentUser != null) {
                    Text("Logged in as ${currentUser.email}")
                } else Text("Logged Out")
            }
        },
        confirmButton = {
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { authViewModel.signIn(email.value, password.value) }) {
                        Text("Sign In")
                    }
                    Button(onClick = { authViewModel.signUp(email.value, password.value) }) {
                        Text("Sign Up")
                    }
                }
                Spacer(Modifier.height(8.dp))

            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ShoppingPreview() {
    ListScreen(
        shoppingListvar = listOf(
            Shopping(1, "Aomic book", 200, "test@email.kd", "123232", 0, ""),
            Shopping(2, "Comic book2", 20, "test@email1.kd", "123232", 0, ""),
            Shopping(3, "Bomic book3", 800, "test@email2.kd", "1", 0, "")
        ),
        errorMessage = "Error Test",
        userEmail = "preview@test.com"

    )
}


