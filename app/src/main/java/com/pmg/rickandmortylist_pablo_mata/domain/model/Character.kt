package com.pmg.rickandmortylist_pablo_mata.domain.model

import com.pmg.rickandmortylist_pablo_mata.data.remote.dto.CharacterLocationDto
import com.pmg.rickandmortylist_pablo_mata.data.remote.dto.CharacterOriginDto

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: CharacterOrigin,
    val location: CharacterLocation,
    val imageUrl: String,
    val episode: List<String>,
    val url: String,
    val created: String
)