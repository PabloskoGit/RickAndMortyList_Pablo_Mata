package com.pmg.rickandmortylist_pablo_mata.data.mapper

import com.pmg.rickandmortylist_pablo_mata.data.remote.dto.character.CharacterLocationDto
import com.pmg.rickandmortylist_pablo_mata.domain.model.CharacterLocation

object LocationMapper {
    fun toDomainModel(dto: CharacterLocationDto): CharacterLocation {
        return CharacterLocation(
            name = dto.name,
            url = dto.url
        )
    }
}