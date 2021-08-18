package com.example.aclass.diff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.aclass.R
import com.example.aclass.databinding.FragmentDiffBinding
import com.example.aclass.databinding.FragmentPullRequestBinding
import com.example.aclass.pullrequests.PullRequestFragmentViewModel
import com.google.android.material.transition.MaterialSharedAxis

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

    private fun setupTransitions() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }
}