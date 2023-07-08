package com.example.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

//Este objeto nos enlaza con la entidad de la db, para los diferentes request GET, POST, PUT, DELETE...
object NotesEntity: Table<Nothing>("instituto"){
    val id = int("id").primaryKey()
    val note = varchar("note")
}