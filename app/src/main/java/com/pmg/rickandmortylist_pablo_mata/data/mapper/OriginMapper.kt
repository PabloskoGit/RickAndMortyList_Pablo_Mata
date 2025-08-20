package com.pmg.rickandmortylist_pablo_mata.data.mapper

import com.pmg.rickandmortylist_pablo_mata.data.remote.dto.CharacterOriginDto
import com.pmg.rickandmortylist_pablo_mata.domain.model.CharacterOrigin

object OriginMapper {
    fun toDomainModel(dto: CharacterOriginDto): CharacterOrigin {
        return CharacterOrigin(
            name = dto.name,
            url = dto.url
        )
    }
}