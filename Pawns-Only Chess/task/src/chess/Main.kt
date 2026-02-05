package chess

fun main() {
    println(" Pawns-Only Chess")

    val files = listOf("a", "b", "c", "d", "e", "f", "g", "h")
    val ranks = (8 downTo 1).toList()

    ranks.forEach { rank ->
        println("  +---+---+---+---+---+---+---+---+")
        val content = when (rank) {
            7 -> " B "
            2 -> " W "
            else -> "   "
        }
        println("$rank |$content|$content|$content|$content|$content|$content|$content|$content|")
    }
    println("  +---+---+---+---+---+---+---+---+")
    println("    ${files.joinToString("   ")}")

}