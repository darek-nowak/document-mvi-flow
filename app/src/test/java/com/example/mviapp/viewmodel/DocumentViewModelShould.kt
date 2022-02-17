package com.example.mviapp.viewmodel

import androidx.lifecycle.Observer
import com.example.mviapp.CoroutineTestExtension
import com.example.mviapp.data.*
import com.example.mviapp.utils.InstantTaskExecutorExtension
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(
    value = [
        InstantTaskExecutorExtension::class,
        CoroutineTestExtension::class
    ]
)
internal class DocumentViewModelShould {
    private val documentsInteractor: DocumentListsInteractor = mockk()
    private val documentsObserver: Observer<DocumentsState> = mockk(relaxed = true)
    private val detailsInteractor: DocumentDetailsInteractor = mockk()
    private val detailsObserver: Observer<DetailsState> = mockk(relaxed = true)

    private lateinit var viewModel: DocumentsListViewModel

    @BeforeEach
    fun setUp() {
        viewModel = DocumentsListViewModel(documentsInteractor, detailsInteractor)
            .apply {
                documents.observeForever(documentsObserver)
                details.observeForever(detailsObserver)
            }
    }

    @Test
    fun `update view on success of document list loading`() {
        coEvery { documentsInteractor.getCvDocumentsList()  } returns Result.Success(DOCUMENTS_LIST)

        runBlockingTest {
            viewModel.fetchDocuments()
        }

        coVerifyOrder {
            documentsObserver.onChanged(DocumentsState.InProgress)
            documentsObserver.onChanged(DocumentsState.Documents(DOCUMENTS_LIST))
        }
    }

    @Test
    fun `update view on failure of document list loading`() {
        coEvery { documentsInteractor.getCvDocumentsList() } returns Result.Error

        runBlockingTest {
            viewModel.fetchDocuments()
        }

        coVerifyOrder {
            documentsObserver.onChanged(DocumentsState.InProgress)
            documentsObserver.onChanged(DocumentsState.Error)
        }
    }

    @Test
    fun `fetch documents list only once for subsequent invocations `() {
        coEvery { documentsInteractor.getCvDocumentsList() } returns
                Result.Success(DOCUMENTS_LIST)

        runBlockingTest {
            viewModel.fetchDocuments()
            viewModel.fetchDocuments()
        }

        coVerify(exactly = 1) { documentsInteractor.getCvDocumentsList() }
    }

    @Test
    fun `update view on success of document details loading`() {
        coEvery { detailsInteractor.getCvDocument(DOCUMENT_INFO.filename) } returns
             Result.Success(DOCUMENT_DETAILS)

        runBlockingTest {
            viewModel.fetchDetails(DOCUMENT_INFO.filename)
        }

        coVerifyOrder {
            detailsObserver.onChanged(DetailsState.InProgress)
            detailsObserver.onChanged(DetailsState.Details(DOCUMENT_DETAILS))
        }
    }

    @Test
    fun `update view on error of document details loading`() {
        coEvery { detailsInteractor.getCvDocument(DOCUMENT_INFO.filename) } returns
                Result.Error

        runBlockingTest {
            viewModel.fetchDetails(DOCUMENT_INFO.filename)
        }

        coVerifyOrder {
            detailsObserver.onChanged(DetailsState.InProgress)
            detailsObserver.onChanged(DetailsState.Error)
        }
    }

    @Test
    fun `fetch document details only once for subsequent invocations `() {
        coEvery { detailsInteractor.getCvDocument(DOCUMENT_INFO.filename) } returns
                Result.Success(DOCUMENT_DETAILS)

        runBlockingTest {
            viewModel.fetchDetails(DOCUMENT_INFO.filename)
            viewModel.fetchDetails(DOCUMENT_INFO.filename)
        }

        coVerify(exactly = 1) { detailsInteractor.getCvDocument(DOCUMENT_INFO.filename) }
    }

    @Test
    fun `call details interactor twice for subsequent invocations with different filename`() {
        coEvery { detailsInteractor.getCvDocument("first") } returns
            Result.Success(DOCUMENT_DETAILS)
        coEvery { detailsInteractor.getCvDocument("second") } returns
            Result.Success(DOCUMENT_DETAILS)

        runBlockingTest {
            viewModel.fetchDetails("first")
            viewModel.fetchDetails("second")
        }

        coVerifyOrder {
            detailsInteractor.getCvDocument("first")
            detailsInteractor.getCvDocument("second")
        }
    }

    private companion object {
        val DOCUMENTS_LIST = listOf(CvDocumentInfo("sir_richard.json", "Sir Richard"))
        val DOCUMENT_INFO = CvDocumentInfo("Sir_Richard.json", "Sir Richard")
        val DOCUMENT_DETAILS = listOf(DocumentDisplayItem.ExtraBigItem("Sir Richard"))
    }
}