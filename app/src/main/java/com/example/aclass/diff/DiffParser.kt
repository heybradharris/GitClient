package com.example.aclass.diff

import android.graphics.Color
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.BackgroundColorSpan

class DiffParser {
    companion object {
        fun parseDiff(diff: String): List<DiffRecyclerItem> {
            val items = mutableListOf<DiffRecyclerItem>()

            var lineNumberRange = ""
            var isCode = false
            var firstCommitLines: CharSequence = "\n"
            var secondCommitLines: CharSequence = "\n"
            var continuousRedLineCount = 0

            for (line in diff.lines()) {

                val title = checkForTitle(line)
                if (title != "") {
                    if (isCode) {
                        items.add(
                            DiffRecyclerItem.SectionTwoFiles(
                                lineNumberRange,
                                firstCommitLines,
                                secondCommitLines
                            )
                        )
                        lineNumberRange = ""
                        isCode = false
                        firstCommitLines = "\n"
                        secondCommitLines = "\n"
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
                        firstCommitLines = "\n"
                        secondCommitLines = "\n"
                    }
                    lineNumberRange = line
                    isCode = true
                    continuousRedLineCount = 0
                    continue
                }

                if (isCode) {
                    var isRed: Boolean
                    var isGreen: Boolean

                    val span: SpannableString = when {
                        line.startsWith("+") -> {
                            val str = SpannableString(line)
                            str.setSpan(BackgroundColorSpan(Color.GREEN), 0, line.length, 0)
                            isGreen = true
                            isRed = false
                            str
                        }
                        line.startsWith("-") -> {
                            val str = SpannableString(line)
                            str.setSpan(BackgroundColorSpan(Color.RED), 0, line.length, 0)
                            isRed = true
                            isGreen = false
                            str
                        }
                        else -> {
                            isGreen = false
                            isRed = false
                            SpannableString(line)
                        }
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

            items.add(
                DiffRecyclerItem.SectionTwoFiles(
                    lineNumberRange,
                    firstCommitLines,
                    secondCommitLines
                )
            )

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