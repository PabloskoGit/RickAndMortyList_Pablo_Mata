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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.pmg.rickandmortylist_pablo_mata.R
import com.pmg.rickandmortylist_pablo_mata.domain.model.Character
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
                    .height(350.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(MaterialTheme.shapes.large)
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
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        is ImageLoadState.Empty -> {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = "No image available",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }

                        is ImageLoadState.Error -> {
                            Image(
                                painter = painterResource(id = R.drawable.ic_error),
                                contentDescription = "Error loading image",
                                modifier = Modifier.size(64.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error)
                            )
                        }

                        ImageLoadState.Success -> Unit
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        StatusTag(status = character.status)
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    CharacterInfoRow(
                        icon = painterResource(R.drawable.ic_face),
                        label = "Gender",
                        value = character.gender
                    )
                    CharacterInfoRow(
                        icon = painterResource(R.drawable.ic_biology),
                        label = "Species",
                        value = character.species
                    )
                    CharacterInfoRow(
                        icon = painterResource(R.drawable.ic_star),
                        label = "Type",
                        value = character.type.ifEmpty { "Unknown" }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    CharacterInfoRow(
                        icon = painterResource(R.drawable.ic_globe_world),
                        label = "Origin",
                        value = character.origin.name
                    )
                    CharacterInfoRow(
                        icon = painterResource(R.drawable.ic_location_on),
                        label = "Location",
                        value = character.location.name
                    )
                    CharacterInfoRow(
                        icon = painterResource(R.drawable.ic_movie),
                        label = "Episodes Appeared",
                        value = character.episode.size.toString()
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CharacterInfoRow(
    icon: Painter,
    label: String,
    value: String,
    iconTint: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}