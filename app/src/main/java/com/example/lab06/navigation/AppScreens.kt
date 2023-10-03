package com.example.lab06.navigation

sealed class AppScreens (val route: String){
    object MainActivity: AppScreens("main_screen")
    object showCityScreen: AppScreens("showImg_screen")
}
