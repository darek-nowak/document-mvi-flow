package com.example.mviapp.data

import android.os.Parcelable
import com.example.mviapp.CoroutineDispatchersProvider
import kotlinx.android.parcel.Parcelize
import java.util.*
import javax.inject.Inject

class DocumentListsInteractor @Inject constructor(
    private val docRepository: GithubDocRepository,
    private val dispatchersProvider: CoroutineDispatchersProvider
) {
    suspend fun getCvDocumentsList(): Result<List<CvDocumentInfo>> =
        asResult(dispatchersProvider.io()) {
            docRepository.fetchDocumentsList()
                .map { file ->
                    CvDocumentInfo(
                        name = file.filename.toApplicantName(),
                        filename = file.filename
                    )
                }
        }

    private fun String.toApplicantName() =
        removeSuffix(".json")
            .split("_")
            .joinToString(" ") { it.capitalize(Locale.UK) }
}

@Parcelize
data class CvDocumentInfo(val filename: String, val name: String) : Parcelable
