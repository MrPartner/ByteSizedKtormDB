package com.example.routing

import com.example.db.DatabaseConnection
import com.example.entities.NotesEntity
import com.example.models.Note
import com.example.models.NoteRequest
import com.example.models.NoteResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.map
import org.ktorm.dsl.select

fun Application.notesRouter() {

    val db = DatabaseConnection.database

    routing {

        //GET show values from db
        get("/notes") {
            val notes = db.from(NotesEntity).select()
                .map {
                    val id = it[NotesEntity.id]
                    val note = it[NotesEntity.note]
                    Note(id ?: -1, note ?: "")
                }
            call.respond(notes)
        }

        //POST inserting values to db
        post("/notes") {
            val request = call.receive<NoteRequest>()
            val result = db.insert(NotesEntity) {
                set(it.note, request.note)
            }
            if (result == 1) {
                call.respond(
                    HttpStatusCode.OK, NoteResponse(
                        success = true,
                        data = "Values has been successfully inserted"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        success = false,
                        data = "Failed to insert values"
                    )
                )
            }
        }


    }
}