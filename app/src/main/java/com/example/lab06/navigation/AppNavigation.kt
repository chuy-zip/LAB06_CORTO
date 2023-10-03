package com.example.lab06.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab06.MainActivity
import com.example.lab06.showCityScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.MainActivity.route){
        composable(route = AppScreens.MainActivity.route){
            MainActivity(navController)
        }

        composable(route = AppScreens.showCityScreen.route + "/{url}",
            arguments = listOf(
                navArgument( name = "url"){ type = NavType.StringType}
            )
        ){
            showCityScreen(navController, it.arguments?.getString("url"))
        }
    }
}