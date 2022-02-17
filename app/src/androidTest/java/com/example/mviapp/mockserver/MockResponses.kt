package com.example.mviapp.mockserver

import com.example.mviapp.rules.withBody
import com.example.mviapp.rules.withEncodedContentBody
import okhttp3.mockwebserver.MockResponse

object MockResponses {
    // DOCUMENTS LIST FETCHING RESPONSES
    val DOCUMENTS_LIST_RESPONSE_SUCCESS = "/repos/darek-nowak/json/contents" to
            MockResponse().withBody("documents_list.json")
    val DOCUMENTS_LIST_RESPONSE_ERROR = "/repos/darek-nowak/json/contents" to
            MockResponse().setResponseCode(401)

    // DOCUMENT DETAILS RESPONSES
    val DOCUMENT_DETAILS_RESPONSE_SUCCESS = "/repos/darek-nowak/json/contents/mark_twain.json" to
            MockResponse().withEncodedContentBody("mark_twain.json")
    val DOCUMENT_DETAILS_RESPONSE_ERROR = "/repos/darek-nowak/json/contents/marie_curie.json" to
            MockResponse().setResponseCode(401)
}