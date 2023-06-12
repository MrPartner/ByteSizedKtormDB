package com.example.models

import kotlinx.serialization.Serializable

//Esta data class es para responder al cliente
@Serializable
data class NoteResponse<T>(
    val data: T,
    val success: Boolean
)
