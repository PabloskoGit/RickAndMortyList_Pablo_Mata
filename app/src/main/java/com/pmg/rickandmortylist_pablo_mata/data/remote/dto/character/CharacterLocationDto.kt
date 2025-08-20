package com.pmg.rickandmortylist_pablo_mata.data.remote.dto.character

import kotlinx.serialization.Serializable

@Serializable
data class CharacterLocationDto(
    val name: String,
    val url: String
)
