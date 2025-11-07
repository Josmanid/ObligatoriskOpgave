package com.example.obligatoriskopgave

sealed class NavRoutes(val route: String){
    data object ListScreen : NavRoutes("listScreen")
    data object AddScreen : NavRoutes("addScreen")
    data object DetailScreen : NavRoutes("detailScreen")
}