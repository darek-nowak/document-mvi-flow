package com.example.mviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mviapp.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DocumentsListViewModel @Inject constructor(
    private val documentsInteractor: DocumentListsInteractor
) : ViewModel() {
    private val _documentsFlow: MutableStateFlow<DocumentsState> = MutableStateFlow(DocumentsState.InProgress)
    val documentsFlow = _documentsFlow as StateFlow<DocumentsState>

    //todo:
    // 1 model states, event, intents (events):
    // - UiEvents: Use shared flow to listen for user intents
    // - ScreenSideEffects: use Channel for it
    // + ScreenStates: Use StateFlow
    // 2 Unidirectional flow: user -> intent -> model -> view (view(model(intent)))
    // - Producer(intent)
    // - Reducer(state)
    // - Renderer(view)

    fun fetchDocuments() {
        if (_documentsFlow.value is DocumentsState.Documents) { return }

        viewModelScope.launch {
            // processor -> result
            setDocumentsValue(DocumentsState.InProgress)
            when(val result = documentsInteractor.getCvDocumentsList()) {
                is Result.Success -> setDocumentsValue(DocumentsState.Documents(result.data))
                Result.Error -> setDocumentsValue(DocumentsState.Error)
            }
        }
    }

    private fun setDocumentsValue(value: DocumentsState) {
        _documentsFlow.value = value
    }
}

sealed class DocumentsState {
    data class Documents(val data: List<CvDocumentInfo>) : DocumentsState()
    object Error : DocumentsState()
    object InProgress : DocumentsState()
}

sealed class UiEvent {
    data class ItemClicked(val filename: String): UiEvent()
}

sealed class SideEffect {
    data class NavigateToDetails(val filename: String): SideEffect()
}