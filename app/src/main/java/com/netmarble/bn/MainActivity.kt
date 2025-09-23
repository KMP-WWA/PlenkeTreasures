package com.netmarble.bn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.netmarble.bn.ui.navigation.AppNavHost
import com.netmarble.bn.ui.theme.PlinkoTreasureTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlinkoTreasureTheme {
                AppNavHost()
            }
        }
    }
}