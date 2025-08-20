package com.pmg.rickandmortylist_pablo_mata.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponseDto(
    val info: InfoDto,
    val results: List<CharacterDto>
)
