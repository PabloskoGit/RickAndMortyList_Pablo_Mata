package com.pmg.rickandmortylist_pablo_mata.utils.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.pmg.rickandmortylist_pablo_mata.R
import com.pmg.rickandmortylist_pablo_mata.domain.model.Character
import com.pmg.rickandmortylist_pablo_mata.domain.model.CharacterLocation
import com.pmg.rickandmortylist_pablo_mata.domain.model.CharacterOrigin
import com.pmg.rickandmortylist_pablo_mata.ui.theme.RickAndMortyList_Pablo_MataTheme
import com.pmg.rickandmortylist_pablo_mata.ui.views.characters_list.ImageLoadState
import com.pmg.rickandmortylist_pablo_mata.ui.views.characters_list.StatusTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSheet(
    modifier: Modifier = Modifier,
    character: Character,
    onDismiss: () -> Unit,
    imageLoader: ImageLoader
) {
    var imageState by remember { mutableStateOf<ImageLoadState>(ImageLoadState.Loading) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
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
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (imageState) {
                        is ImageLoadState.Loading -> {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(32.dp)
                            )
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f),
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = character.name, style = MaterialTheme.typography.headlineMedium)
                        StatusTag(status = character.status)
                    }
                    Text(
                        text = "Gender: ${character.gender}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Species: ${character.species}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Type: ${character.type}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Origin: ${character.origin.name}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Location: ${character.location.name}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
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
        DetailSheet(
            character = Character(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "Scientist",
                gender = "Male",
                imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                origin = CharacterOrigin(name = "Earth", url = "https://rickandmortyapi.com/api/location/1"),
                location = CharacterLocation(name = "Citadel of Rick", url = "https://rickandmortyapi.com/api/location/2"),
                episode = listOf("https://rickandmortyapi.com/api/episode/1", "https://rickandmortyapi.com/api/episode/2"),
                url = "https://rickandmortyapi.com/api/character/1",
                created = "2017-11-04T18:48:46.250Z"
            ),
            onDismiss = {},
            imageLoader = ImageLoader(LocalContext.current)
        )

    }
}