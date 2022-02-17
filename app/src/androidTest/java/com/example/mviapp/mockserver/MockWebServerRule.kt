package com.example.mviapp.rules

import androidx.test.platform.app.InstrumentationRegistry
import com.example.mviapp.data.Base64Decoder
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.rules.ExternalResource
import java.io.InputStreamReader

const val MOCK_WEBSERVER_PORT = 8080

class MockWebServerRule : ExternalResource() {

    private val mockWebServer = MockWebServer()

    override fun before() {
        super.before()
        mockWebServer.start(MOCK_WEBSERVER_PORT)
    }

    override fun after() {
        super.after()
        mockWebServer.shutdown()
    }

    fun setMockingDispatcherFor(vararg responses: Pair<String, MockResponse>) {
        mockWebServer.dispatcher = MockDispatcher(mapOf(*responses))
    }
}


private class MockDispatcher(private val responses: Map<String, MockResponse>) : Dispatcher() {
    @Throws(InterruptedException::class)
    override fun dispatch(request: RecordedRequest): MockResponse {
        return responses[request.path] ?: MockResponse().setResponseCode(404)
    }
}

fun MockResponse.withBody(fileName: String) =
    setBody(fileName.extractResponseFromAssets())
        .setResponseCode(200)

fun MockResponse.withEncodedContentBody(fileName: String, encoder: Base64Decoder = Base64Decoder()): MockResponse {
    val encodedContent = encoder.encode(fileName.extractResponseFromAssets())
    return setBody("""
        { 
          "encoding": "base64", 
          "content": "$encodedContent" 
        }
        """.toSingleLine())
        .setResponseCode(200)
}

private fun String.toSingleLine(): String =
    replace("\\s+".toRegex(), "")

private fun String.extractResponseFromAssets() = InstrumentationRegistry
    .getInstrumentation().context
    .assets
    .open("responses/$this").run {
        val inputStreamReader = InputStreamReader(this)
        val text = inputStreamReader.readText()
        inputStreamReader.close()
        text
    }
