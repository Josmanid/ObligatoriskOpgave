package com.example.obligatoriskopgave.repository

import android.R
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.obligatoriskopgave.models.Shopping
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShoppingRepository {
    private val baseUrl = "https://anbo-salesitems.azurewebsites.net/api/"

    private val shoppingService: ShoppingService

    private val _shoppingList = mutableStateOf<List<Shopping>>(emptyList())
    private val _isLoadingItems = mutableStateOf(false)
    private val _errorMessage = mutableStateOf("")


    val shoppingList: State<List<Shopping>> get() = _shoppingList
    val isLoadingItems: State<Boolean> get() = _isLoadingItems
    val errorMessage: State<String> get() = _errorMessage

    init {
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        shoppingService = build.create(ShoppingService::class.java)
        getAllItems()
    }

    fun getAllItems() {
        _isLoadingItems.value = true
        shoppingService.getAllItems().enqueue(object : Callback<List<Shopping>> {
            override fun onResponse(
                call: Call<List<Shopping>>,
                response: Response<List<Shopping>>
            ) {
                _isLoadingItems.value = false
                if (response.isSuccessful) {
                    val shoppingListVar: List<Shopping>? = response.body()
                    _shoppingList.value = shoppingListVar ?: emptyList()
                    _errorMessage.value = ""
                } else {
                    _errorMessage.value = "${response.code()} ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<Shopping>>, t: Throwable) {
                _isLoadingItems.value = false
                _errorMessage.value = t.message ?: "No connection to back-end"
            }
        })
    }

    fun add(shopping: Shopping){
        shoppingService.createShoppingItem(shopping).enqueue(object : Callback<Shopping>{
            override fun onResponse(call: Call<Shopping>, response: Response<Shopping?>) {
                if(response.isSuccessful)
                {
                    Log.d("Apple","added: "+ response.body())
                    getAllItems()
                    _errorMessage.value = ""
                } else{
                    val message = response.code().toString() + " " + response.message()
                    _errorMessage.value = message
                    Log.e("APPLE", message)
                }

            }
            override fun onFailure(call: Call<Shopping?>, t: Throwable) {
                val message = t.message ?: "No connection to back-end"
                _errorMessage.value = message
                Log.e("Apple", message)
            }
        })
    }
    fun delete(id: Int) {
        Log.d("APPLE", "Delete: $id")
        shoppingService.deleteShoppingItem(id).enqueue(object : Callback<Shopping> {
            override fun onResponse(call: Call<Shopping>, response: Response<Shopping>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Delete: " + response.body())
                    _errorMessage.value = ""
                    getAllItems()
                } else {
                    val message = response.code().toString() + " " + response.message()
                    _errorMessage.value = message
                    Log.e("APPLE", "Not deleted: $message")
                }
            }

            override fun onFailure(call: Call<Shopping>, t: Throwable) {
                val message = t.message ?: "No connection to back-end"
                _errorMessage.value = message
                Log.e("APPLE", "Not deleted $message")
            }
        })
    }
    fun sortByItemTitle(ascending: Boolean) {
        val sortedList = if (ascending) {
            _shoppingList.value.sortedBy { it.description }
        } else {
            _shoppingList.value.sortedByDescending { it.description }
        }
        _shoppingList.value = sortedList
    }

    fun sortByItemPrice(ascending: Boolean) {
        _shoppingList.value = if (ascending) {
            _shoppingList.value.sortedBy { it.price }
        } else {
            _shoppingList.value.sortedByDescending { it.price }
        }
    }

    fun filterByTitle(titleFragment: String) {
        if (titleFragment.isEmpty()) {
            getAllItems()
            return
        } else {
            _shoppingList.value =
                _shoppingList.value.filter {
                    it.description.contains(titleFragment, ignoreCase = true)
                }
        }
    }
    fun filterItems(query: String, byPrice: Boolean) {
        if (query.isEmpty()) {
            getAllItems()
            return
        }

        if (byPrice) {
            val price = query.toDoubleOrNull()
            if (price != null) {
                _shoppingList.value = _shoppingList.value.filter { it.price <= price }
            } else {
                getAllItems()
            }
        } else {
            filterByTitle(query)
        }
    }

}
