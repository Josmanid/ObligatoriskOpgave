package com.example.obligatoriskopgave.models
data class Shopping(
    val id: Int,
    val description: String,
    val price: Int,
    val sellerEmail: String,
    val sellerPhone: String,
    val time: Int,
    val pictureUrl: String
) {

    override fun toString(): String {
        return "$id, $description, $price, $sellerEmail, $sellerPhone ect.."
    }
}
