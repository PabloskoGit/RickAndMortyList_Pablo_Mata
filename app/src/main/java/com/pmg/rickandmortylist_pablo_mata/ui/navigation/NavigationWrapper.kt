package com.pmg.rickandmortylist_pablo_mata.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pmg.rickandmortylist_pablo_mata.ui.views.characters_list.CharacterListScreen
import com.pmg.rickandmortylist_pablo_mata.ui.views.characters_list.CharacterListViewModel

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    val characterListViewModel: CharacterListViewModel = hiltViewModel()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Home> {
                CharacterListScreen(
                    viewModel = characterListViewModel
                )
            }
        }
    }
}