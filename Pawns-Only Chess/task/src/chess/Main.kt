package chess

enum class PlayerColor(val symbol: String) {
    White(" W "),
    Black(" B ");
}

data class Move(val from: String, val to: String, val color: PlayerColor)

fun promptForInput(s: String):String {
    println(s)
    return readln()
}

fun createInitialBoard(): MutableList<MutableList<String>> =
    MutableList(8) { row ->
        MutableList(8) { col ->
            when (row) {
                1 -> " B "
                6 -> " W "
                else -> "   "
            }
        }
    }

fun renderBoard(board: List<List<String>>): String {
    val separator = "  +---+---+---+---+---+---+---+---+"
    val columnLabels = "    a   b   c   d   e   f   g   h"

    val rows = board.mapIndexed { index, row ->
        val rank = 8 - index
        val cells = row.joinToString("|", "|", "|") { it }
        "$rank $cells"
    }
    return rows.joinToString("\n$separator\n", "$separator\n", "\n$separator\n$columnLabels")
}

fun notationToIndices(pos: String): Pair<Int, Int> {
    val col = pos[0] - 'a'
    val row = 8 - (pos[1] - '0')
    return Pair(row, col)
}

fun indicesToNotation(row: Int, col: Int): String =
    "${(col + 'a'.code).toChar()}${8 - row}"

fun getOpponentSymbol(playerColor: PlayerColor): String =
    if (playerColor == PlayerColor.White) PlayerColor.Black.symbol else PlayerColor.White.symbol

fun isValidNormalCapture(
    board: List<List<String>>,
    sRow: Int, sCol: Int,
    eRow: Int, eCol: Int,
    playerColor: PlayerColor
): Boolean {
    val dest = board[eRow][eCol]
    val opponentSymbol = getOpponentSymbol(playerColor)

    if (dest != opponentSymbol || kotlin.math.abs(sCol - eCol) != 1) return false

    return when (playerColor) {
        PlayerColor.White -> sRow - eRow == 1
        PlayerColor.Black -> eRow - sRow == 1
    }
}

fun isValidEnPassant(
    board: List<List<String>>,
    sRow: Int, sCol: Int,
    eRow: Int, eCol: Int,
    playerColor: PlayerColor,
    lastMove: Move?
): Boolean {
    if (lastMove == null) return false

    val opponentColor = if (playerColor == PlayerColor.White) PlayerColor.Black else PlayerColor.White
    if (lastMove.color != opponentColor) return false

    val (lastFromRow, _) = notationToIndices(lastMove.from)
    val (lastToRow, lastToCol) = notationToIndices(lastMove.to)

    if (kotlin.math.abs(lastFromRow - lastToRow) != 2) return false

    return when (playerColor) {
        PlayerColor.White -> {
            sRow == 3 && eRow == 2 && eCol == lastToCol &&
                    kotlin.math.abs(sCol - lastToCol) == 1 && lastToRow == 3
        }
        PlayerColor.Black -> {
            sRow == 4 && eRow == 5 && eCol == lastToCol &&
                    kotlin.math.abs(sCol - lastToCol) == 1 && lastToRow == 4
        }
    }
}

fun isValidForwardMove(
    board: List<List<String>>,
    sRow: Int, sCol: Int,
    eRow: Int, eCol: Int,
    playerColor: PlayerColor,
    movedBefore: MutableSet<String>
): Boolean {
    if (sCol != eCol || board[eRow][eCol] != "   ") return false

    return when (playerColor) {
        PlayerColor.White -> {
            val distance = sRow - eRow
            when (distance) {
                1 -> true
                2 -> {
                    val startPos = indicesToNotation(sRow, sCol)
                    sRow == 6 && !movedBefore.contains(startPos) && board[sRow - 1][sCol] == "   "
                }
                else -> false
            }
        }
        PlayerColor.Black -> {
            val distance = eRow - sRow
            when (distance) {
                1 -> true
                2 -> {
                    val startPos = indicesToNotation(sRow, sCol)
                    sRow == 1 && !movedBefore.contains(startPos) && board[sRow + 1][sCol] == "   "
                }
                else -> false
            }
        }
    }
}


