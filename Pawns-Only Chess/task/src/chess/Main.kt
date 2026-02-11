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
        } }

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

/**
 * Converts chess notation (e.g., a2) to board indices
 */
fun notationToIndices(pos: String): Pair<Int, Int> {
    val col = pos[0] - 'a'
    // board row 0 is rank 8, so index = 8 - rank
    val row = 8 - (pos[1] - '0')
    return Pair(row, col)
}

fun isValidCapture(
    board: List<List<String>>,
    sRow: Int, sCol: Int,
    eRow:Int, eCol: Int,
    playerColor: PlayerColor
): Boolean {
    val dest = board[eRow][eCol]
    val opponentSymbol = if (playerColor == PlayerColor.White) PlayerColor.Black.symbol else PlayerColor.White.symbol

    // Destination must have opponent's pawn
    if (dest != opponentSymbol) {
        return false
    }

    // Must be diagonal (one column difference)
    if (kotlin.math.abs(sCol - eCol) != 1){
        return false
    }

    // Must be one row forward in correct direction
    return when (playerColor) {
        PlayerColor.White -> sRow - eRow == 1 // white moves up
        PlayerColor.Black -> eCol - sCol == 1 // black moves down
    }
}

fun isValidEnPassant(
    board: List<List<String>>,
    sRow: Int, sCol: Int,
    eRow:Int, eCol: Int,
    playerColor: PlayerColor,
    lastMove: Move?
): Boolean {
    // No last move means no en passant
    if (lastMove == null) return false

    val (lastFromRow, lastFromCol) = notationToIndices(lastMove.from)
    val (lastToRow, lastToCol) = notationToIndices(lastMove.to)

    // Last move must be opponent's pawn moving 2 squares
    val opponentColor = if (playerColor == PlayerColor.White) PlayerColor.Black else PlayerColor.White
    if (lastMove.color != opponentColor) return false
    if (kotlin.math.abs(lastFromRow - lastToRow) != 2) return false

    when (playerColor) {
        PlayerColor.White -> {
            // White pawn must be on rank 5 (row index 3)
            if (sRow != 3) return false
            // Opponent pawn must be adjacent on same rank
            if (lastToRow != 3) return false
            if (kotlin.math.abs(sCol - lastToCol) != 1) return false
            // Destination must be diagonal forward (rank 6, row index 2)
            if (eRow != 2 || eCol != lastToCol) return false
        }
        PlayerColor.Black -> {
            // Black pawn must be on rank 4 (row index 4)
            if (sRow != 4) return false
            // Opponent pawn must be adjacent on same rank
            if (lastToRow != 4) return false
            if (kotlin.math.abs(sCol - lastToCol) != 1) return false
            // Destination must be diagonal forward (rank 3, row index 5)
            if (eRow != 5 || eCol != lastToCol) return false
        }
    }
    return true
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

    val pawn = board[sRow][sCol]
    val dest = board[eRow][eCol]

    // Check player pawn at start
    if (pawn != playerColor.symbol) return false

    // Check if it's a capture move
    if (dest != "   " || kotlin.math.abs(sCol - eCol) == 1) {
        // Regular diagonal capture
        if (isValidCapture(board, sRow, sCol, eRow, eCol, playerColor)) {
            return true
        }
        // En passant capture
        if (isValidEnPassant(board, sRow, sCol, eRow, eCol, playerColor, lastMove)) {
            return true
        }
        return false
    }
    // Forward move (non-capture) - destination must be empty and same column
    if (dest != "   ") return false
    if (sCol != eCol) return false

    // Forward move conditions
    return when (playerColor) {
        PlayerColor.White -> {
            val distance = sRow - eRow
            when (distance) {
                1 -> true
                2 -> {
                    // must be first move and the square in between must be empty
                    if (movedBefore.contains(start)) return false
                    if (board[sRow - 1][sCol] != "   ") return false
                    true
                }
                else -> false
            }
        }
        PlayerColor.Black -> {
            val distance = eRow - sRow
            when (distance) {
                1 -> true
                2 -> {
                    if (movedBefore.contains(start)) return false
                    if (board[sRow + 1][sCol] != "   ") return false
                    true
                }
                else -> false
            }
        }
    }
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

    // Check if this is en passant capture
    val isEnPassant = kotlin.math.abs(sCol - eCol) == 1 &&
            board[eRow][eCol] != "   " &&
            lastMove != null

    board[eRow][eCol] = board[sRow][sCol]
    board[sRow][sCol] = "   "

    // Remove captured pawn (for en passant, it's on the same rank as starting position)
    if (isEnPassant) {
        board[sRow][eCol] = "   "
    }

    // mark the pawn has moved from this new square
    movedBefore.add(end)
}

fun gameLoop(firstPlayer: String, secondPlayer: String ) {
    val movePattern = Regex("[a-h][1-8][a-h][1-8]")
    val board = createInitialBoard()
    val movedBefore = mutableSetOf<String>()
    var currentPlayer = firstPlayer
    var currentColor = PlayerColor.White
    var lastMove: Move? = null

    while (true){
        println("$currentPlayer's turn:")
        val input = readln()

        if (input == "exit") {
            println("Bye!")
            break
        } else if (!movePattern.matches(input)){
            println("Invalid Input")
            continue
        }

        val start = input.substring(0..1)
        val end = input.substring(2..3)

        // Check if the pawn belongs to the player at start
        val (sRow, sCol) = notationToIndices(start)
        val pawnAtStart = board[sRow][sCol]
        if (pawnAtStart != currentColor.symbol) {
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

        // Switch players
        if (currentPlayer == firstPlayer){
            currentPlayer = secondPlayer
            currentColor = PlayerColor.Black
        } else {
            currentPlayer = firstPlayer
            currentColor = PlayerColor.White
        }
    }
}


fun main() {
    println("Pawns-Only Chess")

    // Get player names
    val firstPlayer = promptForInput("First Player's name:")
    val secondPlayer = promptForInput("Second Player's name:")

    // Print the initial chessboard
    val board = createInitialBoard()
    println(renderBoard(board))

    // Start game loop
    gameLoop(firstPlayer, secondPlayer)
}


