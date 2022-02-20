package com.example.mviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mviapp.data.DocumentDetailsInteractor
import com.example.mviapp.data.DocumentDisplayItem
import com.example.mviapp.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DocumentDetailsViewModel @Inject constructor(
    private val detailsInteractor: DocumentDetailsInteractor
) :ViewModel() {

    private val _detailsFlow = MutableStateFlow<DetailsState>(DetailsState.InProgress)
    val detailsFlow = _detailsFlow as StateFlow<DetailsState>

    private var lastFilename = ""

    fun fetchDetails(filename: String) {
        if (filename == lastFilename) { return }

        lastFilename = filename
        viewModelScope.launch {
            setDetailsValue(DetailsState.InProgress)
            when(val result = detailsInteractor.getCvDocument(filename)) {
                is Result.Success -> setDetailsValue(DetailsState.Details(result.data))
                Result.Error -> setDetailsValue(DetailsState.Error)
            }
        }
    }

    private fun setDetailsValue(value: DetailsState) {
        _detailsFlow.value = value
    }
}

sealed class DetailsState {
    data class Details(val data: List<DocumentDisplayItem>) : DetailsState()
    object Error : DetailsState()
    object InProgress : DetailsState()
}