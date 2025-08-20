package com.pmg.prueba_pablo_mata.ui.views.characters_list

import com.pmg.prueba_pablo_mata.domain.model.Character

sealed class CharactersListUIState {
    data object Loading : CharactersListUIState()
    data class Success(
        val items: List<Character>,
        val isLoadingMore: Boolean = false,
        val canLoadMore: Boolean = true
    ) : CharactersListUIState()
    data object Empty : CharactersListUIState()
    data class Error(val message: String) : CharactersListUIState()
}
