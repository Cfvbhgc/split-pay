package com.splitpay.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.splitpay.app.presentation.navigation.NavGraph
import com.splitpay.app.presentation.theme.SplitPayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplitPayTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
