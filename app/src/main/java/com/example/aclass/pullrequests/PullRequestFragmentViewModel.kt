package com.example.aclass.pullrequests

import androidx.lifecycle.*
import com.example.aclass.common.di.IoDispatcher
import com.example.aclass.common.di.MainDispatcher
import com.example.aclass.common.model.PullRequest
import com.example.aclass.common.repository.RepoRepository
import com.example.aclass.common.util.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PullRequestFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repoRepository: RepoRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val args = PullRequestFragmentArgs.fromSavedStateHandle(savedStateHandle)

    init {
        viewModelScope.launch(ioDispatcher) {
            val pullRequests = repoRepository.getPullRequests(args.owner, args.repo)
            withContext(mainDispatcher) {
            }
        }
    }

    sealed class ViewState {
    }
}