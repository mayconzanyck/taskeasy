package com.maycon.taskeasy.viewmodel

// ... (imports ficam iguais) ...
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.maycon.taskeasy.data.TarefaRepository
import com.maycon.taskeasy.model.Tarefa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class TarefaViewModel(private val repository: TarefaRepository) : ViewModel() {

    private val _tarefas = MutableStateFlow<List<Tarefa>>(emptyList())
    val tarefas: StateFlow<List<Tarefa>> = _tarefas.asStateFlow()

    private var idUsuarioAtual: String? = null

    fun carregarTarefas(idUsuario: String) {
        if (idUsuario == idUsuarioAtual) return
        idUsuarioAtual = idUsuario

        viewModelScope.launch {
            // 1. Manda o Repositório buscar da nuvem e salvar no local
            repository.carregarTarefasDoFirestore(idUsuario)

            // 2. Ouve o banco local (que agora é um espelho da nuvem)
            repository.getTarefasPorUsuario(idUsuario)
                .distinctUntilChanged()
                .collect { listaDeTarefas ->
                    _tarefas.value = listaDeTarefas
                }
        }
    }

    // ... (o resto do arquivo: inserir, atualizar, deletar e a Factory
    //      ficam EXATAMENTE IGUAIS) ...

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

// ... (a Factory continua igual) ...
class TarefaViewModelFactory(private val repository: TarefaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TarefaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TarefaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}