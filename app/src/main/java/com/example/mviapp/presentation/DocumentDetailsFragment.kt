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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mviapp.MviApplication
import com.example.mviapp.R
import com.example.mviapp.data.DocumentDisplayItem
import com.example.mviapp.databinding.FragmentDetailsBinding
import com.example.mviapp.extensions.changeVisibility
import com.example.mviapp.setUpAppBar
import com.example.mviapp.viewmodel.DetailsState
import com.example.mviapp.viewmodel.DocumentDetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DocumentDetailsFragment: Fragment(R.layout.fragment_details) {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentDetailsBinding

    private val detailsAdapter = DetailsAdapter()
    private val viewModel: DocumentDetailsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MviApplication.applicationComponent
            .documentDetailsComponent()
            .create()
            .inject(this)

        if (savedInstanceState == null) {
            viewModel.fetchDetails(
                filename = requireArguments().getString(DOCUMENT_NAME_KEY)!!
            )
        }


        setupList()
        setUpToolbarTitle()

        observeDetailsChanges()
    }

    private fun observeDetailsChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailsFlow.collect { state ->
                    binding.renderView(state)
                    Timber.d("darek ${state.toString()}")
                }
            }
        }
    }

    private fun FragmentDetailsBinding.renderView(state: DetailsState) {
        when (state) {
            DetailsState.InProgress -> {
                progressBar.changeVisibility(true)
            }
            DetailsState.Error -> {
                progressBar.changeVisibility(false)
                errorText.changeVisibility(true)
            }
            is DetailsState.Details -> {
                progressBar.changeVisibility(false)
                documentDetails.changeVisibility(true)
                showData(state.data)
            }
        }
    }

    private fun setUpToolbarTitle() {
        requireActivity().setUpAppBar(
            titleText = getString(R.string.title_details),
            homeEnabled = true
        )
    }

    private fun setupList() {
        documentDetails.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = detailsAdapter
        }
    }

    private fun showData(details: List<DocumentDisplayItem>) {
        detailsAdapter.setItems(details)
    }

    companion object {
        private const val DOCUMENT_NAME_KEY = "docNameArg"

        fun attachIfNeeded(
            @IdRes containerId: Int,
            fragmentManager: FragmentManager,
            documentName: String
        ) {
            fragmentManager.beginTransaction()
                .replace(containerId, newInstance(documentName))
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        private fun newInstance(documentName: String): Fragment = DocumentDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(DOCUMENT_NAME_KEY, documentName)
            }
        }
    }
}