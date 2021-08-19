package com.example.aclass.diff

sealed class DiffRecyclerItem {
    class Title(val title: String) : DiffRecyclerItem()
    class SectionTwoFiles(
        val lineNumberRange: String,
        val firstCommitLines: List<String>,
        val secondCommitLines: List<String>
    ) : DiffRecyclerItem()
    class SectionOneFile(
        val lineNumberRange: String,
        val commitLines: List<String>
    ) : DiffRecyclerItem()
}