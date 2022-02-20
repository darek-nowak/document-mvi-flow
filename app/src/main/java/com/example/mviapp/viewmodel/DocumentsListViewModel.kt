package com.example.mviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mviapp.data.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class DocumentsListViewModel @Inject constructor(
    private val documentsInteractor: DocumentListsInteractor
) : ViewModel() {
    private val _documentsState: MutableStateFlow<DocumentsState> = MutableStateFlow(DocumentsState.InProgress)
    val documentsState = _documentsState as StateFlow<DocumentsState>

    private val _documentsEvent: MutableSharedFlow<UiEvent> = MutableSharedFlow()

    private val _documentsSideEffect: Channel<SideEffect> = Channel()
    val documentsSideEffect: Flow<SideEffect> = _documentsSideEffect.receiveAsFlow()

    init {
        collectUiEvents()
    }

    fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _documentsEvent.emit(event)
        }
    }

    private fun sendSideEffect(sideEffect: SideEffect) {
        viewModelScope.launch {  _documentsSideEffect.send(sideEffect) }
    }

    private fun setDocumentsState(state: DocumentsState) {
        _documentsState.value = state // viewModelScope.launch { _documentsState.emit(state) } ??
    }

    private fun collectUiEvents() {
        viewModelScope.launch {
            _documentsEvent.collect { event ->
                when(event) {
                    UiEvent.ScreenReady -> fetchDocuments()
                    is UiEvent.ItemClicked -> sendSideEffect(
                        SideEffect.NavigateToDetails(event.filename)
                    )
                }
            }
        }
    }

    //todo:
    // 1 model states, event, intents (events):
    // + UiEvents: Use shared flow to listen for user intents
    // + ScreenSideEffects: use Channel for it
    // + ScreenStates: Use StateFlow
    // 2 Unidirectional flow: user -> intent -> model -> view (view(model(intent)))
    // - Producer(intent)
    // - Reducer(state)
    // - Renderer(view)

    private fun fetchDocuments() {
        if (_documentsState.value is DocumentsState.Documents) { return }

        viewModelScope.launch {
            // processor -> result
            setDocumentsState(DocumentsState.InProgress)
            when(val result = documentsInteractor.getCvDocumentsList()) {
                is Result.Success -> setDocumentsState(DocumentsState.Documents(result.data))
                Result.Error -> setDocumentsState(DocumentsState.Error)
            }
        }
    }
}

sealed class DocumentsState {
    data class Documents(val data: List<CvDocumentInfo>) : DocumentsState()
    object Error : DocumentsState()
    object InProgress : DocumentsState()
}

sealed class UiEvent {
    object ScreenReady: UiEvent()
    data class ItemClicked(val filename: String): UiEvent()
}

sealed class SideEffect {
    data class NavigateToDetails(val filename: String): SideEffect()
}