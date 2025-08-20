package com.pmg.rickandmortylist_pablo_mata.data.remote.api

import com.pmg.rickandmortylist_pablo_mata.data.remote.dto.CharacterResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApi {

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharacterResponseDto

    @GET("character")
    suspend fun searchCharacters(
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("page") page: Int
    ): CharacterResponseDto
}