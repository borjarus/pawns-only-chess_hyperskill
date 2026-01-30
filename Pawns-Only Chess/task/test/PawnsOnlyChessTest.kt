import org.hyperskill.hstest.dynamic.DynamicTest
import org.hyperskill.hstest.stage.StageTest
import org.hyperskill.hstest.testcase.CheckResult
import org.hyperskill.hstest.testcase.CheckResult.correct
import org.hyperskill.hstest.testcase.CheckResult.wrong
import org.hyperskill.hstest.testing.TestedProgram

val pawnsWhite = List<Pair<Int, Int>>(8) { index -> Pair(1, index) }
val pawnsBlack = List<Pair<Int, Int>>(8) { index -> Pair(6, index) }

class PawnsOnlyChessTest : StageTest<Any>() {
    @DynamicTest
    fun chessboardTest(): CheckResult {
        val main = TestedProgram()
        val outputString = main.start()

        var currentPos = checkOutput(outputString.lowercase(), 0, "pawns-only chess")
        if (currentPos == -1) return wrong("Program title \"Pawns-Only Chess\" is expected.")

        currentPos =
            parseChessboard(outputString.substring(currentPos), currentPos, pawnsWhite, pawnsBlack).let { result ->
                if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
            }

        if (outputString.lastIndex >= currentPos && outputString.substring(currentPos).isNotBlank()) {
            return wrong("Unexpected output after the chessboard printout.")
        }

        if (!main.isFinished) return wrong("The application didn't exit.")

        return correct()
    }
}

data class BoardStateResult(
    val errorMsg: String = "",
    val searchPosition: Int = -1,
) {
    val isIncorrect: Boolean get() = searchPosition == -1

    companion object {
        fun success(searchPosition: Int) = BoardStateResult(searchPosition = searchPosition)
        fun error(message: String) = BoardStateResult(errorMsg = message)
    }
}

const val BOARD_HEIGHT = 17

fun parseChessboard(
    output: String,
    searchPos: Int,
    pawnsWhite: List<Pair<Int, Int>>,
    pawnsBlack: List<Pair<Int, Int>>,
): BoardStateResult {
    val lines = output.lines()

    val chessboardStart = "  +---+---+---+---+---+---+---+---+"
    val boardStartIndex = lines.indexOfFirst { it.trimEnd() == chessboardStart }
    if (boardStartIndex == -1) return BoardStateResult.error(
        "Chessboard printout not found; the printout should start with the line \"$chessboardStart\"."
    )

    val chessboardEnd = "    a   b   c   d   e   f   g   h"
    val boardEndIndex = lines.indexOfFirst { it.startsWith(chessboardEnd) }
    if (boardEndIndex == -1) return BoardStateResult.error(
        "End of chessboard printout not found; the printout should end with the line \"$chessboardEnd\"."
    )

    if (boardEndIndex <= boardStartIndex) return BoardStateResult.error(
        "Could not parse chessboard printout. Check if your chessboard printout matches provided example(s)."
    )

    if (boardEndIndex - boardStartIndex != BOARD_HEIGHT) return BoardStateResult.error(
        "Expected chessboard printout to have ${BOARD_HEIGHT + 1} lines, "
                + "but found ${boardEndIndex - boardStartIndex + 1} lines."
    )

    if (boardStartIndex != 0) {
        for (i in 0 until boardStartIndex) {
            if (lines[i].trim().isNotEmpty()) return BoardStateResult.error(
                "Unexpected output before the chessboard printout \"${lines[i]}\".\n"
                        + "If the output is meant to be part of the chessboard, check that the chessboard printout "
                        + "is correct and starts with \"$chessboardStart\"."
            )
        }
    }

    val searchPosition =
        lines.subList(0, boardEndIndex).joinToString("\n").length + chessboardEnd.length + 1 + searchPos

    val boardLines = lines.subList(boardStartIndex, boardEndIndex + 1)
    val expectedBoard = createChessboardStringList(pawnsWhite, pawnsBlack)
    for (rowIndex in 0..BOARD_HEIGHT) {
        val expectedRow = expectedBoard[rowIndex]
        val actualRow = boardLines[rowIndex].trimEnd()

        val expectedRowStructure = expectedRow.stripPawns()
        val actualRowStructure = actualRow.stripPawns()

        if (expectedRowStructure != actualRowStructure) return BoardStateResult.error(
            "Mismatch at line ${rowIndex + 1} of the chessboard printout: "
                    + "expected \"${expectedBoard[rowIndex]}\", but got \"${boardLines[rowIndex]}\"."
        )

        if (rowIndex % 2 == 0 || rowIndex == BOARD_HEIGHT) continue

        val actualCells = actualRow.split('|').map { it.trim() }
        val expectedCells = expectedRow.split('|').map { it.trim() }

        for (j in 0 until 8) {
            val actualContent = actualCells[j + 1]
            val expectedContent = expectedCells[j + 1]

            if (actualContent != expectedContent) {
                val chessboardPos = "${'a' + j}${expectedCells[0]}"

                if (actualContent.isEmpty() && expectedContent.isNotEmpty()) return BoardStateResult.error(
                    "Expected a pawn '$expectedContent' at $chessboardPos, but got nothing."
                )

                if (actualContent.isNotEmpty() && expectedContent.isEmpty()) return BoardStateResult.error(
                    "Unexpected pawn '$actualContent' found at $chessboardPos."
                )

                if (actualContent.isNotEmpty() && expectedContent.isNotEmpty()) return BoardStateResult.error(
                    "Expected a pawn '$expectedContent' at $chessboardPos, but got '$actualContent'."
                )
            }
        }
    }

    return BoardStateResult.success(searchPosition)
}

fun createChessboardStringList(pawnsWhite: List<Pair<Int, Int>>, pawnsBlack: List<Pair<Int, Int>>): List<String> {
    var chessboard = "  +---+---+---+---+---+---+---+---+\n"
    for (i in 7 downTo 0) {
        chessboard += "${i + 1} |"
        for (j in 0..7) {
            val square = when {
                pawnsWhite.contains(Pair(i, j)) -> 'W'
                pawnsBlack.contains(Pair(i, j)) -> 'B'
                else -> ' '
            }
            chessboard += " $square |"
        }
        chessboard += "\n  +---+---+---+---+---+---+---+---+\n"
    }
    chessboard += "    a   b   c   d   e   f   g   h"
    return chessboard.split("\n")
}

fun String.stripPawns(): String = replace('W', ' ').replace('B', ' ')

fun checkOutput(outputString: String, searchPos: Int, vararg checkStr: String): Int {
    var searchPosition = searchPos
    for (str in checkStr) {
        val findPosition = outputString.indexOf(str, searchPosition)
        if (findPosition == -1) return -1
        if (outputString.substring(searchPosition until findPosition).isNotBlank()) return -1
        searchPosition = findPosition + str.length
    }
    return searchPosition
}