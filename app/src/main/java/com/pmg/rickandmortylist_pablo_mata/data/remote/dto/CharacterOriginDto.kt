package com.pmg.rickandmortylist_pablo_mata.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterOriginDto(
    val name: String,
    val url: String
)
