package com.example.aclass.diff

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
                val diffRecyclerItems = parseDiff(diff)

                val sb = StringBuilder()

                for (item in diffRecyclerItems) {
                    if (item is DiffRecyclerItem.Title) {
                        sb.append(item.title + "\n")
                    }

                    if (item is DiffRecyclerItem.SectionOneFile) {
                        sb.append(item.lineNumberRange + "\n")

                        for (me in item.commitLines)
                            sb.append(me + "\n")
                    }

                    if (item is DiffRecyclerItem.SectionTwoFiles) {
                        sb.append(item.lineNumberRange + "\n")

                        for (me in item.firstCommitLines)
                            sb.append(me + "\n")
                    }
                }

                _viewState.postValue(ViewState.Diff(sb.toString()))
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
            val code = mutableListOf<String>()
            var isOneFile = false

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
                                    code.toList()
                                )
                            )
                        } else {
                            items.add(
                                DiffRecyclerItem.SectionTwoFiles(
                                    lineNumberRange,
                                    code.toList(),
                                    code.toList()
                                )
                            )
                        }
                        lineNumberRange = ""
                        isCode = false
                        isOneFile = false
                        code.clear()
                    }
                    items.add(DiffRecyclerItem.Title(title))
                    continue
                }

                if (line.startsWith("@@ ")) {
                    if (isCode) {
                        items.add(
                            DiffRecyclerItem.SectionTwoFiles(
                                lineNumberRange,
                                code.toList(),
                                code.toList()
                            )
                        )
                        code.clear()
                    }
                    lineNumberRange = line
                    isCode = true
                    continue
                }

                if (isCode) {
                    code.add(line)
                }
            }

            // add last
            if (isOneFile) {
                items.add(
                    DiffRecyclerItem.SectionOneFile(
                        lineNumberRange,
                        code.toList()
                    )
                )
            } else {
                items.add(
                    DiffRecyclerItem.SectionTwoFiles(
                        lineNumberRange,
                        code.toList(),
                        code.toList()
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
            val data: String
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