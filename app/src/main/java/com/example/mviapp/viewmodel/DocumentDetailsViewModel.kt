package com.example.mviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mviapp.data.DocumentDetailsInteractor
import com.example.mviapp.data.DocumentDisplayItem
import com.example.mviapp.data.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class DocumentDetailsViewModel @Inject constructor(
    private val detailsInteractor: DocumentDetailsInteractor
) :ViewModel() {

    private val _detailsEvent = MutableSharedFlow<DetailsUiEvent>()

    private val _detailsState = MutableStateFlow<DetailsState>(DetailsState.InProgress)
    val detailsState = _detailsState.asStateFlow()

    private val _detailsSideEffect = Channel<DetailsSideEffect>()
    val detailsSideEffect = _detailsSideEffect.receiveAsFlow()

    private var lastFilename = ""

    init {
        collectUiEvents()
    }

    private fun collectUiEvents() {
        viewModelScope.launch {
            _detailsEvent.collect { event ->
                when(event) {
                    is DetailsUiEvent.ScreenReady -> fetchDetails(event.filename)
                }
            }
        }
    }

    fun sendEvent(event: DetailsUiEvent) {
        viewModelScope.launch { _detailsEvent.emit(event) }
    }

    private fun sendState(state: DetailsState) {
        viewModelScope.launch { _detailsState.emit(state) } // _detailsState.value = state ??
    }

    private fun sendSideEffect(sideEffect: DetailsSideEffect) {
        viewModelScope.launch { _detailsSideEffect.send(sideEffect) }
    }

    private suspend fun fetchDetails(filename: String) {
        if (filename == lastFilename) {
            return
        }

        lastFilename = filename
        sendState(DetailsState.InProgress)
        when (val result = detailsInteractor.getCvDocument(filename)) {
            is Result.Success -> sendState(DetailsState.Details(result.data))
            Result.Error -> sendState(DetailsState.Error)
        }
    }
}

sealed class DetailsState {
    data class Details(val data: List<DocumentDisplayItem>) : DetailsState()
    object Error : DetailsState()
    object InProgress : DetailsState()
}

sealed class DetailsUiEvent {
    data class ScreenReady(val filename: String): DetailsUiEvent()
}

sealed class DetailsSideEffect {
    data class NavigateToDetails(val filename: String): DetailsSideEffect()
}