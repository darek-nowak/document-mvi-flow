package com.example.mviapp.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject

class GithubDocRepository @Inject constructor(
    private val api: GitHubApi,
    private val base64Decoder: Base64Decoder,
    private val jsonObjectMapper: ObjectMapper
) {
    suspend fun fetchDocument(filename: String): CvData {
        val response =  api.getFileContent(file = filename)
        val decodedContent =  base64Decoder.decode(response.content)
        return jsonObjectMapper.readValue(decodedContent, CvData::class.java)
    }

    suspend fun fetchDocumentsList(): List<FileInfo> = api.getFilesList()
}

interface GitHubApi {

    @GET("repos/$USER/$DOC_REPO/contents")
    suspend fun getFilesList(): List<FileInfo>

    @GET("repos/$USER/$DOC_REPO/contents/{file}")
    suspend fun getFileContent(@Path("file") file: String): FileContent

    companion object {
        const val USER = "darek-nowak"
        const val DOC_REPO = "json"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class  FileInfo(
    @JsonProperty("name") val filename: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class  FileContent(
    @JsonProperty("encoding") val encoding: String,
    @JsonProperty("content") val content: String
)

data class CvData(
    @JsonProperty("applicant") val applicant: String,
    @JsonProperty("currentRole") val currentRole: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("experience") val experience: List<ExperienceItem>
)

data class ExperienceItem(
    @JsonProperty("startDate") val startDate: String,
    @JsonProperty("endDate") val endDate: String,
    @JsonProperty("company") val company: String,
    @JsonProperty("role") val role: String,
    @JsonProperty("responsibilities") val responsibilities: List<String>
)
