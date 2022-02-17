package com.example.mviapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mviapp.data.GitHubApi
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        DocumentsListModule::class,
        DocumentDetailsModule::class,
        DispatchersModule::class
    ]
)
interface ApplicationComponent {
    fun documentsListComponent(): DocumentsListComponent.Factory
    fun documentDetailsComponent(): DocumentDetailsComponent.Factory
    fun okHttpClient(): OkHttpClient
}

@Module(includes = [ ApplicationModule.Bindings::class ])
open class ApplicationModule(private val baseUrl: String) {
    @Singleton
    @Provides
    open fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(JacksonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideJacksonObjectMapper() = ObjectMapper()

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient()

    @Provides
    fun provideGitHubApi(retrofit: Retrofit): GitHubApi = retrofit.create(GitHubApi::class.java)

    @Module
    abstract class Bindings {
        @Binds
        abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
    }
}


@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)


