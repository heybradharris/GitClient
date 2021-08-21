package com.example.aclass.diff

import android.text.SpannableString

sealed class DiffRecyclerItem {
    class Title(val title: String) : DiffRecyclerItem()
    class SectionTwoFiles(
        val lineNumberRange: String,
        val firstCommitLines: CharSequence,
        val secondCommitLines: CharSequence
    ) : DiffRecyclerItem()
    class SectionOneFile(
        val lineNumberRange: String,
        val commitLines: CharSequence
    ) : DiffRecyclerItem()
}