package com.pmg.prueba_pablo_mata.domain.usecase

import com.pmg.prueba_pablo_mata.domain.model.Character
import com.pmg.prueba_pablo_mata.domain.repository.CharacterRepository
import javax.inject.Inject

class SearchCharactersUseCase @Inject constructor(private val repository: CharacterRepository) {
    suspend operator fun invoke(
        name: String?,
        status: String?,
        page: Int
    ): Result<List<Character>> {
        return repository.searchCharacters(
            name = name,
            status = status,
            page = page
        )
    }
}