package com.maycon.taskeasy.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.maycon.taskeasy.model.Tarefa
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class TarefaRepository(
    private val tarefaDao: TarefaDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private fun getColecaoTarefas(idUsuario: String) =
        firestore.collection("usuarios").document(idUsuario).collection("tarefas")

    suspend fun carregarTarefasDoFirestore(idUsuario: String) {
        try {
            val snapshot = getColecaoTarefas(idUsuario).get().await()
            val tarefasDaNuvem = snapshot.toObjects(Tarefa::class.java)

            limparTarefasLocais(idUsuario)

            for (tarefa in tarefasDaNuvem) {
                tarefaDao.inserir(tarefa)
            }
        } catch (e: Exception) {
            // Se der erro de internet, o app vai carregar o que já estava no Room
            Log.e("Firestore", "Erro ao CARREGAR tarefas: ${e.message}")
        }
    }

    fun getTarefasPorUsuario(idUsuario: String): Flow<List<Tarefa>> {
        return tarefaDao.getTarefasPorUsuario(idUsuario)
    }

    suspend fun inserir(tarefa: Tarefa) {
        try {
            getColecaoTarefas(tarefa.usuarioId).document(tarefa.id).set(tarefa).await()
            tarefaDao.inserir(tarefa)
        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao INSERIR tarefa: ${e.message}")
        }
    }

    suspend fun atualizar(tarefa: Tarefa) {
        try {
            getColecaoTarefas(tarefa.usuarioId).document(tarefa.id).set(tarefa).await()
            tarefaDao.atualizar(tarefa)
        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao ATUALIZAR tarefa: ${e.message}")
        }
    }

    suspend fun deletar(tarefa: Tarefa) {
        try {
            getColecaoTarefas(tarefa.usuarioId).document(tarefa.id).delete().await()
            tarefaDao.deletar(tarefa)
        } catch (e: Exception) {
            Log.e("Firestore", "Erro ao DELETAR tarefa: ${e.message}")
        }
    }

    private suspend fun limparTarefasLocais(idUsuario: String) {
        tarefaDao.getTarefasPorUsuarioSincrono(idUsuario).forEach { tarefaLocal ->
            // Aqui vamos deletar do DAO sem chamar o firestore de novo
            try {
                tarefaDao.deletar(tarefaLocal)
            } catch (e: Exception) {
                Log.e("Firestore", "Erro ao LIMPAR local: ${e.message}")
            }
        }
    }
}