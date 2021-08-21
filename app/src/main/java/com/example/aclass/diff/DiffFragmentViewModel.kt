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
                val diffItems = parseDiff(diff)
                _viewState.postValue(ViewState.Diff(diffItems))
            } catch (exception: Exception) {
                _viewState.postValue(ViewState.Error(R.string.diff_failed_message))
            }
        }
    }

    private suspend fun parseDiff(diff: String): List<DiffRecyclerItem> {
        val items = mutableListOf<DiffRecyclerItem>()

        withContext(defaultDispatcher) {
            var lineNumberRange = ""
            var isCode = false
            var isOneFile = false
            var firstCommitLines: CharSequence = ""
            var secondCommitLines: CharSequence = ""
            var continuousRedLineCount = 0

            for (line in diff.lines()) {
                if (line.startsWith("deleted file mode") || line.startsWith("new file mode"))
                    isOneFile = true

                val title = checkForTitle(line)
                if (title != "") {
                    if (isCode) {
                        if (isOneFile) {
                            items.add(
                                DiffRecyclerItem.SectionOneFile(
                                    lineNumberRange,
                                    secondCommitLines
                                )
                            )
                        } else {
                            items.add(
                                DiffRecyclerItem.SectionTwoFiles(
                                    lineNumberRange,
                                    firstCommitLines,
                                    secondCommitLines
                                )
                            )
                        }
                        lineNumberRange = ""
                        isCode = false
                        isOneFile = false
                        firstCommitLines = ""
                        secondCommitLines = ""
                    }
                    items.add(DiffRecyclerItem.Title(title))
                    continue
                }

                if (line.startsWith("@@ ")) {
                    if (isCode) {
                        items.add(
                            DiffRecyclerItem.SectionTwoFiles(
                                lineNumberRange,
                                firstCommitLines,
                                secondCommitLines
                            )
                        )
                        firstCommitLines = ""
                        secondCommitLines = ""
                    }
                    lineNumberRange = line
                    isCode = true
                    continue
                }

                if (isCode) {
                    var isRed = false
                    var isGreen = false

                    val span: SpannableString = if (line.startsWith("+")) {
                        val str = SpannableString(line)
                        str.setSpan(BackgroundColorSpan(Color.GREEN), 0, line.length, 0)
                        isGreen = true
                        isRed = false
                        str
                    } else if (line.startsWith("-")) {
                        val str = SpannableString(line)
                        str.setSpan(BackgroundColorSpan(Color.RED), 0, line.length, 0)
                        isRed = true
                        isGreen = false
                        str
                    } else {
                        isGreen = false
                        isRed = false
                        SpannableString(line)
                    }

                    if (isGreen && continuousRedLineCount == 0) {
                        firstCommitLines = TextUtils.concat(firstCommitLines, "\n")
                        secondCommitLines = TextUtils.concat(secondCommitLines, span, "\n")

                    } else if (isGreen && continuousRedLineCount > 0) {
                        secondCommitLines = TextUtils.concat(secondCommitLines, span, "\n")
                        continuousRedLineCount--
                    } else if (isRed) {
                        continuousRedLineCount++
                        firstCommitLines = TextUtils.concat(firstCommitLines, span, "\n")
                    } else {
                        if (continuousRedLineCount > 0) {
                            for (i in 0 until continuousRedLineCount)
                                secondCommitLines = TextUtils.concat(secondCommitLines, "\n")

                            firstCommitLines = TextUtils.concat(firstCommitLines, span, "\n")
                            secondCommitLines = TextUtils.concat(secondCommitLines, span, "\n")

                            continuousRedLineCount = 0
                        } else {
                            firstCommitLines = TextUtils.concat(firstCommitLines, span, "\n")
                            secondCommitLines = TextUtils.concat(secondCommitLines, span, "\n")
                        }
                    }
                }
            }

            // add last
            if (isOneFile) {
                items.add(
                    DiffRecyclerItem.SectionOneFile(
                        lineNumberRange,
                        secondCommitLines
                    )
                )
            } else {
                items.add(
                    DiffRecyclerItem.SectionTwoFiles(
                        lineNumberRange,
                        firstCommitLines,
                        secondCommitLines
                    )
                )
            }
        }

        return items
    }

    private fun addSection(
        isOneFile: Boolean,
        lineNumberRange: String,
        code: List<String>,
        diffRecyclerItems: MutableList<DiffRecyclerItem>
    ) {
        if (isOneFile) {
            //diffRecyclerItems.add(DiffRecyclerItem.SectionOneFile(lineNumberRange, code))
            return
        }

        val firstCommitLines = mutableListOf<String>()
        val secondCommitLines = mutableListOf<String>()
        val stack = Stack<String>()

        for (line in code) {
            if (line.startsWith("+")) {

            } else if (line.startsWith("-")) {
                firstCommitLines.add(line)

            } else {
                firstCommitLines.add(line)
                secondCommitLines.add(line)
            }
        }


    }

    private fun checkForTitle(
        line: String,
    ): String {
        val id = "diff --git"

        if (line.startsWith(id)) {
            val split = line.split("/")
            return split[split.lastIndex]
        }

        return ""
    }

    sealed class ViewState {
        class Diff(
            val diffItems: List<DiffRecyclerItem>
        ) : ViewState()

        object Loading : ViewState()
        class Error(@StringRes val stringId: Int) : ViewState()
    }

    //                        if (isOneFile)
//                            diffRecyclerItems.add(
//                                DiffRecyclerItem.SectionOneFile(
//                                    lineNumberRange,
//                                    code.toList()
//                                )
//                            )
//                        else

    //                diffRecyclerItems.add(
//                    DiffRecyclerItem.SectionOneFile(
//                        lineNumberRange,
//                        code.toList()
//                    )
//                )
//            else
}