fun isValidMove(
    board: List<List<String>>,
    start: String,
    end: String,
    playerColor: PlayerColor,
    movedBefore: MutableSet<String>,
    lastMove: Move?
): Boolean {
    val (sRow, sCol) = notationToIndices(start)
    val (eRow, eCol) = notationToIndices(end)

    if (sRow !in 0..7 || sCol !in 0..7 || eRow !in 0..7 || eCol !in 0..7) return false

    if (board[sRow][sCol] != playerColor.symbol) return false

    return isValidForwardMove(board, sRow, sCol, eRow, eCol, playerColor, movedBefore) ||
            isValidNormalCapture(board, sRow, sCol, eRow, eCol, playerColor) ||
            isValidEnPassant(board, sRow, sCol, eRow, eCol, playerColor, lastMove)
}

fun hasWon(board: List<List<String>>, playerColor: PlayerColor): Boolean {
    val targetRow = if (playerColor == PlayerColor.White) 0 else 7
    val opponentSymbol = getOpponentSymbol(playerColor)

    // Check if player has pawn on target rank
    val reachedEnd = board[targetRow].any { it == playerColor.symbol }

    // Check if all opponent pawns are captured
    val noOpponentPawns = board.all { row -> row.all { it != opponentSymbol } }

    return reachedEnd || noOpponentPawns
}

fun hasAnyValidMove(
    board: List<List<String>>,
    playerColor: PlayerColor,
    movedBefore: MutableSet<String>,
    lastMove: Move?
): Boolean {
    for (sRow in 0..7) {
        for (sCol in 0..7) {
            if (board[sRow][sCol] == playerColor.symbol) {
                val startPos = indicesToNotation(sRow, sCol)

                for (eRow in 0..7) {
                    for (eCol in 0..7) {
                        val endPos = indicesToNotation(eRow, eCol)
                        if (isValidMove(board, startPos, endPos, playerColor, movedBefore, lastMove)) {
                            return true
                        }
                    }
                }
            }
        }
    }
    return false
}

fun updateBoard(
    board: MutableList<MutableList<String>>,
    start: String,
    end: String,
    movedBefore: MutableSet<String>,
    lastMove: Move?
) {
    val (sRow, sCol) = notationToIndices(start)
    val (eRow, eCol) = notationToIndices(end)

    val isEnPassant = board[eRow][eCol] == "   " && kotlin.math.abs(sCol - eCol) == 1

    board[eRow][eCol] = board[sRow][sCol]
    board[sRow][sCol] = "   "

    if (isEnPassant && lastMove != null) {
        val (_, lastToCol) = notationToIndices(lastMove.to)
        board[sRow][lastToCol] = "   "
    }

    movedBefore.add(end)
}

fun gameLoop(firstPlayer: String, secondPlayer: String) {
    val movePattern = Regex("[a-h][1-8][a-h][1-8]")
    val board = createInitialBoard()
    val movedBefore = mutableSetOf<String>()
    var currentPlayer = firstPlayer
    var currentColor = PlayerColor.White
    var lastMove: Move? = null

    while (true) {
        println("$currentPlayer's turn:")
        val input = readln()

        if (input == "exit") {
            println("Bye!")
            break
        }

        if (!movePattern.matches(input)) {
            println("Invalid Input")
            continue
        }

        val start = input.substring(0..1)
        val end = input.substring(2..3)

        val (sRow, sCol) = notationToIndices(start)
        if (board[sRow][sCol] != currentColor.symbol) {
            println("No ${currentColor.name.lowercase()} pawn at $start")
            continue
        }

        if (!isValidMove(board, start, end, currentColor, movedBefore, lastMove)) {
            println("Invalid Input")
            continue
        }

        // Store this move as last move
        lastMove = Move(start, end, currentColor)

        // Move the pawn
        updateBoard(board, start, end, movedBefore, lastMove)

        // Print updated board
        println(renderBoard(board))

        // Check if current player won
        if (hasWon(board, currentColor)) {
            println("${currentColor.name} Wins!")
            println("Bye!")
            break
        }

        // Switch to next player
        currentPlayer = if (currentPlayer == firstPlayer) secondPlayer else firstPlayer
        currentColor = if (currentColor == PlayerColor.White) PlayerColor.Black else PlayerColor.White

        // Check if next player has any valid moves (stalemate)
        if (!hasAnyValidMove(board, currentColor, movedBefore, lastMove)) {
            println("Stalemate!")
            println("Bye!")
            break
        }
    }
}

fun main() {
    println("Pawns-Only Chess")

    // Get player names
    val firstPlayer = promptForInput("First Player's name:")
    val secondPlayer = promptForInput("Second Player's name:")

    // Print the initial chessboard
    println(renderBoard(createInitialBoard()))

    // Start game loop
    gameLoop(firstPlayer, secondPlayer)
}