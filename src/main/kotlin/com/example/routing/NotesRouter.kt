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
import org.ktorm.dsl.*

fun Application.notesRouter() {

    val db = DatabaseConnection.database

    routing {

        //GET show values from db
        get("/notes") {
            //con este codigo mostramos todos los valores de la db
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
            //con esta linea recuperamos la nota del cliente
            val request = call.receive<NoteRequest>()

            //con este codigo verificamos el request si es valido o no, y de ser valido insertamos el nuevo valor
            //y respondemos al cliente Ok o BadRequest
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

        //GET show a single value from id
        get("/notes/{id}") {
            //con esta linea recuperamos la nota por id del cliente
            val id = call.parameters["id"]?.toInt() ?: -1

            //con este codigo verificamos el request si es valido o no, y de ser valido mostramos la nota por id
            //y respondemos al cliente NotFound o OK
            val note = db.from(NotesEntity)
                .select()
                .where { NotesEntity.id eq id }
                .map {
                    val id = it[NotesEntity.id]!!
                    val note = it[NotesEntity.note]!!
                    Note(id = id, note = note)
                }.firstOrNull()

            if (note == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    NoteResponse(
                        success = false,
                        data = "Could not found note with id $id"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    NoteResponse(
                        success = true,
                        data = note
                    )
                )
            }
        }

        //PUT update a note by id
        put("/notes/{id}") {
            //con las siguientes dos lineas, capturamos el id y la nota a upgradear
            val id = call.parameters["id"]?.toInt() ?: -1
            val updateNote = call.receive<NoteRequest>()

            //con este codigo verificamos el request si es valido o no, y de ser valido seteamos el nuevo valor
            //y respondemos al cliente OK o BadRequest
            val rowsEffected = db.update(NotesEntity) {
                set(it.note, updateNote.note)
                where {
                    it.id eq id
                }
            }
            if (rowsEffected == 1) {
                call.respond(
                    HttpStatusCode.OK,
                    NoteResponse(
                        success = true,
                        data = "Note successfully updated"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Note failed to update"
                    )
                )
            }
        }

        //DELETE note by id
        delete("/notes/{id}") {
            //con esta linea recuperamos el id de la nota a deletear
            val id = call.parameters["id"]?.toInt() ?: -1

            //con este codigo verificamos el request si es valido o no, y de ser valido deleteamos la nota por id
            //y respondemos al cliente OK o BadRequest
            val rowsEffected = db.delete(NotesEntity) {
                it.id eq id
            }
            if (rowsEffected == 1) {
                call.respond(
                    HttpStatusCode.OK,
                    NoteResponse(
                        success = true,
                        data = "Note successfully deleted"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Could not found note with id = $id"
                    )
                )
            }
        }


    }
}