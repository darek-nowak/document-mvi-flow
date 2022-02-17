package com.example.mviapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mviapp.MviApplication
import com.example.mviapp.R
import com.example.mviapp.data.CvDocumentInfo
import com.example.mviapp.databinding.FragmentDocumentsBinding
import com.example.mviapp.extensions.changeVisibility
import com.example.mviapp.setUpAppBar
import com.example.mviapp.viewmodel.DocumentsListViewModel
import com.example.mviapp.viewmodel.DocumentsState
import kotlinx.android.synthetic.main.fragment_documents.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DocumentsListFragment: Fragment(R.layout.fragment_documents) {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentDocumentsBinding

    private val documentsAdapter = DocumentsAdapter()
    private val viewModel: DocumentsListViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDocumentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MviApplication.applicationComponent
            .documentsListComponent()
            .create()
            .inject(this)

        if (savedInstanceState == null) {
            viewModel.fetchDocuments()
        }

        setupList()
        setUpToolbarTitle()

        observeDocumentsListChanges()
    }

    private fun observeDocumentsListChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.documentsFlow.collect { state ->
                    binding.renderView(state)
                    Timber.d("darek ${state.toString()}")
                }
            }
        }
    }

    private fun FragmentDocumentsBinding.renderView(state: DocumentsState) {
        when (state) {
            DocumentsState.InProgress -> {
                progressBar.changeVisibility(true)
            }
            DocumentsState.Error -> {
                progressBar.changeVisibility(false)
                errorText.changeVisibility(true)
            }
            is DocumentsState.Documents -> {
                progressBar.changeVisibility(false)
                documentsList.changeVisibility(true)
                showData(state.data)
            }
        }
    }

    private fun setupList() {
        documentsList.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = documentsAdapter
        }
        documentsAdapter.onItemClicked = { documentSelected ->
            DocumentDetailsFragment.attachIfNeeded(
                R.id.documentContainer,
                requireActivity().supportFragmentManager,
                documentSelected.filename
            )
        }
    }

    private fun setUpToolbarTitle() {
        requireActivity().setUpAppBar(titleText = getString(R.string.title_documents))
    }

    private fun showData(data: List<CvDocumentInfo>) {
        documentsAdapter.setItems(data)
    }

    companion object {
        private const val TAG = "documentsTag"

        fun attachIfNeeded(
            @IdRes containerId: Int,
            fragmentManager: FragmentManager
        ) {
            if(fragmentManager.findFragmentByTag(TAG) == null) {
                fragmentManager.beginTransaction()
                    .add(containerId, DocumentsListFragment(), TAG)
                    .commitAllowingStateLoss()
            }
        }
    }
}