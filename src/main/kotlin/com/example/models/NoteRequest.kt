package com.example.models

import kotlinx.serialization.Serializable

//Esta data class es para recuperar la nota del cliente
@Serializable
data class NoteRequest(
    val note: String
)
