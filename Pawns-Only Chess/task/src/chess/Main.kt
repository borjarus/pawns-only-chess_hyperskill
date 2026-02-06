package chess

enum class PlayerColor(val symbol: String) {
    White(" W "),
    Black(" B ");
}

fun promptForInput(promt: String):String {
    println(promt)
    return readln()
}

fun createInitialBoard(): MutableList<MutableList<String>> =
    MutableList(8) { row ->
        MutableList(8) { col ->
            when(row){
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

fun isValidMove(
    board: List<List<String>>,
    start: String,
    end: String,
    playerColor: PlayerColor,
    movedBefore: MutableSet<String>,
): Boolean {
    val (sRow, sCol) = notationToIndices(start)
    val (eRow, eCol) = notationToIndices(end)

    if (sRow !in 0..7 || sCol !in 0..7 || eRow !in 0..7 || eCol !in 0..7) return false

    val pawn = board[sRow][sCol]
    val dest = board[eRow][eCol]

    // Check player pawn at start
    if (pawn != playerColor.symbol) return false
    if (dest != "   ") return false

    // Forward move conditions
    return when (playerColor) {
        PlayerColor.White -> {
            // white moves up the board so row decreases
            if (sCol != eCol) return false // only forward in same column allowed

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
            // black moves down the board so row increases
            if (sCol != eCol) return false // only forward in same column allowed

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
) {
    val (sRow, sCol) = notationToIndices(start)
    val (eRow, eCol) = notationToIndices(end)

    board[eRow][eCol] = board[sRow][sCol]
    board[sRow][sCol] = "   "

    // mark the pawn has moved from this new square
    movedBefore.add(end)
}

fun gameLoop(firstPlayer: String, secondPlayer: String ) {
    val movePattern = Regex("[a-h][1-8][a-h][1-8]")
    val board = createInitialBoard()
    val movedBefore = mutableSetOf<String>()
    var currentPlayer = firstPlayer
    var currentColor = PlayerColor.White

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

        if (!isValidMove(board, start, end, currentColor, movedBefore)) {
            println("Invalid Input")
            continue
        }

        // Move the pawn
        updateBoard(board, start, end, movedBefore)

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


fun printBoard(){
    val renderedBoard = renderBoard(createInitialBoard())
    println(renderedBoard)
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
