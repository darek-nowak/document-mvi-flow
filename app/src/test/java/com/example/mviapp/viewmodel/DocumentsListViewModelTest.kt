package com.example.mviapp.viewmodel

import app.cash.turbine.test
import com.example.mviapp.data.CvDocumentInfo
import com.example.mviapp.data.DocumentListsInteractor
import com.example.mviapp.data.Result
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class DocumentsListViewModelTest {

    private val documentsInteractor: DocumentListsInteractor = mockk(relaxed = true)
    private val viewModel = DocumentsListViewModel(documentsInteractor)

    @Test
    fun `update view on success of document list loading`() = runBlocking {
        coEvery { documentsInteractor.getCvDocumentsList()  } returns Result.Success(DOCUMENTS_LIST)

        viewModel.documentsState.test {
            viewModel.sendEvent(DocumentsUiEvent.ScreenReady)

            assertThat(awaitItem()).isEqualTo(DocumentsState.InProgress)
            assertThat(awaitItem()).isEqualTo(DocumentsState.Documents(DOCUMENTS_LIST))
        }
    }

    @Test
    fun `update view on failure of document list loading`() = runBlocking {
        coEvery { documentsInteractor.getCvDocumentsList() } returns Result.Error

        viewModel.documentsState.test {
            viewModel.sendEvent(DocumentsUiEvent.ScreenReady)

            assertThat(awaitItem()).isEqualTo(DocumentsState.InProgress)
            assertThat(awaitItem()).isEqualTo(DocumentsState.Error)
        }
    }

    private companion object {
        val DOCUMENTS_LIST = listOf(CvDocumentInfo("sir_richard.json", "Sir Richard"))
    }
}

