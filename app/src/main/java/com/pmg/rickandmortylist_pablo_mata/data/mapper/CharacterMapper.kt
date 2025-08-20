package com.pmg.rickandmortylist_pablo_mata.data.mapper

import com.pmg.rickandmortylist_pablo_mata.data.remote.dto.CharacterDto
import com.pmg.rickandmortylist_pablo_mata.domain.model.Character

fun CharacterDto.toMapDomainModel(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        imageUrl = image,
        origin = origin.name
    )
}