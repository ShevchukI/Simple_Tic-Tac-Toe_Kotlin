package tictactoe

fun main() {

    val gameBoard = GameBoard()
    gameBoard.printGrid()
    do {
        do {
            val isCorrectInput = gameBoard.makeMove(readln())
        } while (!isCorrectInput)
        gameBoard.printGrid()
        gameBoard.updateState()
    } while (!gameBoard.gameIsOver())
    gameBoard.printGameState()
}
