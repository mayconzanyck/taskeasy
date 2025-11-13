package com.maycon.taskeasy.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tarefas")
data class Tarefa(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val titulo: String = "",
    val descricao: String = "",
    val data: String = "",
    val concluida: Boolean = false,
    val usuarioId: String = ""
)