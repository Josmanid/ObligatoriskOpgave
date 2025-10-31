package com.example.obligatoriskopgave.models

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.obligatoriskopgave.repository.ShoppingRepository

//Arver fra ViewModel, så den overlever konfigurationsændringer (rotation, tema-skift osv.).
class ShoppingViewModelState : ViewModel() {
    private val repository = ShoppingRepository()

    val shoppingListvar = repository.shoppingList
    val errorMessage = repository.errorMessage
    val isLoadingBooks = repository.isLoadingItems

    fun getAllItems() = repository.getAllItems()

    fun add(shopping: Shopping) = repository.add(shopping)
    fun remove(id: Int) = repository.delete(id)
    fun sortByItemTitle(ascending: Boolean) {
        repository.sortByItemTitle(ascending)
    }

    fun sortByItemPrice(ascending: Boolean) {
        repository.sortByItemPrice(ascending)
    }

    fun filterItems(query: String, byPrice: Boolean) {
        repository.filterItems(query, byPrice)
    }

}
