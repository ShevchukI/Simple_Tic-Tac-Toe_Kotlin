package tictactoe

import java.lang.NumberFormatException
import kotlin.math.abs

class GameBoard {
    private val grid = mutableListOf(
        mutableListOf("_", "_", "_"),
        mutableListOf("_", "_", "_"),
        mutableListOf("_", "_", "_")
    )

    private var gameState = GameState.GAME_NOT_FINISHED
    private var currentPlayer = Player.X

    fun printGrid() {
        println("---------")
        for (row in 0 .. grid.lastIndex) {
            print("| ")
            for (column in 0 .. grid[row].lastIndex) {
                print("${grid[row][column]} ")
            }
            println("| ")
        }
        println("---------")
    }

    fun fillGrid(symbols: String) {
        var symbolIndex = 0
        for (row in 0 .. grid.lastIndex) {
            for (column in 0 .. grid[row].lastIndex) {
                grid[row][column] = symbols[symbolIndex].toString()
                symbolIndex++
            }
        }
    }

    fun updateState() {
        this.gameState = analyzeGameState()
    }

    fun gameIsOver(): Boolean {
        return when(gameState) {
            GameState.DRAW, GameState.X_WINS, GameState.O_WINS -> true
            else -> false
        }
    }

    fun printGameState() {
        println(gameState.value)
    }

    fun makeMove(position: String): Boolean {
        val row: Int
        val column: Int
        try {
            row = position.split(" ")[0].toInt()
            column = position.split(" ")[1].toInt()
        } catch (e: NumberFormatException) {
            println("You should enter numbers!")
            return false
        }

        return setPosition(row, column)
    }

    private fun setPosition(row: Int, column: Int): Boolean {
        if (row !in 1 .. 3 || column !in 1 .. 3) {
            println("Coordinates should be from 1 to 3!")
            return false
        }

        return if (cellIsEmpty(row, column)) {
            if (currentPlayer == Player.X) {
                setSymbol(row, column, "X")
            }
            if (currentPlayer == Player.O) {
               setSymbol(row, column, "O")
            }
            switchPlayer()
            true
        } else {
            println("This cell is occupied! Choose another one!")
            false
        }
    }

    private fun switchPlayer() {
        currentPlayer = when(currentPlayer) {
            Player.X -> Player.O
            Player.O -> Player.X
        }
    }

    private fun cellIsEmpty(row: Int, column: Int): Boolean {
        return grid[row - 1][column - 1] == "_"
    }

    private fun setSymbol(row: Int, column: Int, symbol: String) {
        grid[row - 1][column - 1] = symbol
    }

    private fun analyzeGameState():  GameState {
        val xCount = getSymbolCount("X")
        val oCount = getSymbolCount("O")
        val emptyCount = getSymbolCount("_")
        val xIsWin = playerIsWins("X")
        val oIsWin = playerIsWins("O")

        return if (xIsWin && oIsWin || abs(xCount - oCount) >= 2) {
            GameState.IMPOSSIBLE
        } else if (xIsWin) {
            GameState.X_WINS
        } else if (oIsWin) {
            GameState.O_WINS
        } else if (emptyCount == 0) {
            GameState.DRAW
        } else {
            GameState.GAME_NOT_FINISHED
        }
    }

    private fun getSymbolCount(symbol: String): Int {
        var symbolCount = 0
        for (row in 0 .. grid.lastIndex) {
            for (column in 0 .. grid[row].lastIndex) {
                if (grid[row][column] == symbol) {
                    symbolCount++
                }
            }
        }
        return symbolCount
    }


    private fun playerIsWins(player: String): Boolean {
        return playerWinByRow(player) || playerWinByColumn(player) || playerWinByDiagonals(player)
    }

    private fun playerWinByRow(player: String): Boolean {
        for(row in 0 .. grid.lastIndex) {
            if (grid[row][0] == player && grid[row][1] == player && grid[row][2] == player) {
                return true
            }
        }
        return false
    }

    private fun playerWinByColumn(player: String): Boolean {
        for(column in 0 .. grid.lastIndex) {
            if (grid[0][column] == player && grid[1][column] == player && grid[2][column] == player) {
                return true
            }
        }
        return false
    }

    private fun playerWinByDiagonals(player: String): Boolean {
        if (grid[1][1] == player) {
            if (grid[0][0] == player && grid[2][2] == player) {
                return true
            }
            if (grid[0][2] == player && grid[2][0] == player) {
                return true
            }
        }
        return false
    }
}

enum class GameState(val value: String) {
    GAME_NOT_FINISHED("Game not finished"),
    DRAW("Draw"),
    X_WINS("X wins"),
    O_WINS("O wins"),
    IMPOSSIBLE("Impossible")
}

enum class Player {
    X,
    O
}
