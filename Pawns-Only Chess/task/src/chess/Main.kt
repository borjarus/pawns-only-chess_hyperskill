package chess

fun promptForInput(promt: String):String {
    println(promt)
    return readln()
}

fun gameLoop(firstPlayer: String, secondPlayer: String ) {
    val movePattern = Regex("[a-h][1-8][a-h][1-8]")

    tailrec fun loop(currentPlayer: String): Unit {
        println("$currentPlayer's turn:")
        val input = readln()

        when {
            input == "exit" -> print("Bye!")
            input.matches(movePattern) -> {
                val nextPlayer = if (currentPlayer == firstPlayer) secondPlayer else firstPlayer
                loop(nextPlayer)
            }
            else -> {
                println("Invalid Input")
                loop(currentPlayer)
            }
        }
    }

    loop(firstPlayer)
}

fun createInitialBoard(): List<List<String>> =
    List(8) { row ->
        List(8) { col ->
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

fun printBoard(){
    val renderedBoard = renderBoard(createInitialBoard())
    println(renderedBoard)
}

fun main() {
    println("Pawns-Only Chess")

    // Get player names
    val firstPlayer = promptForInput("First Player's name:")
    val secondPlayer = promptForInput("Second Player's name:")

    // Print the chessboard
    printBoard()

    // Start game loop
    gameLoop(firstPlayer, secondPlayer)
}