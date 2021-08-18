package com.example.aclass.pullrequests

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.aclass.R
import com.example.aclass.common.di.IoDispatcher
import com.example.aclass.common.di.MainDispatcher
import com.example.aclass.common.model.PullRequest
import com.example.aclass.common.repository.RepoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PullRequestFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repoRepository: RepoRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val args = PullRequestFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState>
        get() = _viewState

    private var pullRequestItems: List<PullRequest> = mutableListOf()

    private val openPullRequestItems: List<PullRequest>
        get() = pullRequestItems.filter { it.state == "open" }

    init {
        getPullRequests()
    }

    private fun getPullRequests() {
        _viewState.value = ViewState.Loading
        viewModelScope.launch(ioDispatcher) {
            try {
                pullRequestItems = repoRepository.getPullRequests(args.owner, args.repo)
                _viewState.postValue(ViewState.PullRequests(pullRequestItems))
            } catch (exception: Exception) {
                _viewState.postValue(ViewState.Error(R.string.pull_requests_failed_message))
            }
        }
    }

    fun onChecked(isChecked: Boolean) {
        val items = if (isChecked) openPullRequestItems else pullRequestItems
        _viewState.value = ViewState.PullRequests(items)
    }

    sealed class ViewState {
        object Loading : ViewState()
        class PullRequests(val pullRequestItems: List<PullRequest>) : ViewState()
        class Error(@StringRes val stringId: Int) : ViewState()
    }
}