package com.example.aclass.diff

import android.graphics.Color
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.BackgroundColorSpan
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.aclass.R
import com.example.aclass.common.di.DefaultDispatcher
import com.example.aclass.common.di.IoDispatcher
import com.example.aclass.common.repository.RepoRepository
import com.example.aclass.diff.DiffParser.Companion.parseDiff
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DiffFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repoRepository: RepoRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
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
                val diffItems = withContext(defaultDispatcher) { parseDiff(diff) }
                _viewState.postValue(ViewState.Diff(diffItems))
            } catch (exception: Exception) {
                _viewState.postValue(ViewState.Error(R.string.diff_failed_message))
            }
        }
    }

    sealed class ViewState {
        class Diff(
            val diffItems: List<DiffRecyclerItem>
        ) : ViewState()

        object Loading : ViewState()
        class Error(@StringRes val stringId: Int) : ViewState()
    }
}