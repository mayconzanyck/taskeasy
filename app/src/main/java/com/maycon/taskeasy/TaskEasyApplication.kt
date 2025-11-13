package com.maycon.taskeasy

import android.app.Application
import androidx.room.Room
import com.maycon.taskeasy.data.AppDatabase
import com.maycon.taskeasy.data.TarefaRepository

class TaskEasyApplication : Application() {

    // 'lazy' significa que só vai ser construído quando for usado pela primeira vez
    private val database by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "taskeasy.db" // Nome do arquivo do banco
        ).build()
    }

    // O Repositório precisa do DAO (que vem do 'database')
    val repository by lazy {
        TarefaRepository(database.tarefaDao())
    }
}