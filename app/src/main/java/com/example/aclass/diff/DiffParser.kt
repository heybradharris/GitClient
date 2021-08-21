package com.example.aclass.diff

import android.graphics.Color
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.BackgroundColorSpan
import kotlinx.coroutines.withContext

class DiffParser {
    companion object {
        fun parseDiff(diff: String): List<DiffRecyclerItem> {
            val items = mutableListOf<DiffRecyclerItem>()

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
                    continuousRedLineCount = 0
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
                            for (i in 0 until continuousRedLineCount) {
                                secondCommitLines = TextUtils.concat(secondCommitLines, "\n")
                            }
                            continuousRedLineCount = 0

                            firstCommitLines = TextUtils.concat(firstCommitLines, span, "\n")
                            secondCommitLines = TextUtils.concat(secondCommitLines, span, "\n")
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


            return items
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
    }
}