package com.example.aclass.diff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.example.aclass.MainActivity
import com.example.aclass.databinding.FragmentDiffBinding
import com.example.aclass.diff.DiffFragmentViewModel.ViewState
import com.example.aclass.pullrequests.PullRequestFragmentViewModel
import com.example.aclass.pullrequests.PullRequestRecyclerAdapter
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiffFragment : Fragment() {

    private val viewModel by viewModels<DiffFragmentViewModel>()

    private var _binding: FragmentDiffBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransitions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewState()
    }

    private fun observeViewState() {
        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            when (viewState) {
                is ViewState.Loading -> { showLoadingIndicator() }
                is ViewState.Diff -> {
                    showDiff(viewState.diffItems)
                }
                is ViewState.Error -> {
                    (requireActivity() as MainActivity).showSnackbar(viewState.stringId)
                    findNavController().popBackStack()
                }
            }
        })
    }

    private fun showDiff(diffItems: List<DiffRecyclerItem>) {
        val transition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        TransitionManager.beginDelayedTransition(view as ViewGroup, transition)

        binding.recyclerView.adapter = DiffRecyclerAdapter(diffItems)

        binding.progressIndicator.isVisible = false
        binding.recyclerView.isVisible = true
    }

    private fun showLoadingIndicator() {
        binding.recyclerView.isVisible = false
        binding.progressIndicator.isVisible = true
    }

    private fun setupTransitions() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }
}