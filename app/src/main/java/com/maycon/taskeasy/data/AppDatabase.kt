package com.maycon.taskeasy.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maycon.taskeasy.model.Tarefa

@Database(entities = [Tarefa::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    // Expõe o DAO para o resto do app
    abstract fun tarefaDao(): TarefaDao
}