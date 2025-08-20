package com.pmg.rickandmortylist_pablo_mata.data.repository

import com.pmg.rickandmortylist_pablo_mata.data.mapper.toMapDomainModel
import com.pmg.rickandmortylist_pablo_mata.data.remote.api.RickAndMortyApi
import com.pmg.rickandmortylist_pablo_mata.domain.model.Character
import com.pmg.rickandmortylist_pablo_mata.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi
) : CharacterRepository {

    override suspend fun getCharacters(page: Int): Result<List<Character>> {
        return try {
            val response = api.getCharacters(page = page)
            val domainCharacters = response.results.map { it.toMapDomainModel() }
            Result.success(domainCharacters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchCharacters(
        name: String?,
        status: String?,
        page: Int
    ): Result<List<Character>> {
        return try {
            val response = api.searchCharacters(name = name, status = status, page = page)
            val domainCharacters = response.results.map { it.toMapDomainModel() }
            Result.success(domainCharacters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}