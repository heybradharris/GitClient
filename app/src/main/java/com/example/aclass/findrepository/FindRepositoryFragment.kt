package com.example.aclass.findrepository

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.aclass.R
import com.example.aclass.common.util.event.EventObserver
import com.example.aclass.common.util.getColorFromAttr
import com.example.aclass.databinding.FragmentFindRepositoryBinding
import com.google.android.material.chip.Chip
import com.example.aclass.findrepository.FindRepositoryFragmentViewModel.ViewEvent
import com.google.android.material.animation.AnimationUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class FindRepositoryFragment : Fragment() {

    private val viewModel: FindRepositoryFragmentViewModel by viewModels()

    private var _binding: FragmentFindRepositoryBinding? = null
    private val binding get() = _binding!!

    private var isRandomRepoAnimationActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewEvents()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindRepositoryBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRepositoryChips()
        setupClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListeners() {
        binding.findRepositoryButton.setOnClickListener { viewModel.onFindRepositoryButtonClicked() }
        binding.randomRepositoryButton.setOnClickListener { viewModel.onRandomRepositoryButtonClicked() }
    }

    private fun observeViewEvents() {
        viewModel.navigateToPullRequests.observe(this, EventObserver { event ->
            when (event) {
                is ViewEvent.NavigateToPullRequests -> { }
                is ViewEvent.RandomRepo -> { chooseRandomRepo() }
                is ViewEvent.Error -> { }
            }
        })
    }

    private fun setupRepositoryChips() {
        val flowLayout = binding.flowLayout

        viewModel.repoTitles.map { repoTitle ->
            val chip = Chip(requireContext()).apply {
                text = repoTitle
                setOnClickListener { binding.repoTextEditText.setText(repoTitle) }
            }
            flowLayout.addView(chip)
        }
    }

    private fun chooseRandomRepo() {
        if (isRandomRepoAnimationActive) return
        isRandomRepoAnimationActive = true

        val yellow = requireContext().getColorFromAttr(R.attr.colorSecondary)
        val numChildren = binding.flowLayout.childCount
        var currentChip = binding.flowLayout[Random.nextInt(numChildren)] as Chip
        var prevChip: Chip? = null
        val defaultColorStateList = currentChip.chipBackgroundColor
        val interpolator = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR
        val duration = 350L

        viewLifecycleOwner.lifecycleScope.launch {
            for (i in 0 until numChildren + 10) {
                prevChip?.chipBackgroundColor = defaultColorStateList
                currentChip.chipBackgroundColor = ColorStateList.valueOf(yellow)
                delay(duration - (duration * interpolator.getInterpolation(
                    if (i == 0) 0.20f else 1 - (i / (numChildren + 9f))
                )).toLong())
                prevChip = currentChip
                currentChip = binding.flowLayout[Random.nextInt(numChildren)] as Chip
                while (prevChip == currentChip) {
                    currentChip = binding.flowLayout[Random.nextInt(numChildren)] as Chip
                }
            }
            binding.repoTextEditText.setText(prevChip?.text)
            vibrate()
            delay(duration)
            prevChip?.chipBackgroundColor = defaultColorStateList

            isRandomRepoAnimationActive = false
        }
    }

    private fun vibrate() {
        val v = (requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(
                VibrationEffect.createOneShot(150,
                VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}