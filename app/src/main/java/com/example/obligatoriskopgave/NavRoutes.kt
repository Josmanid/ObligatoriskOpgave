package com.example.obligatoriskopgave
//Purpose:
//This gives you type-safe route names instead of using raw strings everywhere.
sealed class NavRoutes(val route: String){
    data object ListScreen : NavRoutes("listScreen")
    data object AddScreen : NavRoutes("addScreen")
    data object DetailScreen : NavRoutes("detailScreen")
}