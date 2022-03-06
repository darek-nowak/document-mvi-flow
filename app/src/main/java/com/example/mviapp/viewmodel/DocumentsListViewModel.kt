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

    private val _documentsEvent: MutableSharedFlow<DocumentsUiEvent> = MutableSharedFlow()

    private val _documentsState: MutableStateFlow<DocumentsState> = MutableStateFlow(DocumentsState.InProgress)
    val documentsState = _documentsState.asStateFlow()

    private val _documentsSideEffect: Channel<DocumentsSideEffect> = Channel()
    val documentsSideEffect: Flow<DocumentsSideEffect> = _documentsSideEffect.receiveAsFlow()

    init {
        collectUiEvents()
    }

    fun sendEvent(event: DocumentsUiEvent) {
        viewModelScope.launch {
            _documentsEvent.emit(event)
        }
    }

    private fun sendSideEffect(sideEffect: DocumentsSideEffect) {
        viewModelScope.launch {  _documentsSideEffect.send(sideEffect) }
    }

    private fun setDocumentsState(state: DocumentsState) {
        // or use update ?
        // https://proandroiddev.com/make-sure-to-update-your-stateflow-safely-in-kotlin-9ad023db12ba
        // set value is enough ?
        // https://proandroiddev.com/livedata-vs-sharedflow-and-stateflow-in-mvvm-and-mvi-architecture-57aad108816d
        _documentsState.value = state // viewModelScope.launch { _documentsState.emit(state) } ??
    }

    private fun collectUiEvents() {
        viewModelScope.launch {
            _documentsEvent.collect { event ->
                when(event) {
                    DocumentsUiEvent.ScreenReady -> fetchDocuments()
                    is DocumentsUiEvent.ItemClicked -> sendSideEffect(
                        DocumentsSideEffect.NavigateToDetails(event.filename)
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

    private suspend fun fetchDocuments() {
//        if (_documentsState.value is DocumentsState.Documents) {
//            return
//        }

        // processor -> result
        setDocumentsState(DocumentsState.InProgress)
        when (val result = documentsInteractor.getCvDocumentsList()) {
            is Result.Success -> setDocumentsState(DocumentsState.Documents(result.data))
            Result.Error -> setDocumentsState(DocumentsState.Error)
        }
    }
}

sealed class DocumentsState {
    data class Documents(val data: List<CvDocumentInfo>) : DocumentsState()
    object Error : DocumentsState()
    object InProgress : DocumentsState()
}

sealed class DocumentsUiEvent {
    object ScreenReady: DocumentsUiEvent()
    data class ItemClicked(val filename: String): DocumentsUiEvent()
}

sealed class DocumentsSideEffect {
    data class NavigateToDetails(val filename: String): DocumentsSideEffect()
}