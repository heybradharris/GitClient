            var firstCommitLineNumbers: CharSequence = "\n"
            var secondCommitLineNumbers: CharSequence = "\n"
            var firstCommitLines: CharSequence = "\n"
            var secondCommitLines: CharSequence = "\n"
            var firstCommitCounter = 0
            var secondCommitCounter = 0
            for (line in diff.trimEnd().lines()) {
                        items.add(
                            DiffRecyclerItem.Diff(
                                firstCommitLineNumbers,
                                secondCommitLineNumbers,
                                firstCommitLines,
                                secondCommitLines
                        )
                        firstCommitLineNumbers = "\n"
                        secondCommitLineNumbers = "\n"
                        firstCommitLines = "\n"
                        secondCommitLines = "\n"
                            DiffRecyclerItem.Diff(
                                firstCommitLineNumbers,
                                secondCommitLineNumbers,
                        firstCommitLineNumbers = "\n"
                        secondCommitLineNumbers = "\n"
                        firstCommitLines = "\n"
                        secondCommitLines = "\n"
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
                        firstCommitLineNumbers = TextUtils.concat(firstCommitLineNumbers, "\n")
                        secondCommitLineNumbers = TextUtils.concat(secondCommitLineNumbers, secondCommitCounter.toString(), "\n")
                        secondCommitCounter++
                        secondCommitLineNumbers = TextUtils.concat(secondCommitLineNumbers, secondCommitCounter.toString(), "\n")
                        secondCommitCounter++
                        firstCommitLineNumbers = TextUtils.concat(firstCommitLineNumbers, firstCommitCounter.toString(), "\n")
                        firstCommitCounter++
                                secondCommitLineNumbers = TextUtils.concat(secondCommitLineNumbers, "\n")
                        println(line)
                        firstCommitLines = TextUtils.concat(firstCommitLines, span, "\n")
                        firstCommitLineNumbers = TextUtils.concat(firstCommitLineNumbers, firstCommitCounter.toString(), "\n")
                        firstCommitCounter++
                        secondCommitLines = TextUtils.concat(secondCommitLines, span, "\n")
                        secondCommitLineNumbers = TextUtils.concat(secondCommitLineNumbers, secondCommitCounter.toString(), "\n")
                        secondCommitCounter++
            // add last Diff
            items.add(
                DiffRecyclerItem.Diff(
                    firstCommitLineNumbers,
                    secondCommitLineNumbers,
                    firstCommitLines,
                    secondCommitLines
            )