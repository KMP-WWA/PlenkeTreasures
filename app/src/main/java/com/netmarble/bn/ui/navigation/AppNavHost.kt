package com.netmarble.bn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.netmarble.bn.ui.start.StartRoute
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.netmarble.bn.ui.game.BoardRoute
import com.netmarble.bn.ui.shop.ShopRoute
import com.netmarble.bn.R

@Composable
fun AppNavHost() {
    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = context.getString(R.string.route_start)) {
        composable(context.getString(R.string.route_start)) {
            StartRoute(
                onPlay = { islandId ->
                    navController.navigate(context.getString(R.string.route_board_with_param, islandId))
                },
                onShop = { navController.navigate(context.getString(R.string.route_shop)) }
            )
        }
        composable(context.getString(R.string.route_shop)) {
            ShopRoute(onBack = { navController.popBackStack() })
        }
        composable(
            route = context.getString(R.string.route_board),
            arguments = listOf(
                navArgument(context.getString(R.string.nav_argument_island)) {
                    type = NavType.StringType
                    defaultValue = context.getString(R.string.default_island_id)
                }
            )
        ) { backStackEntry ->
            val islandId = backStackEntry.arguments?.getString(context.getString(R.string.nav_argument_island))
                ?: context.getString(R.string.default_island_id)
            BoardRoute(islandId = islandId)
        }
    }
}