package com.maycon.taskeasy.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.maycon.taskeasy.model.Tarefa
import kotlinx.coroutines.flow.Flow

@Dao
interface TarefaDao {

    // ... (as funções inserir, atualizar, deletar ficam iguais) ...
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun inserir(tarefa: Tarefa)

    @Update
    suspend fun atualizar(tarefa: Tarefa)

    @Delete
    suspend fun deletar(tarefa: Tarefa)

    // Esta é a que o ViewModel usa (Flow, assíncrona)
    @Query("SELECT * FROM tarefas WHERE usuarioId = :idUsuario ORDER BY concluida ASC")
    fun getTarefasPorUsuario(idUsuario: String): Flow<List<Tarefa>>

    // --- ADICIONE ESTA FUNÇÃO ---
    // Usada pelo Repositório para limpar o cache local (Síncrona)
    @Query("SELECT * FROM tarefas WHERE usuarioId = :idUsuario")
    suspend fun getTarefasPorUsuarioSincrono(idUsuario: String): List<Tarefa>
}