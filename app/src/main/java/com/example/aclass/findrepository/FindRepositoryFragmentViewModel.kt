package com.example.aclass.findrepository

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FindRepositoryFragmentViewModel @Inject constructor() : ViewModel() {

    val repositories = listOf(
        "square/http",
        "JakeWharton/butterknife",
        "google/gson",
        "square/retrofit",
        "google/guava",
        "bumptech/glide",
        "androidx/androidx",
        "facebook/fresco"
    )

}