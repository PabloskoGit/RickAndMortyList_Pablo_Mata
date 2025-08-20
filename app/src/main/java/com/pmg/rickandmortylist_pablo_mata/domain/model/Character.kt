package com.pmg.prueba_pablo_mata.domain.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val imageUrl: String,
    val origin: String
)