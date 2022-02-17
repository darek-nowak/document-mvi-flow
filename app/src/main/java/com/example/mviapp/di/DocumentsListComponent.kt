package com.example.mviapp.di

import androidx.lifecycle.ViewModel
import com.example.mviapp.presentation.DocumentsListFragment
import com.example.mviapp.viewmodel.DocumentsListViewModel
import dagger.*
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Subcomponent
interface DocumentsListComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): DocumentsListComponent
    }

    fun inject(documentsListFragment: DocumentsListFragment)
}

@Module(subcomponents = [ DocumentsListComponent::class ])
abstract class DocumentsListModule {
    @Binds
    @IntoMap
    @ViewModelKey(DocumentsListViewModel::class)
    abstract fun provideDocumentsListViewModel(viewModel: DocumentsListViewModel): ViewModel
}


