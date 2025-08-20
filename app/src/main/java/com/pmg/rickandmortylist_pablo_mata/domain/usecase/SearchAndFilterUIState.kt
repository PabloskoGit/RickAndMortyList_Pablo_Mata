package com.pmg.prueba_pablo_mata.domain.usecase


data class SearchAndFilterUIState(
    val searchText: String = "",
    val selectedStatus: String? = null
)