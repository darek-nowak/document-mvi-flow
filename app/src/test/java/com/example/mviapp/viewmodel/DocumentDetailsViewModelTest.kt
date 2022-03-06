package com.example.mviapp.viewmodel

import app.cash.turbine.test
import com.example.mviapp.data.CvDocumentInfo
import com.example.mviapp.data.DocumentDetailsInteractor
import com.example.mviapp.data.DocumentDisplayItem
import com.example.mviapp.data.Result
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class DocumentDetailsViewModelTest {

    private val detailsInteractor: DocumentDetailsInteractor = mockk()

    private val viewModel = DocumentDetailsViewModel(detailsInteractor)

    @Test
    fun `update view on success of document details loading`() = runBlocking {
        coEvery { detailsInteractor.getCvDocument(DOCUMENT_INFO.filename) } returns
                Result.Success(DOCUMENT_DETAILS)

        viewModel.detailsState.test {
            viewModel.sendEvent(DetailsUiEvent.ScreenReady(DOCUMENT_INFO.filename))

            assertThat(awaitItem()).isEqualTo(DetailsState.InProgress)
            assertThat(awaitItem()).isEqualTo(DetailsState.Details(DOCUMENT_DETAILS))
        }
    }

    @Test
    fun `update view on error of document details loading`() = runBlocking {
        coEvery { detailsInteractor.getCvDocument(DOCUMENT_INFO.filename) } returns
                Result.Error

        viewModel.detailsState.test {
            viewModel.sendEvent(DetailsUiEvent.ScreenReady(DOCUMENT_INFO.filename))

            assertThat(awaitItem()).isEqualTo(DetailsState.InProgress)
            assertThat(awaitItem()).isEqualTo(DetailsState.Error)
        }
    }

    private companion object {
        val DOCUMENT_INFO = CvDocumentInfo("Sir_Richard.json", "Sir Richard")
        val DOCUMENT_DETAILS = listOf(DocumentDisplayItem.ExtraBigItem("Sir Richard"))
    }
}