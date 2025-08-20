package com.pmg.rickandmortylist_pablo_mata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pmg.rickandmortylist_pablo_mata.ui.navigation.NavigationWrapper
import com.pmg.rickandmortylist_pablo_mata.ui.theme.Prueba_Pablo_MataTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Prueba_Pablo_MataTheme {
                NavigationWrapper()
            }
        }
    }
}