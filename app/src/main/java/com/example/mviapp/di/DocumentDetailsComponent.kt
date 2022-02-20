package com.example.mviapp.di

import androidx.lifecycle.ViewModel
import com.example.mviapp.presentation.DocumentDetailsFragment
import com.example.mviapp.viewmodel.DocumentDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Subcomponent
interface DocumentDetailsComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): DocumentDetailsComponent
    }

    fun inject(documentDetailsFragment: DocumentDetailsFragment)
}

@Module(subcomponents = [ DocumentDetailsComponent::class ])
abstract class DocumentDetailsModule {
    @Binds
    @IntoMap
    @ViewModelKey(DocumentDetailsViewModel::class)
    abstract fun provideDetailsViewModel(viewModel: DocumentDetailsViewModel): ViewModel
}