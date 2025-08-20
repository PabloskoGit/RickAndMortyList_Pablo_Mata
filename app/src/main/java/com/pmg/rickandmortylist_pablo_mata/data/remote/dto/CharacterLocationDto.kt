package com.pmg.prueba_pablo_mata.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterLocationDto(
    val name: String,
    val url: String
)
