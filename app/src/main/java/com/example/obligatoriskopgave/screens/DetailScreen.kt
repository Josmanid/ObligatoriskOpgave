package com.example.obligatoriskopgave.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.navigation.NavController
import com.example.obligatoriskopgave.MainActivity
import com.example.obligatoriskopgave.NavRoutes
import com.example.obligatoriskopgave.models.Shopping
import com.example.obligatoriskopgave.models.ShoppingViewModelState
import kotlin.reflect.full.memberProperties


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    item: String,
    onItemDeleted: (Shopping) -> Unit,
    userEmail: String? = "",
    navController: NavController,
    viewModel: ShoppingViewModelState
) {
    val shoppingItem = viewModel.shoppingListvar.value.find { it.id == item.toInt() }

    if (shoppingItem != null) {
        val fields = Shopping::class.memberProperties.map { prop ->
            prop.name to prop.get(shoppingItem)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Details for ${shoppingItem.description}") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(fields) { (label, value) ->
                    Text(text = "$label: $value", modifier = Modifier.padding(vertical = 4.dp))
                }
            }
                if (shoppingItem.sellerEmail == userEmail) {
                    IconButton(onClick = {
                        onItemDeleted(shoppingItem)
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Remove ${shoppingItem.description}"
                        )
                    }
                }
        }
        }
    }
}
