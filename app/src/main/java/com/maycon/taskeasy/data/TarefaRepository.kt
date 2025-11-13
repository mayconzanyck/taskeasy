package com.maycon.taskeasy.data

import com.maycon.taskeasy.model.Tarefa
import kotlinx.coroutines.flow.Flow

// O Repositório precisa do DAO para poder acessar o banco.
// Recebemos o dao no construtor.
class TarefaRepository(private val tarefaDao: TarefaDao) {

    // Esta função simplesmente "repete" o que o DAO faz.
    // Mais tarde, é aqui que poderíamos adicionar a lógica do Firebase.
    fun getTarefasPorUsuario(idUsuario: String): Flow<List<Tarefa>> {
        return tarefaDao.getTarefasPorUsuario(idUsuario)
    }

    // Usamos 'suspend' pois são operações de banco (I/O)
    suspend fun inserir(tarefa: Tarefa) {
        tarefaDao.inserir(tarefa)
    }

    suspend fun atualizar(tarefa: Tarefa) {
        tarefaDao.atualizar(tarefa)
    }

    suspend fun deletar(tarefa: Tarefa) {
        tarefaDao.deletar(tarefa)
    }
}