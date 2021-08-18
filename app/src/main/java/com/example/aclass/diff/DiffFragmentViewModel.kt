package com.example.aclass.diff

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.aclass.R
import com.example.aclass.common.di.IoDispatcher
import com.example.aclass.common.repository.RepoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiffFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repoRepository: RepoRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val args = DiffFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState>
        get() = _viewState

    init {
        getDiff()
    }

    private fun getDiff() {
        _viewState.value = ViewState.Loading
        viewModelScope.launch(ioDispatcher) {
            try {
                val diff = repoRepository.getDiff(args.owner, args.repo, args.number)
                _viewState.postValue(ViewState.Diff(diff))
            } catch (exception: Exception) {
                _viewState.postValue(ViewState.Error(R.string.diff_failed_message))
            }
        }
    }

    sealed class ViewState {
        class Diff(
            val data: String
        ) : ViewState()

        object Loading : ViewState()
        class Error(@StringRes val stringId: Int) : ViewState()
    }
}