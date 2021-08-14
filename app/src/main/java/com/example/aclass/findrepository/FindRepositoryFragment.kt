package com.example.aclass.findrepository

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.aclass.databinding.FragmentFindRepositoryBinding
import com.google.android.material.chip.Chip

class FindRepositoryFragment : Fragment() {

    private val viewModel: FindRepositoryFragmentViewModel by viewModels()

    private var _binding: FragmentFindRepositoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindRepositoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRepositoryChips()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRepositoryChips() {
        val flowLayout = binding.flowLayout

        viewModel.repositories.map {
            val chip = Chip(requireContext())
            chip.text = it
            flowLayout.addView(chip)
        }
    }
}