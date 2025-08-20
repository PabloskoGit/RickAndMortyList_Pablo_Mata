package com.pmg.prueba_pablo_mata.domain.repository

import com.pmg.prueba_pablo_mata.domain.model.Character

interface CharacterRepository {
    suspend fun getCharacters(page: Int): Result<List<Character>>

    suspend fun searchCharacters(
        name: String?,
        status: String?,
        page: Int
    ): Result<List<Character>>
}