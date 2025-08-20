package com.pmg.rickandmortylist_pablo_mata.ui.views.characters_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.pmg.rickandmortylist_pablo_mata.domain.usecase.GetCharactersUseCase
import com.pmg.rickandmortylist_pablo_mata.ui.views.characters_list.SearchAndFilterUIState
import com.pmg.rickandmortylist_pablo_mata.domain.usecase.SearchCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val searchCharactersUseCase: SearchCharactersUseCase,
    val imageLoader: ImageLoader
) : ViewModel() {

    private val _uiState = MutableStateFlow<CharactersListUIState>(CharactersListUIState.Loading)
    val uiState: StateFlow<CharactersListUIState> = _uiState

    private val _searchAndFilterState = MutableStateFlow(SearchAndFilterUIState())
    val searchAndFilterState: StateFlow<SearchAndFilterUIState> = _searchAndFilterState

    private var currentPage = 1
    private var isLoading = false
    private var endReached = false

    init {
        viewModelScope.launch {
            _searchAndFilterState.collect { state ->
                currentPage = 1
                endReached = false
                loadCharacters()
            }
        }
    }

    fun onSearchTextChanged(text: String) {
        _searchAndFilterState.value = _searchAndFilterState.value.copy(searchText = text)
    }

    fun onStatusFilterChanged(status: String?) {
        val newStatus = if (_searchAndFilterState.value.selectedStatus == status) null else status
        _searchAndFilterState.value = _searchAndFilterState.value.copy(selectedStatus = newStatus)
    }

    fun loadMoreCharacters() {
        if (!isLoading && !endReached) {
            loadCharacters(isLoadMore = true)
        }
    }

    private fun loadCharacters(isLoadMore: Boolean = false) {

        if (_uiState.value is CharactersListUIState.Success && (_uiState.value as CharactersListUIState.Success).isLoadingMore) return
        if (!isLoadMore) {
            _uiState.value = CharactersListUIState.Loading
        } else {
            val currentState = _uiState.value as? CharactersListUIState.Success
            _uiState.value = currentState?.copy(isLoadingMore = true) ?: return
        }

        viewModelScope.launch {
            val currentState = _searchAndFilterState.value
            val result = when {
                currentState.searchText.isNotBlank() || currentState.selectedStatus != null -> {
                    searchCharactersUseCase(
                        name = currentState.searchText.takeIf { it.isNotBlank() },
                        status = currentState.selectedStatus,
                        page = currentPage
                    )
                }
                else -> {
                    getCharactersUseCase(page = currentPage)
                }
            }

            result.onSuccess { newCharacters ->
                endReached = newCharacters.isEmpty()
                val currentItems = if (isLoadMore) {
                    (_uiState.value as? CharactersListUIState.Success)?.items ?: emptyList()
                } else {
                    emptyList()
                }

                val allItems = currentItems + newCharacters

                if (allItems.isEmpty()) {
                    _uiState.value = CharactersListUIState.Empty
                } else {
                    _uiState.value = CharactersListUIState.Success(
                        items = allItems,
                        isLoadingMore = false,
                        canLoadMore = !endReached
                    )
                    if (!endReached) currentPage++
                }
            }.onFailure { error ->
                isLoading = false

                val isEndOfListError = error is HttpException && error.code() == 404

                if (isEndOfListError) {
                    endReached = true
                    val currentSuccessState = _uiState.value as? CharactersListUIState.Success
                    currentSuccessState?.let {
                        _uiState.value = it.copy(
                            isLoadingMore = false,
                            canLoadMore = false
                        )
                    }
                } else {
                    if (isLoadMore && _uiState.value is CharactersListUIState.Success) {
                        val currentSuccessState = _uiState.value as CharactersListUIState.Success
                        _uiState.value = currentSuccessState.copy(isLoadingMore = false)
                    } else {
                        _uiState.value = CharactersListUIState.Error(
                            error.localizedMessage ?: "An unexpected error occurred."
                        )
                    }
                }
            }
        }
    }
}