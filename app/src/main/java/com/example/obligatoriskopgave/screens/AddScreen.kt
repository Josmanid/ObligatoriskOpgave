package com.example.obligatoriskopgave.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.obligatoriskopgave.NavRoutes
import com.example.obligatoriskopgave.models.AuthViewModel
import com.example.obligatoriskopgave.models.Shopping
import com.example.obligatoriskopgave.models.ShoppingViewModelState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    addItem: (Shopping) -> Unit,
    onItemDeleted: (Shopping) -> Unit,
    navigateBack: () -> Unit,
    userEmail: String = "unknown@example.com",
    shoppingListvar: List<Shopping>,
) {
    var description by remember { mutableStateOf("") }
    var priceStr by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }


    var descError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }


    val myItems = shoppingListvar.filter { it.sellerEmail == userEmail }

    Scaffold(
        topBar =
            {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                "Add Shopping Item",
                                style = MaterialTheme.typography.headlineLarge
                            )
                            if (userEmail.isNotEmpty()) {
                                Text(
                                    text = "Logged in as $userEmail",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                        }

                    }
                )
            }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = description,
                onValueChange = { description = it; descError = false },
                label = { Text("Description") },
                isError = descError,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = priceStr,
                onValueChange = { priceStr = it; priceError = false },
                label = { Text("Price") },
                isError = priceError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Seller phone") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Image URL (optional)") },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(16.dp))


            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = navigateBack) { Text("Back") }

                Button(onClick = {
                    val price = priceStr.toIntOrNull()
                    descError = description.isEmpty()
                    priceError = price == null

                    if (!descError && !priceError) {
                        val newItem = Shopping(
                            id = 0,
                            description = description,
                            price = price!!,
                            sellerEmail = userEmail,
                            sellerPhone = phone.ifEmpty { "N/A" },
                            time = (System.currentTimeMillis() / 1000).toInt(),
                            pictureUrl = imageUrl.ifEmpty { "no_image.png" }
                        )
                        addItem(newItem)

                    }
                }) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            Text("My Items:", style = MaterialTheme.typography.titleMedium)

            if (myItems.isEmpty()) {
                Text("You haven’t added any items yet.")
            } else {
                Column {
                    myItems.forEach { item ->
                        ShoppingItem(
                            shopping = item,
                            onShoppingSelected = {},
                            onShoppingDeleted = onItemDeleted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShoppingItem(
    shopping: Shopping,
    modifier: Modifier = Modifier,
    onShoppingSelected: (Shopping) -> Unit = {},
    onShoppingDeleted: (Shopping) -> Unit = {}
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onShoppingDeleted(shopping)
            }
            true
        }
    )
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Red)
            )
        }

    ) {
        Card(
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth(),
            onClick = { onShoppingSelected(shopping) }
        )
        {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "${shopping.description}: ${shopping.price} kr"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddScreenPreview() {

//    AddScreen(
//        navController = rememberNavController(),
//        viewModel = TODO(),
//        authViewModel = TODO(),
//        shoppingListvar = emptyList(),
//        errorMessage = "",
//        userEmail = ""
    //  )
}
