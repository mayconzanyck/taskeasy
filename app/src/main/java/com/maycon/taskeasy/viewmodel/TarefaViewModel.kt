package com.maycon.taskeasy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.maycon.taskeasy.data.TarefaRepository
import com.maycon.taskeasy.model.Tarefa
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// A ViewModel precisa do Repositório para funcionar
class TarefaViewModel(private val repository: TarefaRepository) : ViewModel() {

    // ID do usuário logado (vamos 'chumbar' um valor por enquanto para testes)
    private val idUsuarioLogado = "usuario_teste_123"

    // Aqui o 'Flow' do DAO é convertido em 'StateFlow'
    // A tela vai "ouvir" esse 'stateFlow' para receber a lista de tarefas
    val todasTarefas: StateFlow<List<Tarefa>> =
        repository.getTarefasPorUsuario(idUsuarioLogado)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

    // Funções que a tela vai chamar (usando Corrotinas)
    fun inserir(tarefa: Tarefa) {
        viewModelScope.launch {
            repository.inserir(tarefa)
        }
    }

    fun atualizar(tarefa: Tarefa) {
        viewModelScope.launch {
            repository.atualizar(tarefa)
        }
    }

    fun deletar(tarefa: Tarefa) {
        viewModelScope.launch {
            repository.deletar(tarefa)
        }
    }
}

// Isso é uma "Fábrica" (Factory)
// Ela ensina o Android a criar nossa ViewModel passando o Repositório para ela.
class TarefaViewModelFactory(private val repository: TarefaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TarefaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TarefaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}