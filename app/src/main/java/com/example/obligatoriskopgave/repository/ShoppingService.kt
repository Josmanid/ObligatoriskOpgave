package com.example.obligatoriskopgave.repository

import androidx.compose.ui.input.pointer.PointerId
import com.example.obligatoriskopgave.models.Shopping

import retrofit2.Call

import retrofit2.http.*

interface ShoppingService {
    @GET("SalesItems")
    fun getAllItems(): Call<List<Shopping>>
    @POST("SalesItems")
    fun createShoppingItem(@Body shopping: Shopping): Call<Shopping>
    @DELETE("SalesItems/{shoppingId}")
    fun deleteShoppingItem(@Path("shoppingId") id: Int): Call<Shopping>
}