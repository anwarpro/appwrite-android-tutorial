package com.helloanwar.ideatracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helloanwar.ideatracker.Appwrite
import com.helloanwar.ideatracker.NetworkResource
import io.appwrite.models.DocumentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _state: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState>
        get() = _state.asStateFlow()

    fun getIdeas() = viewModelScope.launch {
        try {
            _state.update {
                it.copy(ideas = NetworkResource.Loading)
            }
            val ideas = Appwrite.ideaRepository.getIdeas()
            _state.update {
                it.copy(ideas = NetworkResource.Success(ideas))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _state.update {
                it.copy(ideas = NetworkResource.Error(e))
            }
        }
    }

    fun addIdea(idea: Map<String, Any>) = viewModelScope.launch {
        Appwrite.ideaRepository.addIdea(idea = idea)
    }

    fun removeIdea(documentId: String) = viewModelScope.launch {
        Appwrite.ideaRepository.removeIdea(documentID = documentId)
    }

    fun currentSession() = viewModelScope.launch {
        try {
            val user = Appwrite.account.getSession("current")
            _state.update { it.copy(isLoggedIn = true) }
        } catch (e: Exception) {
            e.printStackTrace()
            _state.update {
                it.copy(isLoggedIn = false)
            }
        }
    }

    init {
        getIdeas()
        currentSession()
    }
}

data class HomeUiState(
    val ideas: NetworkResource<DocumentList<Map<String, Any>>> = NetworkResource.Idle,
    val isLoggedIn: Boolean = false
)