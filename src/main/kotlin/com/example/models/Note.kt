package com.example.models

import kotlinx.serialization.Serializable

//Con esta data class capturamos el id y la nota del cliente para su uso respectivo
@Serializable
data class Note(
    val id: Int,
    val note: String
)
