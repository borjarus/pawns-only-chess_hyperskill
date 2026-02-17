import org.hyperskill.hstest.dynamic.DynamicTest
import org.hyperskill.hstest.stage.StageTest
import org.hyperskill.hstest.testcase.CheckResult
import org.hyperskill.hstest.testcase.CheckResult.correct
import org.hyperskill.hstest.testcase.CheckResult.wrong
import org.hyperskill.hstest.testing.TestedProgram

class PawnsOnlyChessTest : StageTest<Any>() {
    @DynamicTest
    fun testAdd5(): CheckResult {
        val pawnsWhite = MutableList<Pair<Int, Int>>(8) { index -> Pair(1, index) }
        val pawnsBlack = MutableList<Pair<Int, Int>>(8) { index -> Pair(6, index) }

        val main = TestedProgram()
        var outputString = main.start()

        var currentPos = checkOutput(outputString.lowercase(), 0, "pawns-only chess")
        if (currentPos == -1) return wrong("Program title \"Pawns-Only Chess\" is expected.")
        currentPos = checkOutput(outputString.lowercase(), currentPos, "first player's name:")
        if (currentPos == -1) return wrong("Player 1 name prompt \"First Player's name:\" is expected.")

        outputString = main.execute("John")
        currentPos = checkOutput(outputString.lowercase(), 0, "second player's name:")
        if (currentPos == -1) return wrong("Player 2 name prompt \"Second Player's name:\" is expected.")

        outputString = main.execute("Amelia")
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        for (ch in 'a'..'h') {
            pawnsWhite.apply {
                remove(Pair(1, ch - 'h' + 7))
                add(Pair(3, ch - 'h' + 7))
            }

            var move = "${ch}2${ch}4"
            outputString = main.execute(move)
            currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
                if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
            }
            currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
            if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

            pawnsBlack.apply {
                remove(Pair(6, ch - 'h' + 7))
                add(Pair(4, ch - 'h' + 7))
            }

            move = "${ch}7${ch}5"
            outputString = main.execute(move)
            currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
                if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
            }
            currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
            if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")
        }

        outputString = main.execute("exit")
        currentPos = checkOutput(outputString.lowercase(), 0, "bye!")
        if (currentPos == -1) return wrong("Exit message \"Bye!\" is expected.")

        if (outputString.lastIndex >= currentPos && outputString.substring(currentPos).isNotBlank())
            return wrong("Unexpected output after exit message \"Bye!\".")

        if (!main.isFinished) return wrong("The application didn't exit.")

        return correct()
    }

    @DynamicTest
    fun test1(): CheckResult {
        val pawnsWhite = MutableList<Pair<Int, Int>>(8) { index -> Pair(1, index) }
        val pawnsBlack = MutableList<Pair<Int, Int>>(8) { index -> Pair(6, index) }

        val main = TestedProgram()
        var outputString = main.start()

        var currentPos = checkOutput(outputString.lowercase(), 0, "pawns-only chess")
        if (currentPos == -1) return wrong("Program title \"Pawns-Only Chess\" is expected.")
        currentPos = checkOutput(outputString.lowercase(), currentPos, "first player's name:")
        if (currentPos == -1) return wrong("Player 1 name prompt \"First Player's name:\" is expected.")

        outputString = main.execute("John")
        currentPos = checkOutput(outputString.lowercase(), 0, "second player's name:")
        if (currentPos == -1) return wrong("Player 2 name prompt \"Second Player's name:\" is expected.")

        outputString = main.execute("Amelia")
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 4))
            add(Pair(3, 4))
        }

        var move = "e2e4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 3))
            add(Pair(4, 3))
        }

        move = "d7d5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(3, 4))
            add(Pair(4, 3))
        }
        pawnsBlack.remove(Pair(4, 3))

        move = "e4d5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 2))
            add(Pair(5, 2))
        }

        move = "c7c6"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(4, 3))
            add(Pair(5, 3))
        }

        move = "d5d6"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(5, 2))
            add(Pair(4, 2))
        }

        move = "c6c5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(5, 3))
            add(Pair(6, 3))
        }

        move = "d6d7"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(4, 2))
            add(Pair(3, 2))
        }

        move = "c5c4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(6, 3))
            add(Pair(7, 3))
        }

        move = "d7d8"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "white wins!", "bye!")
        if (currentPos == -1) return wrong("Wrong output after white pawn reaches 8th rank.")

        if (outputString.lastIndex >= currentPos && outputString.substring(currentPos).isNotBlank())
            return wrong("Unexpected output after exit message \"Bye!\".")

        if (!main.isFinished) return wrong("The application didn't exit.")

        return correct()
    }

    @DynamicTest
    fun test2(): CheckResult {
        val pawnsWhite = MutableList<Pair<Int, Int>>(8) { index -> Pair(1, index) }
        val pawnsBlack = MutableList<Pair<Int, Int>>(8) { index -> Pair(6, index) }

        val main = TestedProgram()
        var outputString = main.start()

        var currentPos = checkOutput(outputString.lowercase(), 0, "pawns-only chess")
        if (currentPos == -1) return wrong("Program title \"Pawns-Only Chess\" is expected.")
        currentPos = checkOutput(outputString.lowercase(), currentPos, "first player's name:")
        if (currentPos == -1) return wrong("Player 1 name prompt \"First Player's name:\" is expected.")

        outputString = main.execute("John")
        currentPos = checkOutput(outputString.lowercase(), 0, "second player's name:")
        if (currentPos == -1) return wrong("Player 2 name prompt \"Second Player's name:\" is expected.")

        outputString = main.execute("Amelia")
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 4))
            add(Pair(3, 4))
        }

        var move = "e2e4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 3))
            add(Pair(4, 3))
        }

        move = "d7d5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 1))
            add(Pair(2, 1))
        }

        move = "b2b3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(4, 3))
            add(Pair(3, 4))
        }
        pawnsWhite.remove(Pair(3, 4))

        move = "d5e4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(2, 1))
            add(Pair(3, 1))
        }

        move = "b3b4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(3, 4))
            add(Pair(2, 4))
        }

        move = "e4e3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(3, 1))
            add(Pair(4, 1))
        }

        move = "b4b5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(2, 4))
            add(Pair(1, 4))
        }

        move = "e3e2"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(4, 1))
            add(Pair(5, 1))
        }

        move = "b5b6"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(1, 4))
            add(Pair(0, 4))
        }

        move = "e2e1"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "black wins!", "bye!")
        if (currentPos == -1) return wrong("Wrong output after black pawn reaches 1st rank.")

        if (outputString.lastIndex >= currentPos && outputString.substring(currentPos).isNotBlank())
            return wrong("Unexpected output after exit message \"Bye!\".")

        if (!main.isFinished) return wrong("The application didn't exit.")

        return correct()
    }

    @DynamicTest
    fun test3(): CheckResult {
        val pawnsWhite = MutableList<Pair<Int, Int>>(8) { index -> Pair(1, index) }
        val pawnsBlack = MutableList<Pair<Int, Int>>(8) { index -> Pair(6, index) }

        val main = TestedProgram()
        var outputString = main.start()

        var currentPos = checkOutput(outputString.lowercase(), 0, "pawns-only chess")
        if (currentPos == -1) return wrong("Program title \"Pawns-Only Chess\" is expected.")
        currentPos = checkOutput(outputString.lowercase(), currentPos, "first player's name:")
        if (currentPos == -1) return wrong("Player 1 name prompt \"First Player's name:\" is expected.")

        outputString = main.execute("John")
        currentPos = checkOutput(outputString.lowercase(), 0, "second player's name:")
        if (currentPos == -1) return wrong("Player 2 name prompt \"Second Player's name:\" is expected.")

        outputString = main.execute("Amelia")
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 0))
            add(Pair(3, 0))
        }

        var move = "a2a4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 1))
            add(Pair(4, 1))
        }

        move = "b7b5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(3, 0))
            add(Pair(4, 1))
        }
        pawnsBlack.remove(Pair(4, 1))

        move = "a4b5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 2))
            add(Pair(5, 2))
        }

        move = "c7c6"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(4, 1))
            add(Pair(5, 2))
        }
        pawnsBlack.remove(Pair(5, 2))

        move = "b5c6"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 0))
            add(Pair(4, 0))
        }

        move = "a7a5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(5, 2))
            add(Pair(6, 3))
        }
        pawnsBlack.remove(Pair(6, 3))

        move = "c6d7"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(4, 0))
            add(Pair(3, 0))
        }

        move = "a5a4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 3))
            add(Pair(3, 3))
        }

        move = "d2d4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 4))
            add(Pair(4, 4))
        }

        move = "e7e5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(3, 3))
            add(Pair(4, 4))
        }
        pawnsBlack.remove(Pair(4, 4))

        move = "d4e5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 5))
            add(Pair(5, 5))
        }

        move = "f7f6"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(4, 4))
            add(Pair(5, 5))
        }
        pawnsBlack.remove(Pair(5, 5))

        move = "e5f6"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 7))
            add(Pair(4, 7))
        }

        move = "h7h5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(5, 5))
            add(Pair(6, 6))
        }
        pawnsBlack.remove(Pair(6, 6))

        move = "f6g7"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(3, 0))
            add(Pair(2, 0))
        }

        move = "a4a3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 1))
            add(Pair(2, 0))
        }
        pawnsBlack.remove(Pair(2, 0))

        move = "b2a3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(4, 7))
            add(Pair(3, 7))
        }

        move = "h5h4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 2))
            add(Pair(2, 2))
        }

        move = "c2c3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(3, 7))
            add(Pair(2, 7))
        }

        move = "h4h3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 6))
            add(Pair(2, 7))
        }
        pawnsBlack.remove(Pair(2, 7))

        move = "g2h3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "white wins!", "bye!")
        if (currentPos == -1) return wrong("Wrong output after white wins after capturing all black pawns.")

        if (outputString.lastIndex >= currentPos && outputString.substring(currentPos).isNotBlank())
            return wrong("Unexpected output after exit message \"Bye!\".")

        if (!main.isFinished) return wrong("The application didn't exit.")

        return correct()
    }

    @DynamicTest
    fun test4(): CheckResult {
        val pawnsWhite = MutableList<Pair<Int, Int>>(8) { index -> Pair(1, index) }
        val pawnsBlack = MutableList<Pair<Int, Int>>(8) { index -> Pair(6, index) }

        val main = TestedProgram()
        var outputString = main.start()

        var currentPos = checkOutput(outputString.lowercase(), 0, "pawns-only chess")
        if (currentPos == -1) return wrong("Program title \"Pawns-Only Chess\" is expected.")
        currentPos = checkOutput(outputString.lowercase(), currentPos, "first player's name:")
        if (currentPos == -1) return wrong("Player 1 name prompt \"First Player's name:\" is expected.")

        outputString = main.execute("John")
        currentPos = checkOutput(outputString.lowercase(), 0, "second player's name:")
        if (currentPos == -1) return wrong("Player 2 name prompt \"Second Player's name:\" is expected.")

        outputString = main.execute("Amelia")
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 0))
            add(Pair(3, 0))
        }

        var move = "a2a4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 1))
            add(Pair(4, 1))
        }

        move = "b7b5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 7))
            add(Pair(2, 7))
        }

        move = "h2h3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(4, 1))
            add(Pair(3, 0))
        }
        pawnsWhite.remove(Pair(3, 0))

        move = "b5a4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 1))
            add(Pair(2, 1))
        }

        move = "b2b3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(3, 0))
            add(Pair(2, 1))
        }
        pawnsWhite.remove(Pair(2, 1))

        move = "a4b3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(2, 7))
            add(Pair(3, 7))
        }

        move = "h3h4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(2, 1))
            add(Pair(1, 2))
        }
        pawnsWhite.remove(Pair(1, 2))

        move = "b3c2"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(3, 7))
            add(Pair(4, 7))
        }

        move = "h4h5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 2))
            add(Pair(4, 2))
        }

        move = "c7c5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 3))
            add(Pair(3, 3))
        }

        move = "d2d4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(4, 2))
            add(Pair(3, 3))
        }
        pawnsWhite.remove(Pair(3, 3))

        move = "c5d4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 4))
            add(Pair(2, 4))
        }

        move = "e2e3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(3, 3))
            add(Pair(2, 4))
        }
        pawnsWhite.remove(Pair(2, 4))

        move = "d4e3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 6))
            add(Pair(3, 6))
        }

        move = "g2g4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(2, 4))
            add(Pair(1, 5))
        }
        pawnsWhite.remove(Pair(1, 5))

        move = "e3f2"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(4, 7))
            add(Pair(5, 7))
        }

        move = "h5h6"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 6))
            add(Pair(5, 7))
        }
        pawnsWhite.remove(Pair(5, 7))

        move = "g7h6"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(3, 6))
            add(Pair(4, 6))
        }

        move = "g4g5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(5, 7))
            add(Pair(4, 6))
        }
        pawnsWhite.remove(Pair(4, 6))

        move = "h6g5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "black wins!", "bye!")
        if (currentPos == -1) return wrong("Wrong output after black wins after capturing all white pawns.")

        if (outputString.lastIndex >= currentPos && outputString.substring(currentPos).isNotBlank())
            return wrong("Unexpected output after exit message \"Bye!\".")

        if (!main.isFinished) return wrong("The application didn't exit.")

        return correct()
    }

    @DynamicTest
    fun test5(): CheckResult {
        val pawnsWhite = MutableList<Pair<Int, Int>>(8) { index -> Pair(1, index) }
        val pawnsBlack = MutableList<Pair<Int, Int>>(8) { index -> Pair(6, index) }

        val main = TestedProgram()
        var outputString = main.start()

        var currentPos = checkOutput(outputString.lowercase(), 0, "pawns-only chess")
        if (currentPos == -1) return wrong("Program title \"Pawns-Only Chess\" is expected.")
        currentPos = checkOutput(outputString.lowercase(), currentPos, "first player's name:")
        if (currentPos == -1) return wrong("Player 1 name prompt \"First Player's name:\" is expected.")

        outputString = main.execute("John")
        currentPos = checkOutput(outputString.lowercase(), 0, "second player's name:")
        if (currentPos == -1) return wrong("Player 2 name prompt \"Second Player's name:\" is expected.")

        outputString = main.execute("Amelia")
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 0))
            add(Pair(3, 0))
        }

        var move = "a2a4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 1))
            add(Pair(4, 1))
        }

        move = "b7b5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(3, 0))
            add(Pair(4, 1))
        }
        pawnsBlack.remove(Pair(4, 1))

        move = "a4b5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 2))
            add(Pair(5, 2))
        }

        move = "c7c6"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(4, 1))
            add(Pair(5, 2))
        }
        pawnsBlack.remove(Pair(5, 2))

        move = "b5c6"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 0))
            add(Pair(4, 0))
        }

        move = "a7a5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(5, 2))
            add(Pair(6, 3))
        }
        pawnsBlack.remove(Pair(6, 3))

        move = "c6d7"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(4, 0))
            add(Pair(3, 0))
        }

        move = "a5a4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 3))
            add(Pair(3, 3))
        }

        move = "d2d4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 4))
            add(Pair(4, 4))
        }

        move = "e7e5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(3, 3))
            add(Pair(4, 4))
        }
        pawnsBlack.remove(Pair(4, 4))

        move = "d4e5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 5))
            add(Pair(5, 5))
        }

        move = "f7f6"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(4, 4))
            add(Pair(5, 5))
        }
        pawnsBlack.remove(Pair(5, 5))

        move = "e5f6"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(6, 7))
            add(Pair(4, 7))
        }

        move = "h7h5"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(5, 5))
            add(Pair(6, 6))
        }
        pawnsBlack.remove(Pair(6, 6))

        move = "f6g7"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(3, 0))
            add(Pair(2, 0))
        }

        move = "a4a3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 1))
            add(Pair(2, 0))
        }
        pawnsBlack.remove(Pair(2, 0))

        move = "b2a3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "amelia's turn:")
        if (currentPos == -1) return wrong("Player 2 prompt to play \"Amelia's turn:\" is expected.")

        pawnsBlack.apply {
            remove(Pair(4, 7))
            add(Pair(3, 7))
        }

        move = "h5h4"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "john's turn:")
        if (currentPos == -1) return wrong("Player 1 prompt to play \"John's turn:\" is expected.")

        pawnsWhite.apply {
            remove(Pair(1, 7))
            add(Pair(2, 7))
        }

        move = "h2h3"
        outputString = main.execute(move)
        currentPos = parseChessboard(outputString, 0, pawnsWhite, pawnsBlack, move).let { result ->
            if (result.isIncorrect) return wrong(result.errorMsg) else result.searchPosition
        }
        currentPos = checkOutput(outputString.lowercase(), currentPos, "stalemate!", "bye!")
        if (currentPos == -1) return wrong("Wrong output after white wins after stalemate.")

        if (outputString.lastIndex >= currentPos && outputString.substring(currentPos).isNotBlank())
            return wrong("Unexpected output after exit message \"Bye!\".")

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
    move: String? = null
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
                val errorMsg = if (move?.take(2) == chessboardPos || move?.takeLast(2) == chessboardPos) {
                    "After making a move \"$move\"; "
                } else ""

                if (actualContent.isEmpty() && expectedContent.isNotEmpty()) return BoardStateResult.error(
                    errorMsg + "Expected a pawn '$expectedContent' at $chessboardPos, but got nothing."
                )

                if (actualContent.isNotEmpty() && expectedContent.isEmpty()) return BoardStateResult.error(
                    errorMsg + "Unexpected pawn '$actualContent' found at $chessboardPos."
                )

                if (actualContent.isNotEmpty() && expectedContent.isNotEmpty()) return BoardStateResult.error(
                    errorMsg + "Expected pawn '$expectedContent' at $chessboardPos, but got '$actualContent'."
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