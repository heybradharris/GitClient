package com.example.aclass.diff

sealed class DiffRecyclerItem {
    class Title(val title: String) : DiffRecyclerItem()
    class Diff(
        val firstCommitLineNumbers: CharSequence,
        val secondCommitLineNumbers: CharSequence,
        val firstCommitLines: CharSequence,
        val secondCommitLines: CharSequence
    ) : DiffRecyclerItem()
}