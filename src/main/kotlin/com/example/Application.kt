package com.example

import com.example.entities.NotesEntity
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.select

fun main() {

    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/notes",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "administrador"
    )


    //Insertamos valores a la db
    /*
    database.insert(NotesEntity){
        set(it.note, "Wash clothes")
    }
    database.insert(NotesEntity){
        set(it.note, "Buy groceries")
    }
    database.insert(NotesEntity){
        set(it.note, "Workout")
    }
    */

    //Leemos valores de la db
    var notes = database.from(NotesEntity)
        .select()
    for (row in notes){
        println("${row[NotesEntity.id]}: ${row[NotesEntity.note]}")
    }

    //Esta parte va al final de la funcion main
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureRouting()
}
