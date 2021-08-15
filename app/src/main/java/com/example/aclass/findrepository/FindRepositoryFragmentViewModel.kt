package com.example.aclass.findrepository

import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aclass.R
import com.example.aclass.common.util.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FindRepositoryFragmentViewModel @Inject constructor() : ViewModel() {

    private val _viewEvents = MutableLiveData<Event<ViewEvent>>()
    val navigateToPullRequests: LiveData<Event<ViewEvent>>
        get() = _viewEvents

    val userInput = ObservableField<String>()

    val repoTitles = listOf(
        "square/http",
        "JakeWharton/butterknife",
        "google/gson",
        "square/retrofit",
        "google/guava",
        "bumptech/glide",
        "androidx/androidx",
        "facebook/fresco"
    )

    fun onFindRepositoryButtonClicked() {
        if (isUserInputValid()) {
            // naviagte
        } else {
            _viewEvents.value = Event(ViewEvent.Error(R.string.find_repository_bad_input))
        }
    }

    fun onRandomRepositoryButtonClicked() {
        _viewEvents.value = Event(ViewEvent.RandomRepo)
    }

    private fun isUserInputValid() =
        Regex("^[A-Za-z0-9_.][A-Za-z0-9_.-]+(/[A-Za-z0-9_.-]+)\$").matches(userInput.get() ?: "")

    sealed class ViewEvent {
        class NavigateToPullRequests(val owner: String, val repo: String) : ViewEvent()
        object RandomRepo : ViewEvent()
        class Error(@StringRes val stringId: Int) : ViewEvent()
    }
}