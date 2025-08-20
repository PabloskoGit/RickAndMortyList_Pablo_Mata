package com.pmg.rickandmortylist_pablo_mata.domain.usecase

import com.pmg.rickandmortylist_pablo_mata.domain.model.Character
import com.pmg.rickandmortylist_pablo_mata.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(page: Int): Result<List<Character>> {
        return repository.getCharacters(page)
    }
}