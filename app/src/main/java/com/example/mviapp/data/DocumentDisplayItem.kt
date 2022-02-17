package com.example.mviapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class DocumentDisplayItem: Parcelable {
    abstract val name: String

    @Parcelize
    data class ExtraBigItem(override val name: String) : DocumentDisplayItem()
    @Parcelize
    data class BigItem(override val name: String) : DocumentDisplayItem()
    @Parcelize
    data class ParagraphItem(override val name: String) : DocumentDisplayItem()
    @Parcelize
    data class Header(override val name: String) : DocumentDisplayItem()
    @Parcelize
    data class Item(override val name: String) : DocumentDisplayItem()
}

fun CvData.toDocumentDisplayItems() = mutableListOf(
    DocumentDisplayItem.ExtraBigItem(applicant),
    DocumentDisplayItem.ExtraBigItem(currentRole),
    DocumentDisplayItem.Item(description),
    DocumentDisplayItem.Header("Professional Experience")
).apply {
    experience.forEach { item ->
        add(DocumentDisplayItem.ParagraphItem(item.startDate + " - " + item.endDate.addIfEmpty("till now")))
        add(DocumentDisplayItem.BigItem(item.company))
        add(DocumentDisplayItem.BigItem(item.role))
        item.responsibilities.forEach {
            add(DocumentDisplayItem.Item("• $it"))
        }
    }
}

private fun String.addIfEmpty(name: String) = if (this.isBlank()) name else this