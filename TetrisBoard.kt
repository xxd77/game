import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import java.util.Random

class TetrisGame {
    companion object {
        const val WIDTH = 10
        const val HEIGHT = 20
        const val INITIAL_DELAY = 800L
    }

    private val random = Random()
    var board by mutableStateOf(List(HEIGHT) { MutableList(WIDTH) { Color.Black } })
    var currentPiece by mutableStateOf(Tetromino.random())
    var nextPiece by mutableStateOf(Tetromino.random())
    var pieceX by mutableStateOf(WIDTH / 2 - 2)
    var pieceY by mutableStateOf(0)
    var score by mutableStateOf(0)
    var isGameOver by mutableStateOf(false)

    private var dropDelay = INITIAL_DELAY

    init {
        startAutoDrop()
    }

    private fun startAutoDrop() {
        kotlinx.coroutines.MainScope().launch {
            while (!isGameOver) {
                delay(dropDelay)
                if (!moveDown()) hardDrop() // 自然下落失败 = 落地
            }
        }
    }

    fun moveLeft() { if (validMove(pieceX - 1, pieceY)) pieceX-- }
    fun moveRight() { if (validMove(pieceX + 1, pieceY)) pieceX++ }
    fun softDrop() { if (moveDown()) score += 1 }
    fun rotate() {
        val rotated = currentPiece.rotated()
        if (validMove(pieceX, pieceY, rotated)) currentPiece = rotated
    }

    fun hardDrop() {
        while (moveDown()) { score += 2 }
        lockPiece()
    }

    private fun moveDown(): Boolean {
        if (validMove(pieceX, pieceY + 1)) {
            pieceY++
            return true
        }
        return false
    }

    private fun validMove(x: Int, y: Int, piece: Tetromino = currentPiece): Boolean {
        for (row in 0..3) for (col in 0..3) {
            if (piece.shape[row][col] == 1) {
                val boardX = x + col
                val boardY = y + row
                if (boardX < 0 || boardX >= WIDTH || boardY >= HEIGHT) return false
                if (boardY >= 0 && board[boardY][boardX] != Color.Black) return false
            }
        }
        return true
    }

    private fun lockPiece() {
        for (row in 0..3) for (col in 0..3) {
            if (currentPiece.shape[row][col] == 1) {
                val y = pieceY + row
                val x = pieceX + col
                if (y >= 0) board[y][x] = currentPiece.color
            }
        }

        clearLines()
        spawnNewPiece()
    }

    private fun clearLines() {
        var lines = 0
        board = board.filter { row ->
            val hasEmpty = row.any { it == Color.Black }
            if (!hasEmpty) lines++
            hasEmpty
        }
        while (board.size < HEIGHT) {
            board = mutableListOf(MutableList(WIDTH) { Color.Black }) + board
        }
        if (lines > 0) score += when (lines) {
            1 -> 100; 2 -> 300; 3 -> 500; else -> 800
        } * (1000 / dropDelay + 1) // 越快加成越高
    }

    private fun spawnNewPiece() {
        currentPiece = nextPiece
        nextPiece = Tetromino.random()
        pieceX = WIDTH / 2 - 2
        pieceY = 0

        if (!validMove(pieceX, pieceY)) {
            isGameOver = true
        } else {
            // 加速
            dropDelay = maxOf(100, dropDelay - 15)
        }
    }
}
