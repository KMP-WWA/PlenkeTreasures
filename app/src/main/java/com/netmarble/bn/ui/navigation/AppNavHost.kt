package com.netmarble.bn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.netmarble.bn.ui.start.StartRoute
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.netmarble.bn.ui.game.BoardRoute
import com.netmarble.bn.ui.shop.ShopRoute

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartRoute(
                onPlay = { islandId -> navController.navigate("board?island=$islandId") },
                onShop = { navController.navigate("shop") }
            )
        }
        composable("shop") {
            ShopRoute(onBack = { navController.popBackStack() })
        }
        composable(
            route = "board?island={island}",
            arguments = listOf(
                navArgument("island") { type = NavType.StringType; defaultValue = "ship_deck" }
            )
        ) { backStackEntry ->
            val islandId = backStackEntry.arguments?.getString("island") ?: "ship_deck"
            BoardRoute(islandId = islandId)
        }
    }
}