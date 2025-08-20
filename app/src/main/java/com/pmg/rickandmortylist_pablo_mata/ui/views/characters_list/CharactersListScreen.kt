package com.pmg.rickandmortylist_pablo_mata.ui.views.characters_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.pmg.rickandmortylist_pablo_mata.R
import com.pmg.rickandmortylist_pablo_mata.domain.model.Character
import com.pmg.rickandmortylist_pablo_mata.domain.model.CharacterLocation
import com.pmg.rickandmortylist_pablo_mata.domain.model.CharacterOrigin
import com.pmg.rickandmortylist_pablo_mata.ui.theme.RickAndMortyList_Pablo_MataTheme
import com.pmg.rickandmortylist_pablo_mata.utils.ui.GradientCapsuleBox

@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchFilterState by viewModel.searchAndFilterState.collectAsStateWithLifecycle()
    val imageLoader = viewModel.imageLoader

    CharacterListContent(
        uiState = uiState,
        searchFilterState = searchFilterState,
        imageLoader = imageLoader,
        loadMoreCharacters = { viewModel.loadMoreCharacters() },
        onSearchTextChanged = { viewModel.onSearchTextChanged(it) },
        onStatusFilterChanged = { viewModel.onStatusFilterChanged(it) },
    )
}

@Composable
fun CharacterListContent(
    uiState: CharactersListUIState,
    searchFilterState: SearchAndFilterUIState,
    imageLoader: ImageLoader,
    loadMoreCharacters: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onStatusFilterChanged: (String?) -> Unit
) {

    Column (
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Rick and Morty Characters",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        OutlinedTextField(
            value = searchFilterState.searchText,
            onValueChange = onSearchTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            label = { Text("Search Characters") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
            trailingIcon = {
                if (searchFilterState.searchText.isNotEmpty()) {
                    IconButton(onClick = { onSearchTextChanged("") }) {
                        Icon(Icons.Filled.Close, contentDescription = "Clear Search")
                    }
                }
            },
            singleLine = true,
            maxLines = 1,
            placeholder = { Text("Rick...") },
            shape = MaterialTheme.shapes.medium,

        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val statusOptions = listOf("Alive", "Dead", "Unknown")
            statusOptions.forEach { statusOption ->
                FilterChip(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    selected = searchFilterState.selectedStatus == statusOption,
                    onClick = {
                        onStatusFilterChanged(statusOption)                    },
                    label = { Text(statusOption) },
                    leadingIcon = if (searchFilterState.selectedStatus == statusOption) {
                        { Icon(Icons.Filled.Done, contentDescription = "Selected") }
                    } else null
                )
            }
        }

        when (uiState) {
            is CharactersListUIState.Loading -> {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            is CharactersListUIState.Success -> {
                val lazyListState = rememberLazyListState()

                val shouldLoadMore by remember {
                    derivedStateOf {
                        val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
                            ?: return@derivedStateOf false

                        lastVisibleItem.index >= lazyListState.layoutInfo.totalItemsCount - 1 - 5
                    }
                }

                LaunchedEffect(shouldLoadMore) {
                    if (shouldLoadMore && uiState.canLoadMore && !uiState.isLoadingMore) {
                        loadMoreCharacters()
                    }
                }

                LazyColumn(
                    modifier = Modifier,
                    state = lazyListState,
                    content = {
                        items(uiState.items.size) { index ->
                            val character = uiState.items[index]
                            CharacterItem(
                                character = character,
                                imageLoader = imageLoader
                            )
                        }

                        if (uiState.isLoadingMore) {
                            item {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                )
            }
            is CharactersListUIState.Empty -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No characters found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            is CharactersListUIState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error loading characters: ${uiState.message}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    modifier: Modifier = Modifier,
    character: Character,
    imageLoader: ImageLoader,
) {
    var imageState by remember { mutableStateOf<ImageLoadState>(ImageLoadState.Loading) }

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(MaterialTheme.shapes.medium)
            ) {
                AsyncImage(
                    model = character.imageUrl,
                    imageLoader = imageLoader,
                    contentDescription = character.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    onState = { state ->
                        imageState = when (state) {
                            is AsyncImagePainter.State.Loading -> ImageLoadState.Loading
                            is AsyncImagePainter.State.Error -> ImageLoadState.Error
                            is AsyncImagePainter.State.Success -> ImageLoadState.Success
                            else -> ImageLoadState.Empty
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (imageState) {
                        is ImageLoadState.Loading -> {
                            CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(32.dp))
                        }
                        is ImageLoadState.Empty -> {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = "No image available",
                                modifier = Modifier.size(32.dp),
                                tint = Color.Gray
                            )
                        }
                        is ImageLoadState.Error -> {
                            Image(
                                painter = painterResource(id = R.drawable.ic_error),
                                contentDescription = "Error loading image",
                                modifier = Modifier.size(32.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error)
                            )
                        }
                        ImageLoadState.Success -> {}
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusTag(status = character.status)
                }
                Text(text = "Species: ${character.species}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Origin: ${character.origin.name}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun StatusTag(status: String) {
    val statusColor = when (status.lowercase()) {
        "alive" -> Color(0xFF4CAF50)
        "dead" -> Color(0xFFF44336)
        "unknown" -> Color(0xFF9E9E9E)
        else -> Color.Gray
    }

    val gradientColors = listOf(
        statusColor.copy(alpha = 0.7f),
        statusColor,
        statusColor.copy(alpha = 0.7f)
    )

    GradientCapsuleBox(
        text = status.uppercase(),
        textColor = statusColor,
        gradientColors = gradientColors,
        modifier = Modifier.width(72.dp)
    )
}

sealed class ImageLoadState {
    data object Loading : ImageLoadState()
    data object Success : ImageLoadState()
    data object Empty : ImageLoadState()
    data object Error : ImageLoadState()
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFFFFFFFF,
    device = Devices.PIXEL_9_PRO
)
@Composable
fun GreetingPreview() {
    RickAndMortyList_Pablo_MataTheme {

        val uiStateHome = CharactersListUIState.Success(
            items = listOf(
                Character(
                    id = 1,
                    name = "Morty Smith",
                    status = "Alive",
                    species = "Human",
                    imageUrl = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                    origin = CharacterOrigin(
                        name = "Earth (C-137)",
                        url = "https://rickandmortyapi.com/api/location/1"
                    ),
                    location = CharacterLocation(
                        name = "Earth (Replacement Dimension)",
                        url = "https://rickandmortyapi.com/api/location/20"
                    ),
                    type = "",
                    url = "",
                    gender = "Male",
                    episode = listOf("https://rickandmortyapi.com/api/episode/1"),
                    created = "2017-11-04T18:48:46.250Z"
                )
            ),
            isLoadingMore = false,
            canLoadMore = true
        )

        CharacterListContent(
            uiState = uiStateHome,
            imageLoader = ImageLoader.Builder(LocalContext.current).build(),
            loadMoreCharacters = { },
            searchFilterState = SearchAndFilterUIState(),
            onSearchTextChanged = {},
            onStatusFilterChanged = {}

        )
    }
}