package com.pmg.prueba_pablo_mata.domain.usecase

import com.pmg.prueba_pablo_mata.domain.model.Character
import com.pmg.prueba_pablo_mata.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(page: Int): Result<List<Character>> {
        return repository.getCharacters(page)
    }
}