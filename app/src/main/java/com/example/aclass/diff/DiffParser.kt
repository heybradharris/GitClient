package com.example.aclass.diff

import android.graphics.Color
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.BackgroundColorSpan

class DiffParser {
    companion object {
        fun parseDiff(diff: String): List<DiffRecyclerItem> {
            val items = mutableListOf<DiffRecyclerItem>()

            var isCode = false
            var firstCommitLineNumbers: CharSequence = "\n"
            var secondCommitLineNumbers: CharSequence = "\n"
            var firstCommitLines: CharSequence = "\n"
            var secondCommitLines: CharSequence = "\n"
            var firstCommitCounter = 0
            var secondCommitCounter = 0
            var continuousRedLineCount = 0

            for (line in diff.lines()) {

                val title = checkForTitle(line)
                if (title != "") {
                    if (isCode) {
                        items.add(
                            DiffRecyclerItem.Diff(
                                firstCommitLineNumbers,
                                secondCommitLineNumbers,
                                firstCommitLines,
                                secondCommitLines
                            )
                        )
                        firstCommitLineNumbers = "\n"
                        secondCommitLineNumbers = "\n"
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
                            DiffRecyclerItem.Diff(
                                firstCommitLineNumbers,
                                secondCommitLineNumbers,
                                firstCommitLines,
                                secondCommitLines
                            )
                        )
                        firstCommitLineNumbers = "\n"
                        secondCommitLineNumbers = "\n"
                        firstCommitLines = "\n"
                        secondCommitLines = "\n"
                    }
                    // assign range starts
                    val filtered = line.substringAfter("@@")
                        .substringBefore("@@")
                        .filter { it.isDigit() || it == '+' ||  it == ','}
                    println(filtered)
                    // need another counter
                    firstCommitCounter = listOf(
                        filtered.substringBefore(',').toInt(),
                        //filtered.substringAfter(',').substringBefore('+').toInt()
                    ).maxOrNull() ?: -1
                    secondCommitCounter = listOf(
                        filtered.substringAfter('+').substringBefore(',').toInt(),
                        //filtered.substringAfter(',').substringAfter(',').toInt()
                    ).maxOrNull() ?: -1
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
                        firstCommitLineNumbers = TextUtils.concat(firstCommitLineNumbers, "\n")
                        secondCommitLines = TextUtils.concat(secondCommitLines, span, "\n")
                        secondCommitLineNumbers = TextUtils.concat(secondCommitLineNumbers, secondCommitCounter.toString(), "\n")
                        secondCommitCounter++
                    } else if (isGreen && continuousRedLineCount > 0) {
                        secondCommitLines = TextUtils.concat(secondCommitLines, span, "\n")
                        secondCommitLineNumbers = TextUtils.concat(secondCommitLineNumbers, secondCommitCounter.toString(), "\n")
                        secondCommitCounter++
                        continuousRedLineCount--
                    } else if (isRed) {
                        continuousRedLineCount++
                        firstCommitLines = TextUtils.concat(firstCommitLines, span, "\n")
                        firstCommitLineNumbers = TextUtils.concat(firstCommitLineNumbers, firstCommitCounter.toString(), "\n")
                        firstCommitCounter++
                    } else {
                        if (continuousRedLineCount > 0) {
                            for (i in 0 until continuousRedLineCount) {
                                secondCommitLines = TextUtils.concat(secondCommitLines, "\n")
                            }
                            continuousRedLineCount = 0
                        }
                        firstCommitLines = TextUtils.concat(firstCommitLines, span, "\n")
                        firstCommitLineNumbers = TextUtils.concat(firstCommitLineNumbers, firstCommitCounter.toString(), "\n")
                        firstCommitCounter++
                        secondCommitLines = TextUtils.concat(secondCommitLines, span, "\n")
                        secondCommitLineNumbers = TextUtils.concat(secondCommitLineNumbers, secondCommitCounter.toString(), "\n")
                        secondCommitCounter++
                    }
                }
            }

            // add last Diff
            items.add(
                DiffRecyclerItem.Diff(
                    firstCommitLineNumbers,
                    secondCommitLineNumbers,
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