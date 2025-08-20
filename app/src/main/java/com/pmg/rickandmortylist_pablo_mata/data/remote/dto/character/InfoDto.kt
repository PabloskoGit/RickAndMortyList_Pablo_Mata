package com.pmg.rickandmortylist_pablo_mata.data.remote.dto.character

import kotlinx.serialization.Serializable

@Serializable
data class InfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)